package plugin;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darryl on 5-6-15.
 */
public class PluginManager extends JavaPlugin {
    public String BASEPATH;
    public final Map<String, Material> BLOCKS = new HashMap<String, Material>();
    public final HashMap<String, Schematic> SCHEMATICS = new HashMap<String, Schematic>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        String pluginName = getDescription().getName();
        BASEPATH = "plugins" + File.separator + pluginName + File.separator + "schematics" + File.separator;
        if (!new File(BASEPATH).exists()) {
            boolean success = new File(BASEPATH).mkdirs();
            if (!success) {
                getLogger().severe("Initialization of schematic directory failed");
            }
        }

        // TODO: Create template datafile

        Map<String, Object> schematics = getConfig().getConfigurationSection("schematics").getValues(false);
        for (Map.Entry<String, Object> entry : schematics.entrySet()) {
            try {
                String path = BASEPATH + entry.getValue();
                SCHEMATICS.put(entry.getKey(), WorldEditor.getInstance(this).loadSchematic(new File(path)));
            } catch (IOException e) {
                getLogger().severe("Cannot load " + entry.getValue());
                e.printStackTrace();
            }
        }

        Map<String, Object> blocks = getConfig().getConfigurationSection("blocks").getValues(false);
        for (Map.Entry<String, Object> entry : blocks.entrySet()) {
            BLOCKS.put(entry.getKey(), Material.valueOf((String) entry.getValue()));
        }

        getCommand("placebuildings").setExecutor(new SpawnCommand(this));
        getCommand("availablebuildings").setExecutor(new AvailabilityCommand(this));
    }
}