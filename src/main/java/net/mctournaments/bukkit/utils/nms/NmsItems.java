package net.mctournaments.bukkit.utils.nms;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import net.minecraft.server.v1_11_R1.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_11_R1.util.CraftMagicNumbers;

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
        org.bukkit.inventory.ItemStack item = CraftItemStack.asCraftCopy(itemStack);

        checkState(item.getType() != Material.AIR, "Failed to convert the Bukkit ItemStack to the CraftBukkit equivalent.");
        return item;
    }

    public static boolean isValidItemType(Material material) {
        return CraftMagicNumbers.getItem(material.getId()) != null;
    }

    public static ItemStack craftToNms(org.bukkit.inventory.ItemStack itemStack) {
        try {
            return checkNotNull((ItemStack) craftHandle.get(itemStack), "handle");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
