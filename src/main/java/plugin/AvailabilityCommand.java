package plugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Created by darryl on 23-6-15.
 */
public class AvailabilityCommand implements CommandExecutor {
    private PluginManager plugin;

    public AvailabilityCommand(PluginManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Map<String, Schematic> schematics = plugin.SCHEMATICS;
            int count = 0;
            for (Map.Entry<String, Schematic> entry : schematics.entrySet()) {
                player.chat(count + ": " + entry.getKey() + ": " + entry.getValue().getFilename());
                count++;
            }

            Map<String, Material> blocks = plugin.BLOCKS;
            for (Map.Entry<String, Material> entry : blocks.entrySet())
                player.chat(entry.getKey() + ": " + entry.getValue().name());

            player.chat(plugin.POINTS.size() + " points indexed");
            return true;
        } else {
            return false;
        }
    }
}
