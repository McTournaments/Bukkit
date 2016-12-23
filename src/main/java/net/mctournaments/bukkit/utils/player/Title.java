package net.mctournaments.bukkit.utils.player;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.base.Preconditions;
import net.mctournaments.bukkit.utils.packetwrappers.WrapperPlayServerTitle;
import org.bukkit.entity.Player;

public class Title {

    private WrapperPlayServerTitle title;
    private WrapperPlayServerTitle subtitle;
    private WrapperPlayServerTitle timings;

    public Title() {
        this.timings = new WrapperPlayServerTitle();
        this.timings.setAction(EnumWrappers.TitleAction.TIMES);
        this.timings.setFadeIn(20);
        this.timings.setStay(20);
        this.timings.setFadeOut(20);
    }

    public Title title(WrappedChatComponent chatComponent) {
        this.title = new WrapperPlayServerTitle();
        this.title.setAction(EnumWrappers.TitleAction.TITLE);
        this.title.setTitle(chatComponent);
        return this;
    }

    public Title title(String title) {
        return this.title(WrappedChatComponent.fromText(title));
    }

    public Title subtitle(WrappedChatComponent chatComponent) {
        this.subtitle = new WrapperPlayServerTitle();
        this.subtitle.setAction(EnumWrappers.TitleAction.SUBTITLE);
        this.subtitle.setTitle(chatComponent);
        return this;
    }

    public Title subtitle(String subtitle) {
        return this.subtitle(WrappedChatComponent.fromText(subtitle));
    }

    public Title fadeIn(int fadeIn) {
        this.timings.setFadeIn(fadeIn);
        return this;
    }

    public Title stay(int stay) {
        this.timings.setFadeIn(stay);
        return this;
    }

    public Title fadeOut(int fadeOut) {
        this.timings.setFadeIn(fadeOut);
        return this;
    }

    public void send(Player... players) {
        Preconditions.checkState(this.title != null || this.subtitle != null, "Title and subtitle can not both be null.");

        for (Player player : players) {
            PlayerUtils.resetTitle(player);

            this.timings.sendPacket(player);

            if (this.title != null) {
                this.title.sendPacket(player);
            }

            if (this.subtitle != null) {
                this.subtitle.sendPacket(player);
            }
        }
    }

}
