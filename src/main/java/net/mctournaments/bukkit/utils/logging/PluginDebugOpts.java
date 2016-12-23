package net.mctournaments.bukkit.utils.logging;

import net.mctournaments.bukkit.McTournaments;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author 1Rogue
 */
public class PluginDebugOpts extends Debugger.DebugOpts {

    /**
     * Constructor. Determines the logging prefix and initializes fields
     *
     * @param logger The {@link Logger} relevant to this instance
     */
    public PluginDebugOpts(Logger logger) {
        super();
        this.logger = logger;

        //This ends up with double plugin prefix, one from default plugin logger's prefix, one set berehere
        /*this.prefix = plugin.getDescription().getPrefix() == null
                ? plugin.getName()
                : plugin.getDescription().getPrefix();*/

        this.output = false;
        this.url = null;
    }

    /**
     * Hooks into Bukkit's plugin system and adds a handler to all plugin
     * loggers to allow catching unreported exceptions. If already hooked, will
     * do nothing. This method will continue to hook new plugins via a listener
     */
    public static void hookBukkit() {
        Listener l = new BukkitPluginListener();
        if (HandlerList.getRegisteredListeners(McTournaments.get()).stream().noneMatch(r -> r.getListener().getClass() == l.getClass())) {
            Bukkit.getServer().getPluginManager().registerEvents(l, McTournaments.get());
        }
        //Hook any current plugins without a handler
        Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).forEach(ExceptionHandler::apply);
    }

    @Override
    public JSONObject attachInfo() {
        JSONObject back = super.attachInfo();
        back.put("project-type", "bukkit-plugin");
        JSONObject plugin = new JSONObject();
        plugin.put("core-version", McTournaments.get().getDescription().getVersion());
        back.put("plugin", plugin);
        JSONObject server = new JSONObject();
        Server s = Bukkit.getServer();
        server.put("allow-end", s.getAllowEnd());
        server.put("allow-flight", s.getAllowFlight());
        server.put("allow-nether", s.getAllowNether());
        server.put("ambient-spawn-limit", s.getAmbientSpawnLimit());
        server.put("animal-spawn-limit", s.getAnimalSpawnLimit());
        server.put("binding-address", s.getIp());
        server.put("bukkit-version", s.getBukkitVersion());
        server.put("connection-throttle", s.getConnectionThrottle());
        server.put("default-game-mode", s.getDefaultGameMode().name());
        server.put("default-world-type", s.getWorldType());
        server.put("generate-structures", s.getGenerateStructures());
        server.put("idle-timeout", s.getIdleTimeout());
        server.put("players-online", s.getOnlinePlayers().size());
        server.put("max-players", s.getMaxPlayers());
        server.put("monster-spawn-limit", s.getMonsterSpawnLimit());
        server.put("motd", s.getMotd());
        server.put("name", s.getName());
        server.put("online-mode", s.getOnlineMode());
        server.put("port", s.getPort());
        server.put("server-id", s.getServerId());
        server.put("server-name", s.getServerName());
        server.put("spawn-radius", s.getSpawnRadius());
        server.put("ticks-per-animal-spawns", s.getTicksPerAnimalSpawns());
        server.put("ticks-per-monster-spawns", s.getTicksPerMonsterSpawns());
        server.put("version", s.getVersion());
        server.put("view-distance", s.getViewDistance());
        server.put("warning-state", s.getWarningState());
        server.put("water-animal-spawn-limit", s.getWaterAnimalSpawnLimit());
        back.put("server", server);
        return back;
    }

    /**
     * A {@link Listener} for adding an {@link ExceptionHandler} to a
     * {@link Plugin}'s {@link Logger} upon it being enabled
     *
     * @author 1Rogue
     */
    private final static class BukkitPluginListener implements Listener {

        /**
         * Appends an {@link ExceptionHandler} to a {@link Plugin}'s
         * {@link Logger}
         *
         * @param event The relevant {@link PluginEnableEvent} from Bukkit
         */
        @EventHandler
        public void onEnable(PluginEnableEvent event) {
            ExceptionHandler.apply(event.getPlugin());
        }

    }

    /**
     * Attachable {@link Handler} used to catch any exceptions that are logged
     * directly to a plugin's {@link Logger}
     *
     * @author 1Rogue
     */
    public static class ExceptionHandler extends Handler {

        /**
         * Constructor. Sets the plugin to a field and sets the filter for this
         * {@link Handler} to {@link Level#SEVERE}
         **/
        public ExceptionHandler() {
            super.setFilter((LogRecord record) -> record.getLevel() == Level.SEVERE);
        }

        /**
         * Applies a new {@link Handler} to the passed {@link Plugin}'s
         * {@link Logger} if it is not already attached to it
         *
         * @param p The {@link Plugin} with the {@link Logger} to check
         */
        public static void apply(Plugin p) {
            for (Handler h : p.getLogger().getHandlers()) {
                if (h instanceof ExceptionHandler) {
                    return;
                }
            }
            p.getLogger().addHandler(new ExceptionHandler());
        }

        /**
         * If {@link LogRecord#getThrown()} does not return {@code null}, then this will call
         * {@link Debugger.DebugUtil#report(Debugger.DebugOpts, Throwable, String)}
         * <br><br> {@inheritDoc}
         *
         * @param record {@inheritDoc}
         */
        @Override
        public void publish(LogRecord record) {
            if (record.getThrown() != null) {
                //Report exception
                Debugger.DebugUtil.report(Debugger.DebugUtil.getOpts(), record.getThrown(), record.getMessage());
            }
        }

        /**
         * Does nothing
         */
        @Override
        public void flush() {
        } //not buffered

        /**
         * Does nothing
         *
         * @throws SecurityException Never happens
         */
        @Override
        public void close() throws SecurityException {
        } //nothing to close

    }

}