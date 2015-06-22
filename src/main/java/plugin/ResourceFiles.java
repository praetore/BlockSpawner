package plugin;

import org.bukkit.Material;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darryl on 16-6-15.
 */
public class ResourceFiles {
    public static String DATAPOINTS = "points.csv";
    public static String BASEPATH = "plugins" + File.separator + "WorldEdit" +
            File.separator + "schematics" + File.separator;

    public Map<String, Object> SCHEMATICS;
    private static ResourceFiles instance = null;

    private ResourceFiles() {
        Map<String, Object> schematics = new HashMap<String, Object>();
        schematics.put("Aanlegplaats", "");
        schematics.put("Afmeerboei", "");
        schematics.put("Bolder", Material.ANVIL);
        schematics.put("Kleine Silo", "smallsilo");
        schematics.put("Meerpaal", "paal");
        schematics.put("Silo", "closedtank");
        SCHEMATICS = schematics;
    }

    public static ResourceFiles getInstance() {
        if (instance == null) {
            instance = new ResourceFiles();
        }
        return instance;
    }
}