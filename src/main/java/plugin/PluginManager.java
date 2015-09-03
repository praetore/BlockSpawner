package plugin;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by darryl on 5-6-15.
 */
public class PluginManager extends JavaPlugin {
    public final Map<String, Material> BLOCKS = new HashMap<String, Material>();
    public final HashMap<String, Schematic> SCHEMATICS = new HashMap<String, Schematic>();
    public final List<Point> POINTS = new ArrayList<Point>();
    public boolean HEADER_PRESENT;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        File schematicsDir = new File(getDataFolder(), "schematics");
        if (!schematicsDir.exists()) {
            boolean success = schematicsDir.mkdirs();
            if (!success) {
                getLogger().severe("Initialization of schematic directory failed");
            }
        }

        HEADER_PRESENT = getConfig().getConfigurationSection("data").getBoolean("header");

        String dataFileName = getConfig().getConfigurationSection("data").getString("filename");
        File dataFilePath = new File(getDataFolder(), dataFileName);

        if (!dataFilePath.exists()) {
            getLogger().info("Datafile not found, generating template");
            generateDatafileTemplate(dataFileName, dataFilePath);
        } else {
            readDataFile(dataFilePath);
        }

        indexSchematics();
        indexBlocks();


        getCommand("testschematic").setExecutor(new TestPlacementCommand(this));
        getCommand("placebuildings").setExecutor(new SpawnCommand(this));
        getCommand("availablebuildings").setExecutor(new AvailabilityCommand(this));
        getCommand("removebuildings").setExecutor(new RemoveCommand());
    }

    private void indexBlocks() {
        Map<String, Object> blocks = getConfig().getConfigurationSection("blocks").getValues(false);
        for (Map.Entry<String, Object> entry : blocks.entrySet()) {
            BLOCKS.put(entry.getKey(), Material.valueOf((String) entry.getValue()));
            getLogger().info("Loaded up " + entry.getKey());
        }
    }

    private void indexSchematics() {
        try {
            Map<String, Object> schematics = getConfig().getConfigurationSection("schematics").getValues(false);
            for (Map.Entry<String, Object> entry : schematics.entrySet()) {
                File path = new File(getDataFolder() + File.separator + "schematics",
                        (String) entry.getValue());
                try {
                    SCHEMATICS.put(entry.getKey(), WorldEditor.getInstance(this).loadSchematic(path));
                    getLogger().info("Loaded up " + entry.getKey());
                } catch (IOException e) {
                    getLogger().severe("Cannot load " + entry.getValue() + " from " + path);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void generateDatafileTemplate(String dataFileName, File dataFilePath) {
        try {
            boolean success = new File(getDataFolder(), dataFileName).createNewFile();
            if (success) {
                try {
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            dataFilePath
                                    )
                            )
                    );
                    writer.write("x,z,type");
                    writer.close();
                } catch (IOException e) {
                    getLogger().severe("Cannot write to " + dataFileName);
                }
                getLogger().info("Template datafile has been created");
            } else {
                getLogger().severe("Failed to create " + dataFileName);
            }
        } catch (IOException e) {
            getLogger().severe("Failed to create " + dataFileName);
        }
    }

    private void readDataFile(File dataFilePath) {
        try {
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(dataFilePath));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            boolean header = HEADER_PRESENT;
            try {
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    if (!header) {
                        String[] split = line.split(",");
                        int x = 0;
                        int z = 0;
                        try {
                            x = Integer.parseInt(split[0]);
                            z = Integer.parseInt(split[1]);
                        } catch (NumberFormatException e) {
                            getLogger().severe("Unable to format" + split[0] + " or " + split[1]);
                        }
                        String type = split[2];

                        Point point = new Point(x, z, type);
                        POINTS.add(point);
                    } else {
                        header = false;
                    }
                }
                getLogger().info(POINTS.size() + " points have been indexed");
            } catch (IOException e) {
                getLogger().severe("Can't read datafile");
            }
        } catch (FileNotFoundException e) {
            getLogger().severe("Can't find datafile");
        }
    }
}