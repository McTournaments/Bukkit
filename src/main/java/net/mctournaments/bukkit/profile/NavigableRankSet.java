package net.mctournaments.bukkit.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class NavigableRankSet {

    private final NavigableSet<Rank> base;

    public NavigableRankSet() {
        this.base = new TreeSet<>();
    }

    public NavigableRankSet(NavigableSet<Rank> ranks) {
        this();

        ranks.forEach(this::add);
    }

    public NavigableRankSet(List<Rank> ranks) {
        this();

        ranks.forEach(this::add);
    }

    /**
     * Adds the {@link Rank} to the {@link Set} of {@link Rank}s.
     * <p>
     * If the set already contains a rank which contains the specified {@link Rank} in its inheritance tree,
     * the {@link Rank} is not added.
     *
     * @param rank to add
     */
    public boolean add(Rank rank) {
        if (!this.hasPermsOf(checkNotNull(rank, "rank"))) {
            this.base.removeIf(rank1 -> rank.inheritanceTree().contains(rank1));

            return this.base.add(rank);
        }

        return false;
    }

    /**
     * Removes the specified {@link Rank} from {@code this} {@link Set} of {@link Rank}s.
     * If {@link Set#isEmpty()} returns true from the {@link Set}, then {@link Rank#DEFAULT}
     * is added to {@code this} {@link Set}.
     *
     * @param rank to remove from the set of {@link Rank}s
     */
    public boolean remove(Rank rank) {
        if (this.base.remove(checkNotNull(rank, "rank"))) {
            if (this.base.isEmpty()) {
                this.base.add(Rank.DEFAULT);
            }

            return true;
        }

        return false;
    }

    public void set(Rank rank) {
        this.base.clear();
        this.base.add(rank);
    }

    public void set(NavigableSet<Rank> ranks) {
        this.base.clear();
        ranks.forEach(this::add);

        if (this.base.isEmpty()) {
            this.base.add(Rank.DEFAULT);
        }
    }

    /**
     * Returns whether {@code this} {@link Set}'s {@link Rank} have the perms of the specified {@link Rank}.
     *
     * @param rank to check
     * @return whether the user has the perms of the specified rank
     */
    public boolean hasPermsOf(Rank rank) {
        return this.base.stream().anyMatch(r -> r.inheritanceTree().contains(rank));
    }

    public NavigableSet<Rank> asNavigatableSet() {
        return Collections.unmodifiableNavigableSet(this.base);
    }

    public List<Rank> asList() {
        return new ArrayList<>(this.base);
    }

    @Override
    public String toString() {
        return this.base.toString();
    }
}
