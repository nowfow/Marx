package org.nowfow.marx;

import org.bukkit.plugin.java.JavaPlugin;
import org.nowfow.marx.Commands.MarxCommand;

public final class Markers extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("marx").setExecutor(new MarxCommand(this));

    }

    @Override
    public void onDisable() {

    }
}
