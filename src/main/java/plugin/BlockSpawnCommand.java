package plugin;

import org.bukkit.Location;
import org.bukkit.World;
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

            WorldEditor worldEditor = WorldEditor.getInstance();
            for (String line : GeoLoader.getData()) {
                split = line.split(";");
                y = 0;
                x = Integer.parseInt(split[1]);
                z = Integer.parseInt(split[2]);
                type = Integer.parseInt(split[0]);

                if (type == 2 || type == 4) {
                    worldEditor.placeBlocks(world, new Location(world, x, y, z));
                    String msg = "Meerpaal geplaatst";
                    player.chat(msg);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}