package plugin;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darryl on 23-6-15.
 */
public class LocationIndexer {
    private List<Location> indexedLocations;
    private static LocationIndexer instance;

    private LocationIndexer() {
        indexedLocations = new ArrayList<Location>();
    }

    public static LocationIndexer getInstance() {
        if (instance == null) {
            instance = new LocationIndexer();
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
        }
    }

    public int getLocationSize() {
        return indexedLocations.size();
    }
}