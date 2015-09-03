package plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by darryl on 3-9-15.
 */
public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (LocationIndexer.getInstance().getLocationSize() > 0) {
                LocationIndexer.getInstance().clearLocations();
            } else {
                player.chat("No schematics or blocks have been placed during this session");
            }
            return true;
        }
        return false;
    }
}
