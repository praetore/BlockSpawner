package plugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
                    Location location = player.getLocation();
                    World world = location.getWorld();
                    world.spawnEntity(new Location(world, location.getX(), location.getY() + 5, location.getZ()), EntityType.COW);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}