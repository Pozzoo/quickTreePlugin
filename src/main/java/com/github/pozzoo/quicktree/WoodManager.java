package com.github.pozzoo.quicktree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.*;

public class WoodManager {
    private final List<Material> woods;
    private Set<Location> treeModel;
    private final List<Vector> coordsVector;

    public WoodManager() {
        this.treeModel = new HashSet<>();
        this.coordsVector = new ArrayList<>();
        this.woods = new ArrayList<>();

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

    public List<Vector> getCoordsVectorList() {
        return coordsVector;
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

    public void checkAround(Location location, Player player) {
        while (!location.getBlock().getType().equals(Material.AIR)) {
            for (int i = 0; i < coordsVector.size(); i++) {
                if (isWoodenLogs(location.getBlock().getType())) {
                    addBlockLocation(location.getBlock().getLocation());
                }
                location.add(getCoordsVector(i));
            }
            location.add(0, 1, 0);
        }

        createTree();
    }

    private void createTree() {
        float index = 0;

        for (Location location : treeModel) {

            int height = 0;

            Location location1 = location.clone();

            while (isWoodenLogs(location1.getBlock().getType())) {
                location1.add(0, -1, 0);
                height++;
            }

            BlockDisplay blockDisplay = location.getWorld().spawn(location, BlockDisplay.class);
            blockDisplay.setBlock(location.getBlock().getBlockData());

            Transformation transformation = blockDisplay.getTransformation();

            transformation.getLeftRotation().rotateZ((float) Math.toRadians(90));
            transformation.getTranslation().x -= index;
            transformation.getTranslation().y -= height - 1;

            blockDisplay.setTransformation(transformation);

            index++;
        }
    }
}
