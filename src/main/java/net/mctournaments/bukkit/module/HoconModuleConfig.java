package net.mctournaments.bukkit.module;

import com.google.common.base.Strings;
import com.harryfreeborough.modularity.config.ModuleConfig;
import net.mctournaments.bukkit.config.ConfigFactory;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.util.Map;

public class HoconModuleConfig implements ModuleConfig {

    private final ConfigFactory configFactory;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode config;
    private CommentedConfigurationNode enabledNode;
    private Map<?, ?> modulesMap;

    public HoconModuleConfig(ConfigFactory configFactory) {
        this.configFactory = configFactory;
    }

    @Override
    public void initialize() {
        this.loader = this.configFactory.createLoader("modules");
        try {
            this.config = this.loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.enabledNode = this.config.getNode("modules");
        this.enabledNode.setComment("List of modules to auto-load");

        this.modulesMap = this.enabledNode.getChildrenMap();
    }

    @Override
    public boolean isSet(String name) {
        return this.modulesMap.containsKey(name);
    }

    @Override
    public void setDefault(String name, String description, boolean defaultValue) {
        CommentedConfigurationNode moduleNode = this.enabledNode.getNode(name);
        moduleNode.setComment(Strings.emptyToNull(description));
        moduleNode.setValue(defaultValue);
    }

    @Override
    public boolean isEnabled(String name) {
        return this.enabledNode.getNode(name).getBoolean();
    }

    @Override
    public void close() {
        try {
            this.loader.save(this.config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
