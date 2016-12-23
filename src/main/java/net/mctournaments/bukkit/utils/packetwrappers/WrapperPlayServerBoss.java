package net.mctournaments.bukkit.utils.packetwrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.boss.BarColor;

import java.util.UUID;


public class WrapperPlayServerBoss extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.BOSS;

    public WrapperPlayServerBoss() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerBoss(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve UUID.
     * <p>
     * Notes: unique ID for this bar
     *
     * @return The current UUID
     */
    public UUID getUniqueId() {
        return handle.getUUIDs().read(0);
    }

    /**
     * Set UUID.
     *
     * @param value - new value.
     */
    public void setUniqueId(UUID value) {
        handle.getUUIDs().write(0, value);
    }

    public Action getAction() {
        return handle.getEnumModifier(Action.class, 1).read(0);
    }

    public void setAction(Action value) {
        handle.getEnumModifier(Action.class, 1).write(0, value);
    }

    public WrappedChatComponent getTitle() {
        return handle.getChatComponents().read(0);
    }

    public void setTitle(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }

    public float getHealth() {
        return handle.getFloat().read(0);
    }

    public void setHealth(float value) {
        handle.getFloat().write(0, value);
    }

    public BarColor getColor() {
        return handle.getEnumModifier(BarColor.class, 4).read(0);
    }

    public void setColor(BarColor value) {
        handle.getEnumModifier(BarColor.class, 4).write(0, value);
    }

    public BarStyle getStyle() {
        return handle.getEnumModifier(BarStyle.class, 5).read(0);
    }

    public void setStyle(BarStyle value) {
        handle.getEnumModifier(BarStyle.class, 5).write(0, value);
    }

    public boolean isDarkenSky() {
        return handle.getBooleans().read(0);
    }

    public void setDarkenSky(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public boolean isPlayMusic() {
        return handle.getBooleans().read(1);
    }

    public void setPlayMusic(boolean value) {
        handle.getBooleans().write(1, value);
    }

    public boolean isCreateFog() {
        return handle.getBooleans().read(2);
    }

    public void setCreateFog(boolean value) {
        handle.getBooleans().write(2, value);
    }

    public static enum Action {
        ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
    }

    public static enum BarStyle {
        PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;
    }

}
