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

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
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
                    location.setY(location.getY() + 1);
                    try {
                        Schematic building = WorldEditor.getInstance()
                                .loadSchematic(new File(getClass().getResource("/paal.schematic").getFile()));
                        WorldEditor.getInstance().placeSchematic(world, location, building);
                    } catch (IOException e) {
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        getCommand("filepresent").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    int available;
                    try {
                        available = getResource("paal.schematic").available();
                    } catch (IOException e) {
                        available = -1;
                        player.chat("File doesn't exists");
                    } catch (Exception e) {
                        available = -1;
                        player.chat("Something else went wrong");
                    }
                    player.chat("File existence: " + available);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}