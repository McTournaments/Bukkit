package net.mctournaments.bukkit;

import com.google.common.reflect.TypeToken;
import com.harryfreeborough.modularity.additionalmodules.BukkitGuiceModule;
import com.harryfreeborough.modularity.loader.ModuleLoader;
import net.mctournaments.bukkit.config.ConfigFactory;
import net.mctournaments.bukkit.config.SetTypeSerializer;
import net.mctournaments.bukkit.events.lifecycle.InitializationEvent;
import net.mctournaments.bukkit.events.lifecycle.PreInitializationEvent;
import net.mctournaments.bukkit.menu.MenuListener;
import net.mctournaments.bukkit.module.CoreModule;
import net.mctournaments.bukkit.module.HoconModuleConfig;
import net.mctournaments.bukkit.module.McTournamentsGuiceModule;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.bukkit.profile.permissions.MongoRankDataDao;
import net.mctournaments.bukkit.profile.permissions.PermissionsManager;
import net.mctournaments.bukkit.profile.storage.MongoProfileStorageDao;
import net.mctournaments.bukkit.utils.logging.Debugger;
import net.mctournaments.bukkit.utils.logging.Logging;
import net.mctournaments.bukkit.utils.logging.PluginDebugOpts;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Set;

public final class McTournaments extends JavaPlugin {

    private ProfileManager profileManager;
    private PermissionsManager permissionsManager;

    public static McTournaments get() {
        return JavaPlugin.getPlugin(McTournaments.class);
    }

    @Override
    public void onEnable() {
        this.setupConfigSerializers();
        this.setupLogging();
        this.setupProfileAndPermissionManager();
        this.setupInjector();
        this.callEvents();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        this.permissionsManager.getDao().close();
        this.profileManager.getStorageDao().close();
    }

    private void setupConfigSerializers() {
        TypeSerializers.getDefaultSerializers().registerType(new TypeToken<Set<?>>() {
        }, new SetTypeSerializer());
    }

    private void setupLogging() {
        Logging.setNab(this::getLogger);

        PluginDebugOpts opts = new PluginDebugOpts(this.getLogger());
        opts.toggleOutput(true);
        Debugger.DebugUtil.setOps(() -> opts);
        PluginDebugOpts.hookBukkit();
    }

    private void setupProfileAndPermissionManager() {
        ConfigFactory configFactory = new ConfigFactory(this.getDataFolder());
        ConfigurationLoader<CommentedConfigurationNode> loader = configFactory.createLoader("db");
        CommentedConfigurationNode node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            Logging.severe(e, "Failed to load db config.");
        }

        String ip = node.getNode("ip").getString("127.0.0.1");
        int port = node.getNode("port").getInt(27017);
        String db = node.getNode("db").getString("dev");

        this.profileManager = new ProfileManager(this,
                new MongoProfileStorageDao(ip, port, db)
        );

        this.profileManager.getStorageDao().initialize();

        this.permissionsManager = new PermissionsManager(this,
                new MongoRankDataDao(ip, port, db)
        );

        this.permissionsManager.getDao().initialize();
        this.permissionsManager.loadRankData();
    }

    private void setupInjector() {
        new ModuleLoader()
                .addInjectorModules(
                        new BukkitGuiceModule(this),
                        new McTournamentsGuiceModule(this),
                        new CoreModule(this.permissionsManager))
                .setConfig(new HoconModuleConfig(new ConfigFactory(this.getDataFolder())))
                .load();
    }

    private void callEvents() {
        this.getServer().getPluginManager().callEvent(new PreInitializationEvent());
        this.getServer().getPluginManager().callEvent(new InitializationEvent());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

}
