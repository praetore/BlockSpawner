package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
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
    private java.io.InputStream inputstream;

    public SiloSpawnCommand(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            try {
                Material bolder = Material.ANVIL;
                Schematic paal = WorldEditor.getInstance().loadSchematic(new File(SILOSCHEMATICPATH));

                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    String[] split = line.split(",");
                    int x = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    int y = world.getHighestBlockYAt(x, z);

                    Location currentLocation = new Location(world, x, y, z);
                    WorldEditor.getInstance().placeSchematic(world, currentLocation, paal);
                    player.chat("Placed " + "silo " + "at " + "X: " + x + " Y: " + y + " Z: " + z);
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
