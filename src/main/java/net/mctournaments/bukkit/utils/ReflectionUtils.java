package net.mctournaments.bukkit.utils;

import java.lang.reflect.Field;

/**
 * Utility methods for reflection.
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        } while (checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));

        return null;
    }

}