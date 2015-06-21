package plugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.testcommands.PlacementTestCommand;
import plugin.testcommands.SchematicTestCommand;

import static plugin.ResourceFiles.DATAPOINTS;
import static plugin.ResourceFiles.SILOPOINTS;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandExecutor blockSpawnCommand = new BlockSpawnCommand(getResource(DATAPOINTS));
        getCommand("placeblocks").setExecutor(blockSpawnCommand);
        CommandExecutor siloSpawnCommand = new SiloSpawnCommand(getResource(SILOPOINTS));
        getCommand("placesilos").setExecutor(siloSpawnCommand);
        getCommand("testplacement").setExecutor(new PlacementTestCommand());
        getCommand("testschematic").setExecutor(new SchematicTestCommand());
    }
}