package dev.vyklade;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.vyklade.events.VykladeDamageEvent;

import javax.annotation.Nonnull;

public class VykladeSpellbooks extends JavaPlugin {

    public VykladeSpellbooks(@Nonnull JavaPluginInit init) {
        super(init);
    }
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    protected void setup() {
        this.getEntityStoreRegistry().registerSystem(new VykladeDamageEvent());
    }
}

