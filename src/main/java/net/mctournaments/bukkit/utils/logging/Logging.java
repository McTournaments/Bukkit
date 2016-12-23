package net.mctournaments.bukkit.utils.logging;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This is a simple proxy to a plugin's {@link Logger} class.
 *
 * @see java.util.logging.Logger
 *
 * @author 1Rogue
 */
public final class Logging {

    private static final Logger LOG = Logger.getLogger(Logging.class.getSimpleName());
    private static Supplier<Logger> NAB = () -> LOG;

    private Logging() {
    }

    /**
     * Sets the system/global logging format to:
     * {@code [hour:minute:second] LOGLEVEL: output\n}
     */
    public static void setNicerFormat() {
        //See: https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
        Logging.setGlobalFormat("[%1$tH:%1$tM:%1$tS] %4$s: %5$s%n");
        Formatter f = new SimpleFormatter();
        Arrays.stream(Logging.getHandlers()).forEach(h -> h.setFormatter(f));
    }

    /**
     * Sets the system/global logging format to {@code format}
     *
     * @param format The format to use for any {@link java.util.logging.SimpleFormatter} loggers
     */
    public static void setGlobalFormat(String format) {
        System.setProperty("java.util.logging.SimpleFormatter.format", format);
    }

    /**
     * Returns the current {@link Logger} in use by the plugin calling this
     * method
     *
     * @return See original {@link Logger} implementationThe relevant {@link Logger} for the plugin that calls this method
     */
    public static Logger getLogger() {
        return Logging.nab();
    }


    /**
     * "Nab"s the appropriate plugin instance that called upon this class and
     * returns its {@link Logger}
     *
     * @return See original {@link Logger} implementationThe relevant {@link Logger} for a calling plugin
     */
    private static Logger nab() {
        return NAB.get();
    }

    public static void setNab(Supplier<Logger> nab) {
        NAB = nab;
    }

    //=======================================================
    // Start of manual proxying for convieniance methods
    //=======================================================

    /**
     * @see Logger#addHandler(Handler)
     * @param handler See original {@link Logger} implementation
     */
    public static void addHandler(Handler handler) throws SecurityException {
        Logging.nab().addHandler(handler);
    }

    /**
     * @see Logger#config(String)
     * @param msg See original {@link Logger} implementation
     */
    public static void config(String msg) {
        Logging.nab().config(msg);
    }

    /**
     * @see Logger#config(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void config(Supplier<String> msgSupplier) {
        Logging.nab().config(msgSupplier);
    }

    /**
     * @see Logger#entering(String, String)
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     */
    public static void entering(String sourceClass, String sourceMethod) {
        Logging.nab().entering(sourceClass, sourceMethod);
    }

    /**
     * @see Logger#entering(String, String, Object)
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param param1 See original {@link Logger} implementation
     */
    public static void entering(String sourceClass, String sourceMethod, Object param1) {
        Logging.nab().entering(sourceClass, sourceMethod, param1);
    }

    /**
     * @see Logger#entering(String, String, Object[])
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param params See original {@link Logger} implementation
     */
    public static void entering(String sourceClass, String sourceMethod, Object[] params) {
        Logging.nab().entering(sourceClass, sourceMethod, params);
    }

    /**
     * @see Logger#exiting(String, String)
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     */
    public static void exiting(String sourceClass, String sourceMethod) {
        Logging.nab().exiting(sourceClass, sourceMethod);
    }

    /**
     * @see Logger#exiting(String, String, Object)
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param result See original {@link Logger} implementation
     */
    public static void exiting(String sourceClass, String sourceMethod, Object result) {
        Logging.nab().exiting(sourceClass, sourceMethod, result);
    }

    /**
     * @see Logger#fine(String)
     * @param msg See original {@link Logger} implementation
     */
    public static void fine(String msg) {
        Logging.nab().fine(msg);
    }

    /**
     * @see Logger#fine(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void fine(Supplier<String> msgSupplier) {
        Logging.nab().fine(msgSupplier);
    }

    /**
     * @see Logger#finer(String)
     * @param msg See original {@link Logger} implementation
     */
    public static void finer(String msg) {
        Logging.nab().finer(msg);
    }

    /**
     * @see Logger#finer(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void finer(Supplier<String> msgSupplier) {
        Logging.nab().finer(msgSupplier);
    }

    /**
     * @see Logger#finest(String)
     * @param msg See original {@link Logger} implementation
     */
    public static void finest(String msg) {
        Logging.nab().finest(msg);
    }

