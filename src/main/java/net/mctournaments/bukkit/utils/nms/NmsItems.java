package net.mctournaments.bukkit.utils.nms;

import net.minecraft.server.v1_11_R1.ItemStack;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

import java.lang.reflect.Field;

public class NmsItems {

    private static Field craftHandle = null;

    static {
        try {
            craftHandle = CraftItemStack.class.getDeclaredField("handle");
            craftHandle.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private NmsItems() {
    }

    public static org.bukkit.inventory.ItemStack nmsToCraft(ItemStack itemStack) {
        return CraftItemStack.asCraftMirror(itemStack);
    }

    public static org.bukkit.inventory.ItemStack bukkitToCraft(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asCraftCopy(itemStack);
    }

    public static ItemStack craftToNms(org.bukkit.inventory.ItemStack itemStack) {
        try {
            return (ItemStack) craftHandle.get(itemStack);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
