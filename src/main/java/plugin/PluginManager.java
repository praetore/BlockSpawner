package plugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darryl on 5-6-15.
 */
public class PluginManager extends JavaPlugin {
    public final Map<String, Material> BLOCKS = new HashMap<String, Material>();
    public final HashMap<String, Schematic> SCHEMATICS = new HashMap<String, Schematic>();
    public File DATAFILE;
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
            boolean success = false;

            try {
                success = new File(getDataFolder(), dataFileName).createNewFile();
            } catch (IOException e) {
                getLogger().severe("Failed to create " + dataFileName);
            }

            if (!success) {
                getLogger().severe("Failed to create " + dataFileName);
            }
        }

        try {
            File file = new File(getDataFolder(), dataFileName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write("x,z,type");
            writer.close();
            DATAFILE = file;
        } catch (IOException e) {
            getLogger().severe("Cannot write to " + dataFileName);
        }

        try {
            Map<String, Object> schematics = getConfig().getConfigurationSection("schematics").getValues(false);
            for (Map.Entry<String, Object> entry : schematics.entrySet()) {
                File path = new File(getDataFolder() + File.separator + "schematics",
                        (String) entry.getValue());
                try {
                    SCHEMATICS.put(entry.getKey(), WorldEditor.getInstance(this).loadSchematic(path));
                } catch (IOException e) {
                    getLogger().severe("Cannot load " + entry.getValue() + " from " + path);
                }
            }

            Map<String, Object> blocks = getConfig().getConfigurationSection("blocks").getValues(false);
            for (Map.Entry<String, Object> entry : blocks.entrySet()) {
                BLOCKS.put(entry.getKey(), Material.valueOf((String) entry.getValue()));
            }
        } catch (NullPointerException ignored) {
        }

        getCommand("placebuildings").setExecutor(new SpawnCommand(this));
        getCommand("availablebuildings").setExecutor(new AvailabilityCommand(this));
        getCommand("removebuildings").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                LocationIndexer.getInstance(PluginManager.this).clearLocations();
                return true;
            }
        });
    }
}