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
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by darryl on 9-6-15.
 */
public class WorldEditor {
    private static WorldEditor instance = null;
    private Logger logger;
    private PluginManager plugin;

    private WorldEditor(PluginManager plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public static WorldEditor getInstance(PluginManager plugin) {
        if (instance == null) {
            instance = new WorldEditor(plugin);
        }
        return instance;
    }

    public void place(World world, Location location, String key) {
        logger.info("At X: " + location.getX() + " - Y:" + location.getY() +  " - Z:" + location.getZ());
        if (plugin.SCHEMATICS.containsKey(key)) {
            logger.info("Placing schematic " + key);
            placeSchematic(world, location, plugin.SCHEMATICS.get(key));
        } else if (plugin.BLOCKS.containsKey(key)) {
            logger.info("Placing Material " + key);
            placeBlocks(world, location, 1, plugin.BLOCKS.get(key));
        }
    }

    private void placeBlocks(World world, Location location, int numBlocks, Material material) {
        for (int i = 0; i < numBlocks; i++) {
            location.setY(location.getY() + i);
            Block block = world.getBlockAt(location);
            block.setType(material);
            LocationIndexer.getInstance().addLocation(location);
        }
    }

    private void placeSchematic(World world, Location location, Schematic schematic) {
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();
        int errorCount = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    try {
                        Location blockLocation = new Location(world,
                                        x + location.getX(),
                                        y + location.getY(),
                                        z + location.getZ());
                        Block block = blockLocation.getBlock();
                        try {
                            block.setTypeIdAndData(blocks[index], blockData[index], true);
                            LocationIndexer.getInstance().addLocation(blockLocation);
                        } catch (NullPointerException e) {
                            throw new NullPointerException(e.getMessage());
                        }
                    } catch (NullPointerException e) {
                        errorCount++;
                    }
                }
            }
        }

        if (errorCount > 0) {
            logger.info(errorCount + " errors during the placement of " + schematic.getFilename() + "\n" +
                    "This is probably because of a faulty block type in the schematic.");
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
        return new Schematic(blocks, blockData, width, length, height, file.getName());
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