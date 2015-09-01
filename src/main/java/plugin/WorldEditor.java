package plugin;

import ch.spacebase.opennbt.stream.NBTInputStream;
import ch.spacebase.opennbt.tag.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.la4j.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public void place(World world, Location location, String key, String direction) {
        if (plugin.SCHEMATICS.containsKey(key)) {
            logger.info("Placing schematic " + key + " in direction " + direction);
            placeSchematicOld(world, location, plugin.SCHEMATICS.get(key), direction);
        } else if (plugin.BLOCKS.containsKey(key)) {
            logger.info("Placing Material " + key);
            placeBlocks(world, location, 1, plugin.BLOCKS.get(key));
        }
    }

    public void placeBlocks(World world, Location location, int numBlocks, Material material) {
        for (int i = 0; i < numBlocks; i++) {
            location.setY(location.getY() + i);
            Block block = world.getBlockAt(location);
            block.setType(material);
            LocationIndexer.getInstance(plugin).addLocation(location);
        }
    }

    public void placeSchematic(World world, Location location, Schematic schematic, String direction) {
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();

        List<double[][]> mats = new ArrayList<double[][]>();
        for (int x = 0; x < width; ++x) {
            double[][] mat = new double[height][length];
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    mat[y][z] = index;
                }
            }
            mats.add(mat);
        }

        List<Double> nums = new ArrayList<Double>();
        for (double[][] mat : mats) {
            Matrix matrix = getRotatedMatrix(mat, direction);
            for (int i = 0; i < matrix.rows(); i++) {
                for (int j = 0; j < matrix.columns(); j++) {
                    nums.add(matrix.get(i, j));
                }
            }
        }

        int index = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    placeSchematicBlock(new Location(world,
                            x + location.getX(),
                            y + location.getY(),
                            z + location.getZ()),
                            blocks[(int) nums.get(index).doubleValue()],
                            blockData[(int) nums.get(index).doubleValue()]);
                    index++;
                }
            }
        }
    }

    private void placeSchematicOld(World world, Location location, Schematic schematic, String direction) {
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();

        List<Integer> lognums = new ArrayList<Integer>();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    try {
                        placeSchematicBlock(new Location(world,
                                        x + location.getX(),
                                        y + location.getY(),
                                        z + location.getZ()),
                                blocks[index],
                                blockData[index]);
                        lognums.add(index);
                    } catch (NullPointerException e) {
                        plugin.getLogger().severe("Placement of block failed at index " + index);
                    }
                }
            }
        }
        String out = "";
        for (Integer lognum : lognums) {
            out += lognum + " ";
        }
        plugin.getLogger().info(out);
    }

    private Matrix getRotatedMatrix(double[][] mat, String direction) {
        if (direction.equals("west")) {
            return Matrix.from2DArray(mat).rotate();
        } else if (direction.equals("south")) {
            return Matrix.from2DArray(mat).rotate().rotate();
        } else if (direction.equals("east")) {
            return Matrix.from2DArray(mat).rotate().rotate().rotate();
        } else {
            return Matrix.from2DArray(mat);
        }
    }

    private void placeSchematicBlock(Location location, byte block1, byte b) {
        Block block = location.getBlock();
        try {
            block.setTypeIdAndData(block1, b, true);
            LocationIndexer.getInstance(plugin).addLocation(location);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
            throw new NullPointerException(e.getMessage());
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