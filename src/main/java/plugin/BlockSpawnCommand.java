package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by darryl on 5-6-15.
 */
public class BlockSpawnCommand implements CommandExecutor{
    private final String PAALSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" + File.separator +
            "schematics" + File.separator + "paal.schematic";

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

            WorldEditor worldEditor = WorldEditor.getInstance();

            Schematic PAALSCHEMATIC;
            try {
                PAALSCHEMATIC = WorldEditor.getInstance().loadSchematic(new File(PAALSCHEMATICPATH));
            } catch (IOException e) {
                return false;
            }

            for (String line : GeoLoader.getData()) {
                split = line.split(";");
                y = 256;
                x = Integer.parseInt(split[1]);
                z = Integer.parseInt(split[2]);
                type = Integer.parseInt(split[0]);

                if ((type == 2 || type == 4)) {
                    Location placedAt = worldEditor.placeSchematic(world, new Location(world, x, y, z), PAALSCHEMATIC);
                    String msg = new StringBuilder()
                            .append("Meerpaal geplaatst op X:")
                            .append(placedAt.getX())
                            .append(" Y:")
                            .append(placedAt.getY())
                            .append(" Z:")
                            .append(placedAt.getZ())
                            .toString();

                    player.chat(msg);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}