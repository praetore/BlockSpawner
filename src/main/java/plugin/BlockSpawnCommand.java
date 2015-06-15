package plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

/**
 * Created by darryl on 5-6-15.
 */
public class BlockSpawnCommand implements CommandExecutor{
    private final String PAALSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" + File.separator +
            "schematics" + File.separator + "paal.schematic";
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

            Schematic PAALSCHEMATIC;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            try {
                PAALSCHEMATIC = WorldEditor.getInstance().loadSchematic(new File(PAALSCHEMATICPATH));
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    String[] split = line.split(";");
                    int y = 256;
                    int x = Integer.parseInt(split[1]);
                    int z = Integer.parseInt(split[2]);
                    int type = Integer.parseInt(split[0]);

                    String msg = null;
                    Location placedAt;
                    Location currentLocation = new Location(world, x, y, z);
                    switch (type) {
                        case 1:
                            placedAt = worldEditor.placeBlocks(world, currentLocation, 1, Material.ANVIL);
                            msg = new StringBuilder()
                                    .append("Bolder geplaatst op X:")
                                    .append(placedAt.getX())
                                    .append(" Y:")
                                    .append(placedAt.getY())
                                    .append(" Z:")
                                    .append(placedAt.getZ())
                                    .toString();
                            break;
                        case 2:
                        case 4:
                            placedAt = worldEditor.placeSchematic(world,
                                    currentLocation, PAALSCHEMATIC);
                            msg = new StringBuilder()
                                    .append("Meerpaal geplaatst op X:")
                                    .append(placedAt.getX())
                                    .append(" Y:")
                                    .append(placedAt.getY())
                                    .append(" Z:")
                                    .append(placedAt.getZ())
                                    .toString();
                            break;
                    }
                    player.chat(msg);
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