package org.nowfow.marx;

import org.bukkit.plugin.java.JavaPlugin;

public final class Markers extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("marx").setExecutor(new MarxCommand(this));

    }

    @Override
    public void onDisable() {

    }
}
