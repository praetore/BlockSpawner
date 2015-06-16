package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

import static plugin.ResourceFiles.PAALSCHEMATICPATH;

/**
 * Created by darryl on 5-6-15.
 */
public class BlockSpawnCommand implements CommandExecutor{
    private final InputStream inputstream;

    public BlockSpawnCommand(InputStream stream) {
        this.inputstream = stream;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
            WorldEditor worldEditor = WorldEditor.getInstance();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            try {
                Material bolder = Material.ANVIL;
                Schematic paal = WorldEditor.getInstance().loadSchematic(new File(PAALSCHEMATICPATH));

                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    String[] split = line.split(",");
                    int y = 256;
                    int x = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    int type = Integer.parseInt(split[0]);

                    Location currentLocation = new Location(world, x, y, z);
					StringBuilder sb = new StringBuilder().
						append("Placed ");
                    switch (type) {
                        case 1:
                            worldEditor.getInstance().placeBlocks(world, currentLocation, 1, bolder);
							sb.append("Bolder ");
                            break;
                        case 4:
                            worldEditor.getInstance().placeSchematic(world, currentLocation, paal);
							sb.append("Paal ");
                            break;
                    }
					sb.append("at ").append("X: ").append(x).append(" Z: ").append(z);
					player.chat(sb.toString());
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
