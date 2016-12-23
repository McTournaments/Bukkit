package net.mctournaments.bukkit.command;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.AbstractModule;
import com.sk89q.intake.parametric.Key;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import net.mctournaments.bukkit.data.MessageType;
import net.mctournaments.bukkit.data.Messages;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.common.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

class BukkitModule extends AbstractModule {

    private final ProfileManager profileManager;
    private final NamespaceProvider<CommandSender> senderProvider;

    public BukkitModule(ProfileManager profileManager) {
        this.profileManager = profileManager;

        this.senderProvider = new NamespaceProvider<>(CommandSender.class);
    }

    public static Player findBestPlayer(String test) throws ArgumentParseException {
        test = test.toLowerCase();

        List<Player> candidates = new ArrayList<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String name = player.getName().toLowerCase();
            if (name.equalsIgnoreCase(test)) {
                return player;
            } else if (name.startsWith(test)) {
                candidates.add(player);
            }
        }

        if (candidates.isEmpty()) {
            throw new ArgumentParseException(Messages.format(MessageType.ERROR, "%s did not match any players", test));
        } else if (candidates.size() == 1) {
            return candidates.get(0);
        } else {
            throw new ArgumentParseException(Messages.format(MessageType.WARNING, "Did you mean: " + Joiner.on(",").join(candidates)));
        }
    }

    @Override
    protected void configure() {
        this.bind(CommandSender.class).annotatedWith(Sender.class).toProvider(this.senderProvider);
        this.bind(Player.class).annotatedWith(Sender.class).toProvider(new PlayerSenderProvider<>());
        this.bind(Profile.class).annotatedWith(Sender.class).toProvider(new ProfileSenderProvider<>());
        this.bind(Player.class).toProvider(new PlayerProvider<>());
        this.bind(Profile.class).toProvider(new ProfileProvider<>());
        this.bind(Key.<Set<Player>>get(new TypeToken<Set<Player>>() {
        }.getType())).toProvider(new PlayerSetProvider<>());
        this.bind(Key.<Set<Profile>>get(new TypeToken<Set<Profile>>() {
        }.getType())).toProvider(new ProfileSetProvider<>());
    }

    private class PlayerSenderProvider<T extends Player> implements Provider<T> {

        @Override
        public boolean isProvided() {
            return false;
        }

        @Nullable
        @Override
        public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
            CommandSender sender = senderProvider.get(arguments, modifiers);
            if (sender instanceof Player) {
                return (T) sender;
            } else {
                throw new ArgumentParseException("Only players can use this command.");
            }
        }

        @Override
        public List<String> getSuggestions(String prefix) {
            return ImmutableList.of();
        }
    }

    private class ProfileSenderProvider<T extends Profile> implements Provider<T> {

        @Override
        public boolean isProvided() {
            return false;
        }

        @Nullable
        @Override
        public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
            CommandSender sender = senderProvider.get(arguments, modifiers);
            if (sender instanceof Player) {
                return (T) profileManager.getThrowableProfile((Player) sender);
            } else {
                throw new ArgumentParseException("Only players can use this command.");
            }
        }

        @Override
        public List<String> getSuggestions(String prefix) {
            return ImmutableList.of();
        }
    }

    private class PlayerProvider<T extends Player> implements Provider<T> {

        @Override
        public boolean isProvided() {
            return false;
        }

        @Nullable
        @Override
        public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
            return (T) findBestPlayer(arguments.next());
        }

        @Override
        public List<String> getSuggestions(String prefix) {
            return ImmutableList.of();
        }
    }

    private class ProfileProvider<T extends Profile> implements Provider<T> {

        @Override
        public boolean isProvided() {
            return false;
        }

        @Nullable
        @Override
        public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
            return (T) profileManager.getThrowableProfile(findBestPlayer(arguments.next()));
        }

        @Override
        public List<String> getSuggestions(String prefix) {
            return ImmutableList.of();
        }
    }

    private abstract class SetAdapter<T> implements Provider<Set<T>> {

        protected abstract Collection<? extends T> getAll() throws ArgumentParseException;

        protected abstract T find(String token) throws ArgumentParseException;

        @Nullable
        @Override
        public Set<T> get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
            Set<T> entries = Sets.newHashSet();
            String input;

            do {
                input = arguments.next();
                String[] tokens = input.split(",");
                for (String token : tokens) {
                    token = token.trim();
                    if (token.equals("*")) {
                        entries.addAll(getAll());
                    } else {
                        entries.add(find(token));
                    }
                }
            } while (!input.isEmpty() && input.charAt(input.length() - 1) == ',');

            return entries;
        }

        @Override
        public List<String> getSuggestions(String prefix) {
            return ImmutableList.of();
        }
    }

    private class PlayerSetProvider<T extends Player> extends SetAdapter<T> {

        @Override
        protected Collection<? extends T> getAll() {
            return (Collection<? extends T>) Bukkit.getServer().getOnlinePlayers();
        }

        @Override
        protected T find(String token) throws ArgumentParseException {
            return (T) findBestPlayer(token);
        }

        @Override
        public boolean isProvided() {
            return false;
        }
    }

    private class ProfileSetProvider<T extends Profile> extends SetAdapter<T> {

        @Override
        protected Collection<? extends T> getAll() {
            return (Collection<? extends T>) Bukkit.getServer().getOnlinePlayers()
                    .stream().map(profileManager::getThrowableProfile);
        }

        @Override
        protected T find(String token) throws ArgumentParseException {
            return (T) profileManager.getThrowableProfile(findBestPlayer(token));
        }

        @Override
        public boolean isProvided() {
            return false;
        }
    }

}
