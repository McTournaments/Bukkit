package net.mctournaments.bukkit.utils.player;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.mctournaments.bukkit.utils.packetwrappers.WrapperPlayServerBoss;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BossBar {

    private final UUID uuid;
    private final WrapperPlayServerBoss addPacket;
    private final List<WeakReference<Player>> recievers;

    public BossBar() {
        this.uuid = UUID.randomUUID();
        this.recievers = new ArrayList<>();

        this.addPacket = new WrapperPlayServerBoss();
        this.addPacket.setUniqueId(this.uuid);
        this.addPacket.setAction(WrapperPlayServerBoss.Action.ADD);
    }

    private void updateRecievers() {
        Iterator<WeakReference<Player>> iter = this.recievers.iterator();
        while (iter.hasNext()) {
            Player player = iter.next().get();
            if (player == null || !player.isOnline()) {
                iter.remove();
            }
        }
    }

    public List<Player> getRecievers() {
        this.updateRecievers();

        return this.recievers.stream().map(Reference::get).collect(Collectors.toList());
    }

    public BossBar title(WrappedChatComponent chatComponent) {
        this.addPacket.setTitle(chatComponent);

        List<Player> receivers = this.getRecievers();
        if (!receivers.isEmpty()) {
            WrapperPlayServerBoss updatePacket = new WrapperPlayServerBoss();
            updatePacket.setUniqueId(this.uuid);
            updatePacket.setAction(WrapperPlayServerBoss.Action.UPDATE_NAME);
            updatePacket.setTitle(chatComponent);
            receivers.forEach(updatePacket::sendPacket);
        }

        return this;
    }

    public BossBar title(String title) {
        return this.title(WrappedChatComponent.fromText(title));
    }

    public BossBar health(float value) {
        this.addPacket.setHealth(value);

        List<Player> receivers = this.getRecievers();
        if (!receivers.isEmpty()) {
            WrapperPlayServerBoss updatePacket = new WrapperPlayServerBoss();
            updatePacket.setUniqueId(this.uuid);
            updatePacket.setAction(WrapperPlayServerBoss.Action.UPDATE_PCT);
            updatePacket.setHealth(value);
            receivers.forEach(updatePacket::sendPacket);
        }

        return this;
    }

    public BossBar color(BarColor color) {
        this.addPacket.setColor(color);

        List<Player> receivers = this.getRecievers();
        if (!receivers.isEmpty()) {
            WrapperPlayServerBoss updatePacket = new WrapperPlayServerBoss();
            updatePacket.setUniqueId(this.uuid);
            updatePacket.setAction(WrapperPlayServerBoss.Action.UPDATE_STYLE);
            updatePacket.setColor(color);
            receivers.forEach(updatePacket::sendPacket);
        }

        return this;
    }

    public BossBar style(WrapperPlayServerBoss.BarStyle style) {
        this.addPacket.setStyle(style);

        List<Player> receivers = this.getRecievers();
        if (!receivers.isEmpty()) {
            WrapperPlayServerBoss updatePacket = new WrapperPlayServerBoss();
            updatePacket.setUniqueId(this.uuid);
            updatePacket.setAction(WrapperPlayServerBoss.Action.UPDATE_STYLE);
            updatePacket.setStyle(style);
            receivers.forEach(updatePacket::sendPacket);
        }

        return this;
    }

    public void addReciever(Player player) {
        this.addPacket.sendPacket(player);
        this.recievers.add(new WeakReference<>(player));
    }

    public void remove(Player player) {
        WrapperPlayServerBoss packet = new WrapperPlayServerBoss();
        packet.setUniqueId(this.uuid);
        packet.setAction(WrapperPlayServerBoss.Action.REMOVE);
        packet.sendPacket(player);
    }

    public void removeReciever(Player player) {
        this.recievers.removeIf(playerWeakReference -> Objects.equals(playerWeakReference.get(), player));
    }

    public void clearRecievers() {
        this.recievers.clear();
    }

}
