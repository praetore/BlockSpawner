package plugin;

import java.io.File;

/**
 * Created by darryl on 16-6-15.
 */
public interface ResourceFiles {
    String PAALSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" +
            File.separator + "schematics" + File.separator + "paal.schematic";

    String SILOSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" +
            File.separator + "schematics" + File.separator + "pointytower.schematic";

    String DATAPOINTS = "datapoints.csv";

    String SILOPOINTS = "silopoints.csv";
}