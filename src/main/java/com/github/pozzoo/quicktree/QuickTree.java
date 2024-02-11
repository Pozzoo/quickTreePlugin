package com.github.pozzoo.quicktree;

import com.github.pozzoo.quicktree.Listeners.BlockListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuickTree extends JavaPlugin {
    private WoodManager woodManager;
    private static QuickTree instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.woodManager = new WoodManager();
        new BlockListener(instance);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static QuickTree getInstance() {
        return instance;
    }

    public WoodManager getWoodManager() {
        return woodManager;
    }
}
