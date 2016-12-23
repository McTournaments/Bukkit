package net.mctournaments.bukkit.module;

import static com.google.common.base.Preconditions.checkState;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.harryfreeborough.modularity.Module;
import com.harryfreeborough.modularity.injector.AutoRegister;
import com.sk89q.intake.Command;
import net.mctournaments.bukkit.McTournaments;
import net.mctournaments.bukkit.command.CommandManager;
import net.mctournaments.bukkit.config.Config;
import net.mctournaments.bukkit.config.ConfigFactory;
import net.mctournaments.bukkit.config.InjectConfig;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.bukkit.utils.logging.Logging;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class McTournamentsGuiceModule extends AbstractModule {

    private final File dataDir;
    private final ConfigFactory configFactory;

    public McTournamentsGuiceModule(Plugin plugin) {
        this.dataDir = plugin.getDataFolder();
        this.configFactory = new ConfigFactory(this.dataDir);
    }

    @Override
    protected void configure() {
        bind(ProfileManager.class).toProvider(() -> McTournaments.get().getProfileManager());
        bind(ConfigFactory.class).toInstance(this.configFactory);

        bindListener(Matchers.any(), new CommandTypeListener());
        bindListener(Matchers.any(), new ConfigTypeListener());
    }

    private class CommandTypeListener implements TypeListener {

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            Class<?> clazz = type.getRawType();
            if (clazz.isAnnotationPresent(Module.class) || clazz.isAnnotationPresent(AutoRegister.class)) {
                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(Command.class)) {
                        CommandManager commandManager = encounter.getProvider(CommandManager.class).get();
                        encounter.register((InjectionListener<I>) commandManager::registerCommands);
                        break;
                    }
                }
            }
        }

    }

    private class ConfigTypeListener implements TypeListener {

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            Class<?> clazz = type.getRawType();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(InjectConfig.class)) {
                        checkState(field.getType() == Config.class, "InjectConfig type must be Config");
                        encounter.register(new ConfigMembersInjector<>(field.getAnnotation(InjectConfig.class), field));
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }

    }

    private class ConfigMembersInjector<T> implements MembersInjector<T> {

        private final InjectConfig annotation;
        private final Field field;

        public ConfigMembersInjector(InjectConfig annotation, Field field) {
            this.annotation = annotation;

            this.field = field;
            this.field.setAccessible(true);
        }

        @Override
        public void injectMembers(T instance) {
            try {
                ParameterizedType paramType = (ParameterizedType) this.field.getGenericType();
                this.field.set(instance, configFactory.createMapping(this.annotation.value(), (Class<?>) paramType.getActualTypeArguments()[0]));
            } catch (IllegalAccessException e) {
                Logging.warning(e, "Failed to set @InjectConfig");
            }
        }

    }

}
