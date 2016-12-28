package net.mctournaments.bukkit.modules;

import static net.mctournaments.bukkit.utils.message.MessageType.ERROR;
import static net.mctournaments.bukkit.utils.message.MessageType.INFO;
import static net.mctournaments.bukkit.utils.message.MessageType.SUCCESS;
import static net.mctournaments.bukkit.utils.message.MessageType.WARNING;
import static net.mctournaments.bukkit.utils.message.Messages.send;

import com.google.inject.Inject;
import com.harryfreeborough.modularity.Module;
import com.sk89q.intake.Command;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.Require;
import net.mctournaments.bukkit.command.Sender;
import net.mctournaments.bukkit.events.lifecycle.ReloadConfigEvent;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.bukkit.profile.Rank;
import net.mctournaments.bukkit.profile.permissions.PermissionsManager;
import net.mctournaments.bukkit.utils.message.MessageType;
import net.mctournaments.bukkit.utils.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;

@Module(name = "AdminCommands")
public class AdminCommands implements Listener {

    private final DateTimeFormatter dataTimeFormatter;

    @Inject private Plugin plugin;
    @Inject private ProfileManager profileManager;
    @Inject private PermissionsManager permissionsManager;

    public AdminCommands() {
        this.dataTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.UK)
                .withZone(ZoneId.systemDefault());
    }

    @Command(aliases = "reloadconfigs", desc = "Reloads the configs")
    @Require("mct.admin.reloadconfigs")
    public void reloadConfigs(@Sender CommandSender sender) {
        Bukkit.getPluginManager().callEvent(new ReloadConfigEvent());
        Messages.send(sender, MessageType.SUCCESS, "Successfully reloaded all the configs.");
    }

    @Command(aliases = "setrank", desc = "Sets the target's rank.", usage = "/setrank [target] [rank]")
    @Require("mct.admin.setrank")
    public void setRank(@Sender CommandSender sender, String target, String inputRank) throws CommandException {
        Rank rank = Arrays.stream(Rank.values()).
                filter(r -> r.name().equals(inputRank.toUpperCase())).findAny()
                .orElseThrow(() -> new CommandException("Invalid rank: " + inputRank));

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (this.profileManager.getStorageDao().setRank(target, rank)) {
                send(sender, SUCCESS, "Successfully set %s's rank to %s.", target, rank.name());

                this.profileManager.getProfile(target).ifPresent(profile -> {
                    profile.getRanks().set(rank);
                    Bukkit.getScheduler().runTask(this.plugin, () -> this.permissionsManager.reloadPerms(profile));
                });
            } else {
                send(sender, ERROR, "Failed to set %s's rank to %s.", target, rank.name());
            }
        });
    }

    @Command(aliases = "addrank", desc = "Adds the specified rank to the specified target.", usage = "/addrank [target] [rank]")
    @Require("mct.admin.addrank")
    public void addRank(@Sender CommandSender sender, String target, String inputRank) throws CommandException {
        Rank rank = Arrays.stream(Rank.values()).
                filter(r -> r.name().equals(inputRank.toUpperCase())).findAny()
                .orElseThrow(() -> new CommandException("Invalid rank: " + inputRank));

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (this.profileManager.getStorageDao().addRank(target, rank)) {
                send(sender, SUCCESS, "Successfully added the rank %s to %s.", rank.name(), target);

                this.profileManager.getProfile(target).ifPresent(profile -> {
                    profile.getRanks().add(rank);
                    Bukkit.getScheduler().runTask(this.plugin, () -> this.permissionsManager.reloadPerms(profile));
                });
            } else {
                send(sender, ERROR, "Failed to add the rank %s to %s.", rank.name(), target);
            }
        });
    }

    @Command(aliases = "removerank", desc = "Removes the specified rank from the specified target.", usage = "/removerank [target] [rank]")
    @Require("mct.admin.removerank")
    public void removeRank(@Sender CommandSender sender, String target, String inputRank) throws CommandException {
        Rank rank = Arrays.stream(Rank.values()).
                filter(r -> r.name().equals(inputRank.toUpperCase())).findAny()
                .orElseThrow(() -> new CommandException("Invalid rank: " + inputRank));

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (this.profileManager.getStorageDao().removeRank(target, rank)) {
                send(sender, SUCCESS, "Successfully removed the rank %s from %s.", rank.name(), target);

                this.profileManager.getProfile(target).ifPresent(profile -> {
                    profile.getRanks().remove(rank);
                    Bukkit.getScheduler().runTask(this.plugin, () -> this.permissionsManager.reloadPerms(profile));
                });
            } else {
                send(sender, ERROR, "Failed to remove the rank %s from %s.", rank.name(), target);
            }
        });
    }

    @Command(aliases = "whois", desc = "Returns list of information about the specified player.", usage = "/whois [target]")
    @Require("mct.admin.whois")
    public void whois(@Sender CommandSender sender, String target) throws CommandException {
        this.profileManager.getProfile(target, profile -> {
            send(sender, WARNING, "Player: %s.", profile.getLastKnownUsername());

            send(sender, INFO, "UUID: %s.", profile.getUniqueId());
            send(sender, INFO, "Known usernames: %s.", profile.getKnownUsernames());

            send(sender, INFO, "Last known IP: %s.", profile.getLastKnownIp());
            send(sender, INFO, "Known IPs: %s.", profile.getKnownIps());

            send(sender, INFO, "Ranks: %s.", profile.getRanks());

            send(sender, INFO, "First join: %s.", this.dataTimeFormatter.format(Instant.ofEpochMilli(profile.getFirstJoin())));
            send(sender, INFO, "Last join: %s.", this.dataTimeFormatter.format(Instant.ofEpochMilli(profile.getLastJoin())));
        }, () -> send(sender, ERROR, "Could not find profile for: '%s'.", target));
    }

}
