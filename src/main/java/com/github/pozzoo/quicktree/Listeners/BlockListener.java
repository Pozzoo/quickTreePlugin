package com.github.pozzoo.quicktree.Listeners;

import com.github.pozzoo.quicktree.QuickTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

    public BlockListener(QuickTree plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (QuickTree.getInstance().getWoodManager().isWoodenLogs(event.getBlock().getType())) {
            Location location = event.getBlock().getLocation();

            QuickTree.getInstance().getWoodManager().checkAround(location, event.getPlayer());

            QuickTree.getInstance().getWoodManager().destroyTree();
        }
    }

}
