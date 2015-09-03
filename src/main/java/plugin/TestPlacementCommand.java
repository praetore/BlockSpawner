package plugin;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by darryl on 3-9-15.
 */
public class TestPlacementCommand implements CommandExecutor {
    private PluginManager plugin;

    public TestPlacementCommand(PluginManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Block block = player.getTargetBlock(null, 100);
            Location locationBlock = block.getLocation();
            String choice = args[0];
            if (choice != null) {
                String schematic = (String) plugin.SCHEMATICS.keySet().toArray()[Integer.parseInt(choice)];
                WorldEditor.getInstance(plugin)
                        .place(player.getWorld(),
                                locationBlock,
                                schematic
                        );
                return true;
            } else {
                player.chat("You must choose by picking a number from a schematic from /availablebuildings");
                return false;
            }
        }
        return false;
    }
}
