package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
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

            try {
                InputStreamReader streamReader = new InputStreamReader(new FileInputStream(plugin.DATAFILE));
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                WorldEditor instance = WorldEditor.getInstance(plugin);
                boolean header = plugin.HEADER_PRESENT;
                try {
                    while (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        if (!header) {
                            String[] split = line.split(",");
                            int x = Integer.parseInt(split[0]);
                            int z = Integer.parseInt(split[1]);
                            String type = split[2];
                            int y = world.getHighestBlockYAt(x, z);

                            Location currentLocation = new Location(world, x, y, z);
                            try {
                                instance.place(world, currentLocation, type);
                                String msg = "Placed " + type + " at X: " + x + " Y: " + y + " Z: " + z;
                                logger.info(msg);
                            } catch (NullPointerException e) {
                                logger.severe("Error placing " + type);
                            }
                        } else {
                            header = false;
                        }
                    }
                } catch (IOException e) {
                    return false;
                }
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }

        } else {
            return false;
        }
    }
}
