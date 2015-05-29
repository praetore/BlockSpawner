import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.MapInitializeEvent;

/**
 * Created by darryl on 29-5-15.
 */
public class BlockSpawnLoader extends EventPlugin {
    @EventHandler
    public void onMapLoad(MapInitializeEvent event) {
        World world = event.getMap().getWorld();
        Coords blockCoords;
        for (String line : CoordsData.getData()) {
            String[] split = line.split(";");
            blockCoords = new Coords(Integer.parseInt(split[1]), Integer.parseInt(split[2]), 85, Integer.parseInt(split[0]));
            Location loc = new Location(world, blockCoords.getX(), blockCoords.getY(), blockCoords.getZ(), 0f, 0f);
            world.getBlockAt(loc).setType(Material.OBSIDIAN);
        }
    }
}
