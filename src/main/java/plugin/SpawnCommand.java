package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by darryl on 22-6-15.
 */
public class SpawnCommand implements CommandExecutor {
    private Logger logger;
    private Plugin plugin;
    private InputStream inputstream;

    public SpawnCommand(Plugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
        inputstream = plugin.getResource(ResourceFiles.DATAPOINTS);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();

            InputStreamReader streamReader = new InputStreamReader(inputstream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            try {
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    String[] split = line.split(",");
                    int x = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    String type = split[3];
                    int y = world.getHighestBlockYAt(x, z);

                    Location currentLocation = new Location(world, x, y, z);
                    try {
                        WorldEditor.getInstance().place(world, currentLocation, type);
                        String msg = "Placed " + type + " at X: " + x + " Y: " + y + " Z: " + z;
                        logger.info(msg);
                    } catch (NullPointerException ignored) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
