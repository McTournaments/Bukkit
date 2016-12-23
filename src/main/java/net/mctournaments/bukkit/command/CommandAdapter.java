package net.mctournaments.bukkit.command;

import com.sk89q.intake.CommandMapping;
import org.bukkit.command.Command;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

abstract class CommandAdapter extends Command implements PluginIdentifiableCommand {

    private final CommandMapping command;
    private final Plugin plugin;

    public CommandAdapter(CommandMapping command, Plugin plugin) {
        super(command.getPrimaryAlias(), command.getDescription().getShortDescription(),
                "/" + command.getPrimaryAlias() + " " + command.getDescription().getUsage(), Arrays.asList(command.getAllAliases()));

        this.command = command;
        this.plugin = plugin;
    }

    @Override public Plugin getPlugin() {
        return this.plugin;
    }

}
