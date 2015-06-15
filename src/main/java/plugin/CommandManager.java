package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    private final Logger logger;
    private final String PAALSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" + File.separator +
            "schematics" + File.separator + "paal.schematic";

    public CommandManager() {
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        BlockSpawnCommand spawnCommand = new BlockSpawnCommand();
        getCommand("placeblocks").setExecutor(spawnCommand);
        getCommand("testplacement").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    World world = player.getWorld();
                    Location location = player.getLocation();
                    location.setX(location.getX() + 2);
                    WorldEditor.getInstance().placeBlocks(world, location);
                    return true;
                } else {
                    logger.severe("Placing blocks has failed");
                    return false;
                }
            }
        });
        getCommand("testschematic").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    World world = player.getWorld();
                    Location location = player.getLocation();
                    location.setX(location.getX() + 2);
                    location.setY(location.getY());
                    try {
                        File file = new File(PAALSCHEMATICPATH);
                        Schematic building = WorldEditor.getInstance().loadSchematic(file);
                        WorldEditor.getInstance().placeSchematic(world, location, building);
                    } catch (IOException e) {
                        logger.severe("Schematic file can not be opened");
                        return false;
                    } catch (Exception e) {
                        logger.severe("Something else went wrong");
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}