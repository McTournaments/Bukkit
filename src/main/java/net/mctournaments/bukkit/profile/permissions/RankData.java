package net.mctournaments.bukkit.profile.permissions;

import java.util.List;

public class RankData {

    private final List<String> perms;
    private final List<String> negatedPerms;

    public RankData(List<String> perms, List<String> negatedPerms) {
        this.perms = perms;
        this.negatedPerms = negatedPerms;
    }

    public List<String> getPerms() {
        return this.perms;
    }

    public List<String> getNegatedPerms() {
        return this.negatedPerms;
    }

}
