package be.mrtibo.queuer;

import be.mrtibo.queuer.config.Configuration;
import be.mrtibo.queuer.queues.QueueManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Queuer extends JavaPlugin {

    private static Plugin plugin;
    private static QueueManager manager;
    private static Configuration config;

    @Override
    public void onEnable() {
        plugin = this;
        config = new Configuration(this);
        manager = new QueueManager();
        manager.loadSavedQueues();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.onShutdown();
        plugin = null;
        manager = null;
        config = null;
    }

    public static Plugin getInstance() { return plugin; }
    public static QueueManager getManager() { return manager; }
    public static Configuration getConfiguration() { return config; }
}
