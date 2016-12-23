package net.mctournaments.bukkit.command;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.mctournaments.bukkit.data.MessageType.ERROR;
import static net.mctournaments.bukkit.data.MessageType.INFO;
import static net.mctournaments.bukkit.data.MessageType.INFO_ALTERNATIVE;
import static net.mctournaments.bukkit.data.MessageType.WARNING;
import static net.mctournaments.bukkit.data.Messages.send;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.sk89q.intake.Command;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.Description;
import com.sk89q.intake.Intake;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import com.sk89q.intake.parametric.Injector;
import com.sk89q.intake.parametric.Module;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import com.sk89q.intake.util.auth.AuthorizationException;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.bukkit.utils.logging.Logging;
import net.mctournaments.bukkit.utils.reflection.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements Listener {

    private static final Joiner argJoiner = Joiner.on(" ");

    private final Plugin plugin;
    private final Injector injector;
    private final ParametricBuilder builder;
    private final SimpleDispatcher dispatcher;

    @Inject
    public CommandManager(Plugin plugin, ProfileManager profileManager) {
        this.plugin = plugin;

        this.injector = Intake.createInjector();
        this.injector.install(new PrimitivesModule());
        this.injector.install(new BukkitModule(profileManager));

        this.builder = new ParametricBuilder(this.injector);
        this.builder.setAuthorizer(new AuthorizerAdapter());

        this.dispatcher = new SimpleDispatcher();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().equals(this.plugin)) {
            this.registerCommandsToBukkit();
        }
    }

    public void installModule(Module module) {
        this.injector.install(module);
    }

    public void registerCommands(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            Command definition = method.getAnnotation(Command.class);
            if (definition == null) {
                continue;
            }

            this.dispatcher.registerCommand(this.builder.build(object, method), definition.aliases());
        }
    }

    public void registerCommandsToBukkit() {
        for (CommandMapping command : this.dispatcher.getCommands()) {
            Logging.info("Registering " + command.getPrimaryAlias());
            CommandAdapter adapter = new CommandAdapter(command, this.plugin) {
                @Override public boolean execute(CommandSender sender, String alias, String[] args) {
                    Namespace namespace = new Namespace();
                    namespace.put(CommandSender.class, sender);
                    namespace.put(Plugin.class, CommandManager.this.plugin);

                    try {
                        command.getCallable().call(argJoiner.join(args), namespace, Lists.newArrayList(command.getPrimaryAlias()));
                    } catch (InvalidUsageException ex) {
                        sendCommandUsageHelp(ex, sender);
                    } catch (CommandException ex) {
                        send(sender, ERROR, ex.getMessage());
                    } catch (AuthorizationException ex) {
                        send(sender, ERROR, "You do not have permission for this command.");
                    } catch (InvocationCommandException ex) {
                        send(sender, ERROR, "A severe error occurred while running your command. The server log will have the error that can be "
                                + "reported.");
                        Logging.warning(ex, "Command execution failed");
                    }

                    return true;
                }

                @Override public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                    Namespace namespace = new Namespace();
                    namespace.put(CommandSender.class, sender);

                    String s = argJoiner.join(args);
                    try {
                        return command.getCallable().getSuggestions(s, namespace);
                    } catch (CommandException ex) {
                        Logging.severe(ex, "Failed to get suggestions for " + command.getPrimaryAlias() + " command with '" + s + "' arguments.");
                        return ImmutableList.of();
                    }
                }
            };

            this.getCommandMap().register(command.getPrimaryAlias(), this.plugin.getName(), adapter);
        }
    }

    private void sendCommandUsageHelp(InvalidUsageException ex, CommandSender sender) {
        String commandString = Joiner.on(" ").join(ex.getAliasStack());
        Description description = ex.getCommand().getDescription();

        if (ex.isFullHelpSuggested()) {
            if (ex.getCommand() instanceof Dispatcher) {
                send(sender, INFO_ALTERNATIVE, "Subcommands:");

                Dispatcher dispatcher = (Dispatcher) ex.getCommand();
                List<CommandMapping> list = new ArrayList<>(dispatcher.getCommands());

                for (CommandMapping mapping : list) {
                    send(sender, INFO_ALTERNATIVE, "/%s %s: %s",
                            commandString.isEmpty() ? "" : commandString + " ",
                            mapping.getPrimaryAlias(),
                            ": " + mapping.getDescription().getShortDescription());
                }
            } else {
                send(sender, INFO, "Help for %s:", commandString);

                if (description.getUsage() != null) {
                    send(sender, INFO, "Usage: '%s'", description.getUsage());
                } else {
                    send(sender, WARNING, "Usage information is not available.");
                }

                if (description.getHelp() != null) {
                    send(sender, WARNING, description.getHelp());
                } else if (description.getShortDescription() != null) {
                    send(sender, INFO, description.getShortDescription());
                } else {
                    send(sender, WARNING, "No further help is available.");
                }
            }

            String message = ex.getMessage();
            if (message != null) {
                send(sender, ERROR, message);
            }
        } else {
            String message = ex.getMessage();
            send(sender, ERROR, message != null ? message : "The command was not used properly (no more help available).");
            send(sender, INFO, "Usage: %s", description.getUsage());
        }
    }

    public CommandMap getCommandMap() {
        return checkNotNull(ReflectionUtils.getField(Bukkit.getServer().getPluginManager(), "commandMap"));
    }

}
