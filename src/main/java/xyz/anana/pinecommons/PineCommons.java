package xyz.anana.pinecommons;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
public class PineCommons extends JavaPlugin {

    @Override
    public void onEnable() {
        Pine.setPineCommons(this);

        register(Pine.getPine().getCommandHandler());
    }

    /**
     * Register bukkit events.
     *
     * @param listeners Bukkit events.
     */
    private void register(Listener... listeners) {
        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, this);
    }

}
