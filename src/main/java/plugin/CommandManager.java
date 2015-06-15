package plugin;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.testcommands.PlacementTestCommand;
import plugin.testcommands.SchematicTestCommand;

import java.io.File;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    private final String PAALSCHEMATICPATH = "plugins" + File.separator + "WorldEdit" + File.separator +
            "schematics" + File.separator + "paal.schematic";

    @Override
    public void onEnable() {
        BlockSpawnCommand spawnCommand = new BlockSpawnCommand(getResource("datapoints.csv"));
        getCommand("placeblocks").setExecutor(spawnCommand);
        getCommand("testplacement").setExecutor(new PlacementTestCommand());
        getCommand("testschematic").setExecutor(new SchematicTestCommand(PAALSCHEMATICPATH));
    }
}