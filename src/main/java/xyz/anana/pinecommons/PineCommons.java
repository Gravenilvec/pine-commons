package xyz.anana.pinecommons;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.anana.pinecommons.commands.Command;
import xyz.anana.pinecommons.commands.CommandHandler;
import xyz.anana.pinecommons.inventory.InventoryListener;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
public class PineCommons extends JavaPlugin {

    @Getter
    private static Logger pineLogger = Logger.getLogger("PineCommons");
    @Getter
    private static CommandHandler commandHandler;

    @Override
    public void onEnable() {
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommand(this);
        register(commandHandler, new InventoryListener());
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

    @Command(label = "test")
    public void test(Player p, String... args) {
        p.sendMessage("This is a test command");
    }

}
