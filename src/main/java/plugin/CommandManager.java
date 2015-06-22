package plugin;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.testcommands.PlacementTestCommand;
import plugin.testcommands.SchematicTestCommand;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("placebuildings").setExecutor(new SpawnCommand(this));
        getCommand("testplacement").setExecutor(new PlacementTestCommand(this));
        getCommand("testschematic").setExecutor(new SchematicTestCommand(this));
    }
}