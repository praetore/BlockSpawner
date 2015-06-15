package plugin.testcommands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.Schematic;
import plugin.WorldEditor;

import java.io.File;
import java.io.IOException;

/**
 * Created by darryl on 16-6-15.
 */
public class SchematicTestCommand implements CommandExecutor {
    private String filepath;

    public SchematicTestCommand(String path) {
        this.filepath = path;
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
                File file = new File(filepath);
                Schematic building = WorldEditor.getInstance().loadSchematic(file);
                WorldEditor.getInstance().placeSchematic(world, location, building);
            } catch (IOException e) {
                return false;
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
