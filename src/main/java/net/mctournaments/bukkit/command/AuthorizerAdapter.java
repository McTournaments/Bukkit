package net.mctournaments.bukkit.command;

import com.google.common.base.Preconditions;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.Authorizer;
import org.bukkit.command.CommandSender;

public class AuthorizerAdapter implements Authorizer {

    @Override public boolean testPermission(Namespace namespace, String permission) {
        CommandSender sender = Preconditions.checkNotNull(namespace.get(CommandSender.class), "sender");
        return sender.hasPermission(permission);
    }

}
