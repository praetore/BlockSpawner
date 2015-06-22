package plugin.testcommands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import plugin.WorldEditor;

/**
 * Created by darryl on 16-6-15.
 */
public class SchematicTestCommand implements CommandExecutor {
    private Plugin plugin;

    public SchematicTestCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
            Location location = player.getLocation();
            location.setX(location.getX() + 2);
            location.setY(location.getY());
            try {
                new WorldEditor(plugin).place(world, location, "Meerpaal");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}