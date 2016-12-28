package net.mctournaments.bukkit.utils.items;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import net.mctournaments.bukkit.utils.items.gadget.GadgetFactory;
import net.mctournaments.bukkit.utils.items.meta.MetaBuilder;
import net.mctournaments.bukkit.utils.nms.NbtTags;
import net.mctournaments.bukkit.utils.nms.NmsItems;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack base;
    private ItemMeta meta;
    private NbtTags tags;
    private String gadget;

    private ItemBuilder(ItemStack base) {
        this.base = base;
        this.meta = this.base.getItemMeta();

        this.tags = NbtTags.create();
    }

    public static ItemBuilder create(Material material) {
        checkState(NmsItems.isValidItemType(material), "Specified Material is not a valid item type, check if it's a block type.");

        return new ItemBuilder(new ItemStack(material));
    }

    public static ItemBuilder create(ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public ItemBuilder name(String name) {
        checkNotNull(this.meta, "meta on name");
        this.meta.setDisplayName(checkNotNull(name, "name"));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.meta.setLore(checkNotNull(lore, "lore"));
        return this;
    }

    public ItemBuilder lore(String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    public ItemBuilder amount(int amount) {
        this.base.setAmount(amount);
        return this;
    }

    public ItemBuilder data(int data) {
        this.base.setDurability((short) data);
        return this;
    }

    public ItemBuilder dyeData(DyeColor color) {
        return this.data(color.getDyeData());
    }

    public ItemBuilder woolData(DyeColor color) {
        return this.data(color.getWoolData());
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder tags(Consumer<NbtTags> consumer) {
        consumer.accept(this.tags);
        return this;
    }

    public ItemBuilder gadget(String gadget) {
        checkArgument(gadget != null && !gadget.isEmpty(), "gadget name cannot be null or empty.");
        this.gadget = gadget;
        return this;
    }

    public ItemBuilder meta(MetaBuilder builder) {
        builder.attach(this.meta);
        return this;
    }

    public ItemStack build() {
        this.base.setItemMeta(this.meta);
        this.base = NmsItems.bukkitToCraft(this.base);
        NbtTags tags = NbtTags.craftCreate(this.base).join(this.tags);
        if (this.gadget != null && !this.gadget.isEmpty()) {
            tags.setString(GadgetFactory.GADGET_NBT_KEY, this.gadget);
        }

        return this.base;
    }

}
