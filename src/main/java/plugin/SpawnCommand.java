package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Created by darryl on 22-6-15.
 */
public class SpawnCommand implements CommandExecutor {
    private Logger logger;
    private PluginManager plugin;

    public SpawnCommand(PluginManager plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();

            WorldEditor instance = WorldEditor.getInstance(plugin);
            for (Point point : plugin.POINTS) {
                int x = point.getX();
                int z = point.getZ();
                int y = world.getHighestBlockYAt(x, z);
                String type = point.getType();
                Location currentLocation = new Location(world, x, y, z);
                try {
                    instance.place(world, currentLocation, type);
                    String msg = "Placed " + type + " at X: " + x + " Y: " + y + " Z: " + z;
                    logger.info(msg);
                } catch (NullPointerException e) {
                    logger.severe("Error placing " + type);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
