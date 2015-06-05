package plugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by darryl on 5-6-15.
 */
public class CommandManager extends JavaPlugin {
    @Override
    public void onEnable() {
        BlockSpawnCommand spawnCommand = new BlockSpawnCommand();
        getCommand("placeblocks").setExecutor(spawnCommand);
    }
}