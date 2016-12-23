package net.mctournaments.bukkit.module;

import com.google.inject.AbstractModule;
import net.mctournaments.bukkit.profile.permissions.PermissionsManager;

public class CoreModule extends AbstractModule {

    private final PermissionsManager permissionsManager;

    public CoreModule(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }

    @Override
    protected void configure() {
        bind(PermissionsManager.class).toInstance(this.permissionsManager);
    }

}
