package plugin;

import ch.spacebase.opennbt.stream.NBTInputStream;
import ch.spacebase.opennbt.tag.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darryl on 9-6-15.
 */
public class WorldEditor {
    private static WorldEditor instance = null;
    private final Map<String, Schematic> schematics;
    private final HashMap<String, Material> materials;

    private WorldEditor() {
        schematics = new HashMap<String, Schematic>();
        materials = new HashMap<String, Material>();

        for (String key : ResourceFiles.SCHEMATICS.keySet()) {
            Object entry = ResourceFiles.SCHEMATICS.get(key);
            if (entry instanceof String) {
                String schematic = (String) entry;
                if (!schematic.equals("")) {
                    String path = ResourceFiles.BASEPATH + schematic + ".schematic";
                    try {
                        schematics.put(key, loadSchematic(new File(path)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (entry instanceof Material) {
                Material material = (Material) entry;
                materials.put(key, material);
            }
        }
    }

    public static WorldEditor getInstance() {
        if (instance == null) {
            instance = new WorldEditor();
        }
        return instance;
    }

    public void place(World world, Location location, String key) {
        if (schematics.containsKey(key)) {
            placeSchematic(world, location, schematics.get(key));
        } else {
            placeBlocks(world, location, 1, materials.get(key));
        }
    }

    public void placeBlocks(World world, Location location, int numBlocks, Material material) {
        for (int i = 0; i < numBlocks; i++) {
            location.setY(location.getY() + i);
            world.getBlockAt(location).setType(material);
        }
    }

    public void placeSchematic(World world, Location location, Schematic schematic) {
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + location.getX(), y + location.getY(), z + location.getZ()).getBlock();
                    try {
                        byte block_by_idx = blocks[index];
                        byte block_data = blockData[index];
                        block.setTypeIdAndData(block_by_idx, block_data, true);
                    } catch (NullPointerException e) {
                        throw new NullPointerException(e.getMessage());
                    }
                }
            }
        }
    }

    public Schematic loadSchematic(File file) throws IOException {
        NBTInputStream nbtStream = new NBTInputStream(new FileInputStream(file));

        CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
        if (!schematicTag.getName().equals("Schematic")) {
            throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
        }

        Map<String, Tag> schematic = schematicTag.getValue();
        if (!schematic.containsKey("Blocks")) {
            throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
        }

        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

        String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
        }

        byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
        return new Schematic(blocks, blockData, width, length, height);
    }

    /**
     * Get child tag of a NBT structure.
     *
     * @param items The parent tag map
     * @param key The name of the tag to get
     * @param expected The expected type of the tag
     * @return child tag casted to the expected type
     * @throws IllegalArgumentException if the tag does not exist or the tag is not of the
     * expected type
     */
    private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException
    {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}