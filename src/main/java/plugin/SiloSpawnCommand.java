package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

import static plugin.ResourceFiles.SILOSCHEMATICPATH;

/**
 * Created by darryl on 21-6-15.
 */
public class SiloSpawnCommand implements CommandExecutor {
    private InputStream inputstream;

    public SiloSpawnCommand(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();

            InputStreamReader streamReader = new InputStreamReader(inputstream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            try {
                Schematic schematic = WorldEditor.getInstance().loadSchematic(new File(SILOSCHEMATICPATH));

                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    String[] split = line.split(",");
                    int x = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    int y = world.getHighestBlockYAt(x, z);

                    Location currentLocation = new Location(world, x, y, z);
                    try {
                        WorldEditor.getInstance().placeSchematic(world, currentLocation, schematic);
                        player.chat("Placed silo at X: " + x + " Y: " + y + " Z: " + z);
                    } catch (NullPointerException ignored) {

                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
