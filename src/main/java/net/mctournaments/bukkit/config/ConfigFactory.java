package net.mctournaments.bukkit.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.harryfreeborough.modularity.injector.AutoRegister;
import net.mctournaments.bukkit.events.lifecycle.PreInitializationEvent;
import net.mctournaments.bukkit.events.lifecycle.ReloadConfigEvent;
import net.mctournaments.bukkit.utils.logging.Logging;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;

@AutoRegister
public class ConfigFactory implements Listener {

    private final LoadingCache<Key, Config<?>> configs;

    private final File dir;

    @Inject
    public ConfigFactory(@DataDir File dir) {
        this.dir = checkNotNull(dir, "dir");

        this.configs = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .build(new CacheLoader<Key, Config<?>>() {
                    @Override
                    public Config<?> load(Key key) throws Exception {
                        File file = new File(dir, key.name + ".conf");
                        file.getAbsoluteFile().getParentFile().mkdirs();
                        return new Config<>(createLoader(file), key.type);
                    }
                });
    }

    public File getDir() {
        return this.dir;
    }

    public ConfigurationLoader<CommentedConfigurationNode> createLoader(File file) {
        return HoconConfigurationLoader.builder().setFile(file).build();
    }

    public ConfigurationLoader<CommentedConfigurationNode> createLoader(String name) {
        File file = new File(this.dir, name + ".conf");
        file.getAbsoluteFile().getParentFile().mkdirs();
        return this.createLoader(file);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInitialization(PreInitializationEvent event) {
        this.loadConfigs();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInitializationLate(PreInitializationEvent event) {
        this.saveConfigs();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onReloadConfig(ReloadConfigEvent event) {
        this.loadConfigs();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onReloadConfigLate(ReloadConfigEvent event) {
        this.saveConfigs();
    }

    private void loadConfigs() {
        this.configs.asMap().entrySet().forEach(entry -> {
            Logging.info("Loading " + entry.getKey().getName());
            entry.getValue().load();
        });
    }

    private void saveConfigs() {
        this.configs.asMap().entrySet().forEach(entry -> {
            Logging.info("Saving " + entry.getKey().getName());
            entry.getValue().save();
        });
    }

    public <T> Config<T> createMapping(String name, Class<T> type) {
        return (Config<T>) this.configs.getUnchecked(new Key(name, type));
    }

    private static class Key {

        private final String name;
        private final Class<?> type;

        public Key(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public Class<?> getType() {
            return this.type;
        }

    }

}
