import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Created by darryl on 29-5-15.
 */
public class BlockSpawnLoader extends EventLoader {
    @EventHandler
    public void onMapLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        int y;
        int x;
        int z;
        int type;

        String[] split;
        Coords blockCoords;
        Location loc;
        Block currentBlock;

        for (String line : CoordsData.getData()) {
            split = line.split(";");
            y = 0;
            x = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);
            type = Integer.parseInt(split[0]);

            blockCoords = new Coords(x, z, y, type);
            loc = new Location(world, blockCoords.getX(), blockCoords.getY(), blockCoords.getZ(), 0f, 0f);
            currentBlock = world.getBlockAt(loc);

            while (!currentBlock.getType().equals(Material.AIR)) {
                y++;
                blockCoords = new Coords(x, z, y, type);
                loc = new Location(world, blockCoords.getX(), blockCoords.getY(), blockCoords.getZ(), 0f, 0f);
                currentBlock = world.getBlockAt(loc);
            }

            y += 3;

            for (int i = y; i < 10; i++) {
                blockCoords = new Coords(x, z, i, type);
                loc = new Location(world, blockCoords.getX(), blockCoords.getY(), blockCoords.getZ(), 0f, 0f);
                world.getBlockAt(loc).setType(Material.OBSIDIAN);
            }
            logger.info("Block placed at " + blockCoords.toString());
        }
    }
}
