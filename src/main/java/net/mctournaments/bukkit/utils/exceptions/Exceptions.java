package net.mctournaments.bukkit.utils.exceptions;

/**
 * A utility class for throwing conditional exceptions, similar to Apache's
 * {@link org.apache.commons.lang3.Validate Validate} class.
 *
 * @author 1Rogue
 */
public final class Exceptions {

    private Exceptions() {
    }

    /**
     * Creates a readable stack trace from a passed {@link Throwable}. This
     * method will reproduce the same output that
     * {@link Throwable#printStackTrace()} would output
     *
     * @param t The {@link Throwable} to make readable
     * @return A string representing the entire stack trace
     */
    public static String readableStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] trace = t.getStackTrace();
        for (StackTraceElement elem : trace) {
            sb.append("\tat ").append(elem).append('\n');
        }
        if (t.getCause() != null) {
            Exceptions.readableStackTraceAsCause(sb, t.getCause(), trace);
        }
        return sb.toString();
    }

    /**
     * Recursive method for appending {@link Throwable] causes that are appended
     * to a {@link Throwable}
     *
     * @param sb The {@link StringBuilder} being appended to
     * @param t The {@link Throwable} root
     * @param causedTrace An array of already visited stack nodes
     */
    private static void readableStackTraceAsCause(StringBuilder sb, Throwable t, StackTraceElement[] causedTrace) {
        // Compute number of frames in common between previous and caused
        StackTraceElement[] trace = t.getStackTrace();
        int m = trace.length - 1;
        int n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int common = trace.length - 1 - m;

        sb.append("Caused by: ").append(t).append('\n');
        for (int i = 0; i <= m; i++) {
            sb.append("\tat ").append(trace[i]).append('\n');
        }
        if (common != 0) {
            sb.append("\t... ").append(common).append(" more\n");
        }
        if (t.getCause() != null) {
            Exceptions.readableStackTraceAsCause(sb, t.getCause(), trace);
        }
    }

}
