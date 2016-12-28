package net.mctournaments.bukkit.utils.player;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.mctournaments.bukkit.profile.Profile;
import net.mctournaments.bukkit.utils.packetwrappers.WrapperPlayServerChat;
import net.mctournaments.bukkit.utils.packetwrappers.WrapperPlayServerTitle;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class PlayerUtils {

    private PlayerUtils() {
    }

    public static Optional<Player> getPlayer(Profile profile) {
        return getPlayer(profile.getUniqueId());
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public static Optional<UUID> getUUID(String username) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);

        if (player.hasPlayedBefore() || player.isOnline()) {
            return Optional.of(player.getUniqueId());
        }

        return Optional.empty();
    }

    /**
     * Resets specified {@link Player}'s:
     * <ul>
     *     <li>GameMode to adventure</li>
     *     <li>Ability to Fly</li>
     *     <li>Flying State</li>
     *     <li>Flying Speed</li>
     *     <li>Inventory Contents</li>
     *     <li>EnderChest Contents</li>
     *     <li>Bed Spawn Location</li>
     *     <li>Health</li>
     *     <li>Max Health</li>
     *     <li>Health Scaled</li>
     *     <li>Health Scale</li>
     *     <li>Potion Effects</li>
     *     <li>Food Level</li>
     *     <li>Saturation</li>
     *     <li>Total Experience</li>
     *     <li>Personal Player Time</li>
     *     <li>Personal Player Weather</li>
     * </ul>
     *
     * @param player to reset
     */
    public static void resetPlayer(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFlySpeed(0.1f);
        player.getInventory().setContents(new ItemStack[]{});
        player.getEnderChest().setContents(new ItemStack[]{});
        player.setBedSpawnLocation(null);
        player.setMaxHealth(20);
        player.setHealthScaled(false);
        player.setHealthScale(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setTotalExperience(0);
        player.setPlayerTime(0, true);
        player.setPlayerWeather(null);
    }

    public static void resetTitle(Player player) {
        WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.RESET);
        packet.sendPacket(player);
    }

    public static void sendJson(Player player, String json) {
        WrapperPlayServerChat packet = new WrapperPlayServerChat();
        packet.setMessage(WrappedChatComponent.fromJson(json));
        packet.setPosition((byte) 0);
        packet.sendPacket(player);
    }

}
