package net.mctournaments.bukkit.utils.nms;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import net.minecraft.server.v1_11_R1.NBTBase;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NbtTags {

    private static Field compoundMap = null;

    static {
        try {
            compoundMap = NBTTagCompound.class.getDeclaredField("map");
            compoundMap.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private NBTTagCompound compound;

    private NbtTags(NBTTagCompound compound) {
        this.compound = compound;
    }

    /**
     * Creates a blank set of nbt data.
     *
     * @return Blank {@link NbtTags} instance
     */
    public static NbtTags create() {
        return new NbtTags(new NBTTagCompound());
    }

    /**
     * Creates a new {@link NbtTags} object.
     * Varies from {@link NbtTags#craftCreate(ItemStack)} (ItemStack)} as any values that are set
     * in the returned {@link NbtTags} instance, are not reflected onto the
     * supplied {@link ItemStack}'s nbt data.
     *
     * @param base Base {@link ItemStack} to copy data from
     * @return {@link NbtTags} instance based on the supplied {@link ItemStack}
     */
    public static NbtTags create(ItemStack base) {
        return craftCreate(NmsItems.bukkitToCraft(base));
    }

    /**
     * Creates a new {@link NbtTags} object.
     * Varies from {@link NbtTags#create(ItemStack)} as any values that are set
     * in the returned {@link NbtTags} instance, are reflected onto the
     * supplied {@link ItemStack}'s nbt data.
     *
     * @param base Base {@link ItemStack}, must be an instance of {@link CraftItemStack}.
     * @return {@link NbtTags} instance based on the supplied {@link ItemStack}
     */
    public static NbtTags craftCreate(ItemStack base) {
        checkNotNull(base, "base craftCreate input");
        checkArgument(base instanceof CraftItemStack, "craftCreate must be used with a CraftItemStack");
        net.minecraft.server.v1_11_R1.ItemStack nmsItem = NmsItems.craftToNms(base);
        checkNotNull(nmsItem, "nmsItem");

        NBTTagCompound compound = nmsItem.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsItem.setTag(compound);
        }

        return new NbtTags(compound);
    }

    private static Map<String, NBTBase> getCompoundMap(NBTTagCompound compound) {
        try {
            return (Map<String, NBTBase>) compoundMap.get(compound);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    private static String toString(NBTTagCompound compound) {
        StringBuilder builder = new StringBuilder("{");

        for (Iterator<Map.Entry<String, NBTBase>> iter = getCompoundMap(compound).entrySet().iterator();
             iter.hasNext(); ) {
            Map.Entry<String, NBTBase> entry = iter.next();
            if (builder.length() != 1) {
                builder.append(',');
            }

            builder.append(entry.getKey()).append("(").append(entry.getValue().getTypeId()).append(")").append(':');
            if (entry.getValue() instanceof NBTTagCompound) {
                builder.append(toString((NBTTagCompound) entry.getValue()));
            } else {
                builder.append(entry.getValue().toString());
            }
        }

        return builder.append('}').toString();
    }

    public boolean hasKey(String key) {
        return this.compound.hasKey(key);
    }

    public byte getByte(String key) {
        return this.compound.getByte(key);
    }

    public short getShort(String key) {
        return this.compound.getShort(key);
    }

    public int getInt(String key) {
        return this.compound.getInt(key);
    }

    public long getLong(String key) {
        return this.compound.getLong(key);
    }

    public float getFloat(String key) {
        return this.compound.getFloat(key);
    }

    public double getDouble(String key) {
        return this.compound.getDouble(key);
    }

    public String getString(String key) {
        return this.compound.getString(key);
    }

    public boolean getBoolean(String key) {
        return this.compound.getBoolean(key);
    }

    public byte[] getByteArray(String key) {
        return this.compound.getByteArray(key);
    }

    public int[] getIntArray(String key) {
        return this.compound.getIntArray(key);
    }

    public NbtTags getCompound(String key) {
        return new NbtTags(this.compound.getCompound(key));
    }

    public NbtTags setByte(String key, byte value) {
        this.compound.setByte(key, value);
        return this;
    }

    public NbtTags setShort(String key, short value) {
        this.compound.setShort(key, value);
        return this;
    }

    public NbtTags setInt(String key, int value) {
        this.compound.setInt(key, value);
        return this;
    }

    public NbtTags setLong(String key, long value) {
        this.compound.setLong(key, value);
        return this;
    }

    public NbtTags setFloat(String key, float value) {
        this.compound.setFloat(key, value);
        return this;
    }

    public NbtTags setDouble(String key, double value) {
        this.compound.setDouble(key, value);
        return this;
    }

    public NbtTags setString(String key, String value) {
        this.compound.setString(key, value);
        return this;
    }

    public NbtTags setByteArray(String key, byte[] value) {
        this.compound.setByteArray(key, value);
        return this;
    }

    public NbtTags setIntArray(String key, int[] value) {
        this.compound.setIntArray(key, value);
        return this;
    }

    public NbtTags setBoolean(String key, boolean value) {
        this.compound.setBoolean(key, value);
        return this;
    }

    public NbtTags join(NbtTags nbtTags) {
        for (Map.Entry<String, NBTBase> entry : getCompoundMap(nbtTags.compound).entrySet()) {
            this.compound.set(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public String toString() {
        return toString(this.compound);
    }

}
