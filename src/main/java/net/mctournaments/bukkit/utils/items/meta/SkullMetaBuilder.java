package net.mctournaments.bukkit.utils.items.meta;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullMetaBuilder implements MetaBuilder {

    private String owner;

    private SkullMetaBuilder() {
    }

    public static SkullMetaBuilder create() {
        return new SkullMetaBuilder();
    }

    public SkullMetaBuilder owner(String owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public void attach(ItemMeta meta) {
        ((SkullMeta) meta).setOwner(this.owner);
    }

}
