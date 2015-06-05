package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by darryl on 5-6-15.
 */
public class BlockSpawnCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
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
                String msg = "Block placed at " + blockCoords.toString();
                player.chat(msg);
            }
            return true;
        } else {
            return false;
        }
    }
}