    /**
     * @see Logger#finest(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void finest(Supplier<String> msgSupplier) {
        Logging.nab().finest(msgSupplier);
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getFilter()
     */
    public static Filter getFilter() {
        return Logging.nab().getFilter();
    }

    /**
     * @see Logger#setFilter(Filter)
     * @param newFilter See original {@link Logger} implementation
     */
    public static void setFilter(Filter newFilter) throws SecurityException {
        Logging.nab().setFilter(newFilter);
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getHandlers()
     */
    public static Handler[] getHandlers() {
        return Logging.nab().getHandlers();
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getLevel()
     */
    public static Level getLevel() {
        return Logging.nab().getLevel();
    }

    /**
     * @see Logger#setLevel(Level)
     * @param newLevel See original {@link Logger} implementation
     */
    public static void setLevel(Level newLevel) throws SecurityException {
        Logging.nab().setLevel(newLevel);
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getName()
     */
    public static String getName() {
        return Logging.nab().getName();
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getParent()
     */
    public static Logger getParent() {
        return Logging.nab().getParent();
    }

    /**
     * @see Logger#setParent(Logger)
     * @param parent See original {@link Logger} implementation
     */
    public static void setParent(Logger parent) {
        Logging.nab().setParent(parent);
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getResourceBundle()
     */
    public static ResourceBundle getResourceBundle() {
        return Logging.nab().getResourceBundle();
    }

    /**
     * @see Logger#setResourceBundle(ResourceBundle)
     * @param bundle See original {@link Logger} implementation
     */
    public static void setResourceBundle(ResourceBundle bundle) {
        Logging.nab().setResourceBundle(bundle);
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getResourceBundleName()
     */
    public static String getResourceBundleName() {
        return Logging.nab().getResourceBundleName();
    }

    /**
     * @return See original {@link Logger} implementation
     * @see Logger#getUseParentHandlers()
     */
    public static boolean getUseParentHandlers() {
        return Logging.nab().getUseParentHandlers();
    }

    /**
     * @see Logger#setUseParentHandlers(boolean)
     * @param useParentHandlers See original {@link Logger} implementation
     */
    public static void setUseParentHandlers(boolean useParentHandlers) {
        Logging.nab().setUseParentHandlers(useParentHandlers);
    }

    /**
     * Formats input with {@link String#format(String, Object...)} and passes to the
     * original {@link Logger#info(String)} implementation
     *
     * @param msg The string message
     * @param args Arguments to be used for formatting the msg
     */
    public static void info(String msg, Object... args) {
        Logging.nab().info(String.format(msg, args));
    }

    /**
     * @see Logger#info(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void info(Supplier<String> msgSupplier) {
        Logging.nab().info(msgSupplier);
    }

    /**
     * @param level See original {@link Logger} implementation
     * @return See original {@link Logger} implementation
     * @see Logger#isLoggable(Level)
     */
    public static boolean isLoggable(Level level) {
        return Logging.nab().isLoggable(level);
    }

    /**
     * @see Logger#log(LogRecord)
     * @param record See original {@link Logger} implementation
     */
    public static void log(LogRecord record) {
        Logging.nab().log(record);
    }

    /**
     * @see Logger#log(Level, String)
     * @param level See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     */
    public static void log(Level level, String msg) {
        Logging.nab().log(level, msg);
    }

    /**
     * @see Logger#log(Level, Supplier)
     * @param level See original {@link Logger} implementation
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void log(Level level, Supplier<String> msgSupplier) {
        Logging.nab().log(level, msgSupplier);
    }

    /**
     * @see Logger#log(Level, String, Object)
     * @param level See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param param1 See original {@link Logger} implementation
     */
    public static void log(Level level, String msg, Object param1) {
        Logging.nab().log(level, msg, param1);
    }

    /**
     * @see Logger#log(Level, String, Object[])
     * @param level See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param params See original {@link Logger} implementation
     */
    public static void log(Level level, String msg, Object[] params) {
        Logging.nab().log(level, msg, params);
    }

    /**
     * @see Logger#log(Level, String, Throwable)
     * @param level See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     */
    public static void log(Level level, String msg, Throwable thrown) {
        Logging.nab().log(level, msg, thrown);
    }

    /**
     * @see Logger#log(Level, Throwable, Supplier)
     * @param level See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void log(Level level, Throwable thrown, Supplier<String> msgSupplier) {
        Logging.nab().log(level, thrown, msgSupplier);
    }

    /**
     * @see Logger#logp(Level, String, String, String)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, String msg) {
        Logging.nab().logp(level, sourceClass, sourceMethod, msg);
    }

    /**
     * @see Logger#logp(Level, String, String, Supplier)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, Supplier<String> msgSupplier) {
        Logging.nab().logp(level, sourceClass, sourceMethod, msgSupplier);
    }

    /**
     * @see Logger#logp(Level, String, String, String, Object)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param param1 See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
        Logging.nab().logp(level, sourceClass, sourceMethod, msg, param1);
    }

    /**
     * @see Logger#logp(Level, String, String, String, Object[])
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param params See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
        Logging.nab().logp(level, sourceClass, sourceMethod, msg, params);
    }

    /**
     * @see Logger#logp(Level, String, String, String, Throwable)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
        Logging.nab().logp(level, sourceClass, sourceMethod, msg, thrown);
    }

    /**
     * @see Logger#logp(Level, String, String, Throwable, Supplier)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void logp(Level level, String sourceClass, String sourceMethod, Throwable thrown, Supplier<String> msgSupplier) {
        Logging.nab().logp(level, sourceClass, sourceMethod, thrown, msgSupplier);
    }

    /**
     * @see Logger#logrb(Level, String, String, String, String)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundleName See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundleName, msg);
    }

    /**
     * @see Logger#logrb(Level, String, String, ResourceBundle, String, Object...)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundle See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param params See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Object... params) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundle, msg, params);
    }

    /**
     * @see Logger#logrb(Level, String, String, ResourceBundle, String, Throwable)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundle See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, ResourceBundle bundle, String msg, Throwable thrown) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundle, msg, thrown);
    }

    /**
     * @see Logger#logrb(Level, String, String, String, String, Object)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundleName See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param param1 See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
    }

    /**
     * @see Logger#logrb(Level, String, String, String, String, Object[])
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundleName See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param params See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
    }

    /**
     * @see Logger#logrb(Level, String, String, String, String, Throwable)
     * @param level See original {@link Logger} implementation
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param bundleName See original {@link Logger} implementation
     * @param msg See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     */
    public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
        Logging.nab().logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
    }

    /**
     * @see Logger#removeHandler(Handler)
     * @param handler See original {@link Logger} implementation
     */
    public static void removeHandler(Handler handler) throws SecurityException {
        Logging.nab().removeHandler(handler);
    }

    /**
     * @see Logger#severe(String)
     * @param msg See original {@link Logger} implementation
     */
    public static void severe(String msg) {
        Logging.nab().severe(msg);
    }

    /**
     * @see Logger#severe(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void severe(Supplier<String> msgSupplier) {
        Logging.nab().severe(msgSupplier);
    }

    /**
     * @see Logger#throwing(String, String, Throwable)
     * @param sourceClass See original {@link Logger} implementation
     * @param sourceMethod See original {@link Logger} implementation
     * @param thrown See original {@link Logger} implementation
     */
    public static void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        Logging.nab().throwing(sourceClass, sourceMethod, thrown);
    }

    /**
     * Formats input with {@link String#format(String, Object...)} and passes to the
     * original {@link Logger#warning(String)} (String)} implementation
     *
     * @param msg The string message
     * @param args Arguments to be used for formatting the msg
     */
    public static void warning(String msg, Object... args) {
        Logging.nab().warning(String.format(msg, args));
    }

    /**
     * @see Logger#warning(Supplier)
     * @param msgSupplier See original {@link Logger} implementation
     */
    public static void warning(Supplier<String> msgSupplier) {
        Logging.nab().warning(msgSupplier);
    }

    /**
     * Logs an exception to the held {@link Logger} at {@link Level#SEVERE}
     *
     * @param ex The {@link Throwable} to print
     * @param format The formatting string
     * @param args The formatting arguments
     */
    public static void severe(Throwable ex, String format, Object... args) {
        Logging.nab().log(Level.SEVERE, String.format(format, args), ex);
    }

    /**
     * Logs an exception to the held {@link Logger} at {@link Level#WARNING}
     *
     * @param ex The {@link Throwable} to print
     * @param format The formatting string
     * @param args The formatting arguments
     */
    public static void warning(Throwable ex, String format, Object... args) {
        Logging.nab().log(Level.WARNING, String.format(format, args), ex);
    }


}