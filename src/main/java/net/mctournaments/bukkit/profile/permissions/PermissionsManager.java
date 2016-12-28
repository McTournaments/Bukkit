package net.mctournaments.bukkit.profile.permissions;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.MapMaker;
import net.mctournaments.bukkit.profile.Profile;
import net.mctournaments.bukkit.profile.Rank;
import net.mctournaments.bukkit.utils.player.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

public class PermissionsManager {

    private final Plugin plugin;
    private final RankDataDao dao;
    private final Map<Rank, RankData> rankData;
    private final Map<Player, PermissionAttachment> permissionAttatchments;

    public PermissionsManager(Plugin plugin, RankDataDao dao) {
        this.plugin = plugin;

        this.dao = dao;
        this.rankData = new HashMap<>();
        this.permissionAttatchments = new MapMaker().weakKeys().makeMap();
    }

    public void loadRankData() {
        Arrays.stream(Rank.values()).forEach(rank -> this.rankData.put(rank, dao.getRankData(rank)));
    }

    public void reloadPerms(Profile profile) {
        Optional<Player> player = PlayerUtils.getPlayer(profile);

        if (player.isPresent()) {
            if (isAttached(player.get())) {
                this.removeAttachment(player.get());
            }

            this.attachAndSet(profile);
        }
    }

    public boolean isAttached(Player player) {
        return this.permissionAttatchments.containsKey(player);
    }

    public void attachAndSet(Profile profile) {
        Optional<Player> player = PlayerUtils.getPlayer(profile);
        checkState(player.isPresent(), "Player must be online to attach perms.");

        PermissionAttachment attachment = player.get().addAttachment(this.plugin);

        NavigableSet<Rank> permRanks = new TreeSet<>();
        profile.getRanks().asNavigableSet().forEach(rank -> permRanks.addAll(rank.inheritanceTree()));

        permRanks.forEach(rank -> {
            RankData rankData = this.rankData.get(rank);

            rankData.getNegatedPerms().forEach(perm -> attachment.setPermission(perm, false));
            rankData.getPerms().forEach(perm -> attachment.setPermission(perm, true));
        });

        this.permissionAttatchments.put(player.get(), attachment);
    }

    public void removeAttachment(Player player) {
        player.removeAttachment(this.permissionAttatchments.get(player));
    }

    public RankDataDao getDao() {
        return this.dao;
    }

}
