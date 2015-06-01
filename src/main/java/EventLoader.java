import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


/**
 * Created by darryl on 26-5-15.
 */
abstract public class EventLoader extends JavaPlugin implements Listener {
    protected Logger logger;

    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}