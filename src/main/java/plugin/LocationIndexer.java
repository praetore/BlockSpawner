package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by darryl on 23-6-15.
 */
public class LocationIndexer {
    private final Logger logger;
    private List<Location> indexedLocations;
    private static LocationIndexer instance;

    private LocationIndexer(Plugin plugin) {
        logger = plugin.getLogger();
        indexedLocations = new ArrayList<Location>();
    }

    public static LocationIndexer getInstance(Plugin plugin) {
        if (instance == null) {
            instance = new LocationIndexer(plugin);
        }
        return instance;
    }

    public void addLocation(Location location) {
        indexedLocations.add(location);
    }

    public void clearLocations() {
        if (indexedLocations.size() > 0) {
            for (Location location : indexedLocations) {
                location.getBlock().setType(Material.AIR);
            }
        } else {
            logger.info("No blocks are placed during this session");
        }
    }
}