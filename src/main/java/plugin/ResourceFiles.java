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

    public ResourceFiles() {
        Map<String, Object> schematicsFiles = new HashMap<String, Object>();
        schematicsFiles.put("Aanlegplaats", "");
        schematicsFiles.put("Afmeerboei", "");
        schematicsFiles.put("Bolder", Material.ANVIL);
        schematicsFiles.put("Kleine Silo", "smallsilo");
        schematicsFiles.put("Meerpaal", "paal");
        schematicsFiles.put("Silo", "closedtank2");
        this.SCHEMATICS = schematicsFiles;
    }
}