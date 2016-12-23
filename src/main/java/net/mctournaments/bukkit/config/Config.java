package net.mctournaments.bukkit.config;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import net.mctournaments.bukkit.utils.logging.Logging;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;

public class Config<T> {

    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private final ObjectMapper<T>.BoundInstance configMapper;
    private ConfigurationNode root;
    private T object;
    private boolean loadAttempted;

    public Config(ConfigurationLoader<CommentedConfigurationNode> loader, Class<T> type) throws ConfigLoadException {
        try {
            this.loader = checkNotNull(loader, "loader");
            this.configMapper = ObjectMapper.forClass(type).bindToNew();
            this.root = SimpleCommentedConfigurationNode.root();
            this.object = configMapper.populate(root);
        } catch (ObjectMappingException e) {
            throw new ConfigLoadException(e);
        }
    }

    public boolean load() {
        try {
            this.root = this.loader.load();
            this.object = this.configMapper.populate(this.root);
            return true;
        } catch (IOException | ObjectMappingException e) {
            Logging.severe(e, "Failed to load the configuration file.");
            return false;
        } finally {
            this.loadAttempted = true;
        }
    }

    public boolean save() {
        checkState(this.loadAttempted, "Config#load() must be called before Config#save().");
        try {
            this.configMapper.serialize(this.root);
            this.loader.save(this.root);
            return true;
        } catch (ObjectMappingException | IOException e) {
            Logging.warning(e, "Failed to save the configuration file.");
            return false;
        }
    }

    public T get() {
        return this.object;
    }

}
