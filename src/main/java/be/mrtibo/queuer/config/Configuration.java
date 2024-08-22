package be.mrtibo.queuer.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Configuration {

    private Plugin plugin;
    public static HashMap<String, String> config = new HashMap<>();

    public Configuration(Plugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    public void loadConfig() {
        config.clear();
        loadString("joinqueue",
                "leavequeue",
                "alreadyqueued",
                "queueposition",
                "queuepositionauto",
                "playerturn",
                "queuefull",
                "attentionsound"
        );
    }

    private void loadString(String... path) {
        for (String p : path) {
            loadString(p);
        }
    }

    private void loadString(String path) {
        FileConfiguration c = plugin.getConfig();
        config.put(path, c.getString(path));
    }

}
