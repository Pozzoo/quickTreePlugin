package com.github.pozzoo.quicktree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.*;

public class WoodManager {
    private final List<Material> woods;
    private final Set<Location> treeModel;
    private final List<BlockDisplay> treeDisplay;
    private final List<Vector> coordsVector;

    public WoodManager() {
        this.treeModel = new HashSet<>();
        this.coordsVector = new ArrayList<>();
        this.woods = new ArrayList<>();
        this.treeDisplay = new ArrayList<>();

        warmupWoods();
        warmupCoords();
    }

    private void warmupWoods() {
        this.woods.add(Material.ACACIA_LOG);
        this.woods.add(Material.BIRCH_LOG);
        this.woods.add(Material.CHERRY_LOG);
        this.woods.add(Material.JUNGLE_LOG);
        this.woods.add(Material.DARK_OAK_LOG);
        this.woods.add(Material.MANGROVE_LOG);
        this.woods.add(Material.OAK_LOG);
        this.woods.add(Material.SPRUCE_LOG);
        this.woods.add(Material.CRIMSON_STEM);
        this.woods.add(Material.WARPED_STEM);
    }

    private void warmupCoords() {
        coordsVector.add(new Vector(1, 0, 0));
        coordsVector.add(new Vector(0, 0, 1));
        coordsVector.add(new Vector(-1, 0, 0));
        coordsVector.add(new Vector(-1, 0, 0));
        coordsVector.add(new Vector(0, 0, -1));
        coordsVector.add(new Vector(0, 0, -1));
        coordsVector.add(new Vector(1, 0, 0));
        coordsVector.add(new Vector(1, 0, 0));
        coordsVector.add(new Vector(-1, 0, 1));
    }

    public boolean isWoodenLogs(Material material) {
        return this.woods.contains(material);
    }

    public Vector getCoordsVector(int i) {
        return coordsVector.get(i);
    }

    public void destroyTree() {
        for (Location location : this.treeModel) {
            location.getBlock().breakNaturally(true);
        }

        this.treeModel.clear();
    }

    public void addBlockLocation(Location location) {
        this.treeModel.add(location);
    }

    public void checkAround(Location location) {

        Location location1 = location.clone();

        while (!location1.getBlock().getType().equals(Material.AIR)) {

            for (int i = 0; i < coordsVector.size(); i++) {
                if (isWoodenLogs(location1.getBlock().getType())) {
                    addBlockLocation(location1.getBlock().getLocation());
                }
                location1.add(getCoordsVector(i));
            }
            location1.add(0, 1, 0);
        }

        createTree();
    }

    private void createTree() {
        for (Location location : treeModel) {
            BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);
            blockDisplay.setBlock(location.getBlock().getBlockData());

            this.treeDisplay.add(blockDisplay);
        }

        animateTree();
    }

    private void animateTree() {
        new BukkitRunnable() {
            int iterations = 0;

            @Override
            public void run() {
                for (BlockDisplay blockDisplay : treeDisplay) {
                    int height = 0;

                    Location location = blockDisplay.getLocation().clone();

                    while (location.getBlock().getType().equals(Material.AIR)) {
                        location.add(0, -1, 0);
                        height++;
                    }

                    Transformation transformation = blockDisplay.getTransformation();

                    transformation.getLeftRotation().rotateZ((float) Math.toRadians((double) 90 / 10));

                    transformation.getTranslation().x -= (float) (height - 1) / 10;
                    transformation.getTranslation().y -= (float) (height - 1) / 10;

                    blockDisplay.setTransformation(transformation);
                }
                iterations++;

                if (iterations == 10) {
                    this.cancel();
                    explodeBlocks();
                }
            }
        }.runTaskTimer(QuickTree.getInstance(), 0, 1);

        destroyTree();
    }

    private void explodeBlocks() {
        new BukkitRunnable() {

            @Override
            public void run() {

                for (BlockDisplay blockDisplay : treeDisplay) {
                    blockDisplay.remove();
                    blockDisplay.getWorld().spawnParticle(Particle.BLOCK_CRACK, blockDisplay.getLocation(), 50, 1, 5, 0, Material.SPRUCE_LOG.createBlockData());

                }
                treeDisplay.clear();
            }
        }.runTaskLater(QuickTree.getInstance(), 20);

    }
}
