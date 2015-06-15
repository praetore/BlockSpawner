package plugin;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.testcommands.PlacementTestCommand;
import plugin.testcommands.SchematicTestCommand;

import static plugin.ResourceFiles.DATAPOINTS;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    @Override
    public void onEnable() {
        BlockSpawnCommand spawnCommand = new BlockSpawnCommand(getResource(DATAPOINTS));
        getCommand("placeblocks").setExecutor(spawnCommand);
        getCommand("testplacement").setExecutor(new PlacementTestCommand());
        getCommand("testschematic").setExecutor(new SchematicTestCommand());
    }
}