package plugin.testcommands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.WorldEditor;

/**
 * Created by darryl on 16-6-15.
 */
public class PlacementTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
            Location location = player.getLocation();
            location.setX(location.getX() + 2);
            WorldEditor.getInstance().placeBlocks(world, location, 1, Material.ANVIL);
            return true;
        } else {
            return false;
        }
    }
}
