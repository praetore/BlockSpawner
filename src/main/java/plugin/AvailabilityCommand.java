package plugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by darryl on 23-6-15.
 */
public class AvailabilityCommand implements CommandExecutor {
    private final Logger logger;
    private PluginManager plugin;

    public AvailabilityCommand(PluginManager plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Map<String, Schematic> schematics = plugin.SCHEMATICS;
        for (Map.Entry<String, Schematic> entry : schematics.entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue().getFilename());
        }

        Map<String, Material> blocks = plugin.BLOCKS;
        for (Map.Entry<String, Material> entry : blocks.entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue().name());
        }
        return true;
    }
}
