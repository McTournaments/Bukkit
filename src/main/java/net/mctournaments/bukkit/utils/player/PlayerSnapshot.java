package net.mctournaments.bukkit.utils.player;

import com.google.common.base.Objects;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

/**
 * Holds a snapshot of a {@link Player}'s data.
 */
public class PlayerSnapshot {

    private final GameMode gameMode;
    private final boolean allowFlight;
    private final boolean flying;
    private final float flySpeed;
    private final ItemStack[] inventoryContents;
    private final ItemStack[] enderChestContents;
    private final Location location;
    private final Location bedSpawnLocation;
    private final double health;
    private final double maxHealth;
    private final boolean healthScaled;
    private final double healthScale;
    private final Collection<PotionEffect> potionEffects;
    private final int foodLevel;
    private final float saturation;
    private final int totalExperience;
    private final long playerTimeOffset;
    private final WeatherType playerWeather;

    /**
     * Creates a snapshot of the specified player's:
     * <ul>
     *     <li>GameMode to adventure</li>
     *     <li>Ability to Fly</li>
     *     <li>Flying State</li>
     *     <li>Flying Speed</li>
     *     <li>Inventory Contents</li>
     *     <li>EnderChest Contents</li>
     *     <li>Location</li>
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
     * @param player to create a snapshot's data of
     */
    public PlayerSnapshot(Player player) {
        this.gameMode = player.getGameMode();
        this.allowFlight = player.getAllowFlight();
        this.flying = player.isFlying();
        this.flySpeed = player.getFlySpeed();
        this.inventoryContents = player.getInventory().getContents();
        this.enderChestContents = player.getEnderChest().getContents();
        this.location = player.getLocation();
        this.bedSpawnLocation = player.getBedSpawnLocation();
        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();
        this.healthScaled = player.isHealthScaled();
        this.healthScale = player.getHealthScale();
        this.potionEffects = player.getActivePotionEffects();
        this.foodLevel = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.totalExperience = player.getTotalExperience();
        this.playerTimeOffset = player.getPlayerTimeOffset();
        this.playerWeather = player.getPlayerWeather();
    }

    /**
     * Sets all of the {@link Player}'s data to the data from {@code this} snapshot.
     *
     * @param player
     */
    public void resetToSnapshot(Player player) {
        player.setGameMode(this.gameMode);
        player.setAllowFlight(this.allowFlight);
        player.setFlying(this.flying);
        player.setFlySpeed(this.flySpeed);
        player.getInventory().setContents(this.inventoryContents);
        player.getEnderChest().setContents(this.enderChestContents);
        player.teleport(this.location);
        player.setBedSpawnLocation(this.bedSpawnLocation);
        player.setHealth(this.health);
        player.setMaxHealth(this.maxHealth);
        player.setHealthScaled(this.healthScaled);
        player.setHealthScale(this.healthScale);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        this.potionEffects.forEach(player::addPotionEffect);

        player.setFoodLevel(this.foodLevel);
        player.setSaturation(this.saturation);
        player.setTotalExperience(this.totalExperience);
        player.setPlayerTime(this.playerTimeOffset, true);
        player.setPlayerWeather(this.playerWeather);
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public boolean getAllowFlight() {
        return this.allowFlight;
    }

    public boolean isFlying() {
        return this.flying;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public ItemStack[] getInventoryContents() {
        return this.inventoryContents.clone();
    }

    public ItemStack[] getEnderChestContents() {
        return this.enderChestContents.clone();
    }

    public Location getLocation() {
        return this.location;
    }

    public double getHealth() {
        return this.health;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public boolean isHealthScaled() {
        return this.healthScaled;
    }

    public double getHealthScale() {
        return this.healthScale;
    }

    public Collection<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public int getTotalExperience() {
        return this.totalExperience;
    }

    public long getPlayerTimeOffset() {
        return this.playerTimeOffset;
    }

    public WeatherType getPlayerWeather() {
        return this.playerWeather;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("gameMode", this.gameMode)
                .add("allowFlight", this.allowFlight)
                .add("flying", this.flying)
                .add("flySpeed", this.flySpeed)
                .add("inventoryContents", this.inventoryContents)
                .add("enderChestContents", this.enderChestContents)
                .add("location", this.location)
                .add("bedSpawnLocation", this.bedSpawnLocation)
                .add("health", this.health)
                .add("maxHealth", this.maxHealth)
                .add("healthScaled", this.healthScaled)
                .add("healthScale", this.healthScale)
                .add("potionEffects", this.potionEffects)
                .add("foodLevel", this.foodLevel)
                .add("saturation", this.saturation)
                .add("totalExperience", this.totalExperience)
                .add("playerTimeOffset", this.playerTimeOffset)
                .add("playerWeather", this.playerWeather)
                .toString();
    }

}
