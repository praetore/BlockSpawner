package plugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.testcommands.PlacementTestCommand;
import plugin.testcommands.SchematicTestCommand;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    @Override
    public void onEnable() {
        CommandExecutor spawnCommand = new SpawnCommand(this);
        getCommand("placebuildings").setExecutor(spawnCommand);
        getCommand("testplacement").setExecutor(new PlacementTestCommand());
        getCommand("testschematic").setExecutor(new SchematicTestCommand());
    }
}