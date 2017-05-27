package xyz.anana.pinecommons;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.anana.pinecommons.commands.Command;
import xyz.anana.pinecommons.commands.CommandBase;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
public class PineCommons extends JavaPlugin {

    @Getter
    private static Logger pineLogger = Logger.getLogger("PineCommons");
    @Getter
    private static CommandBase commandBase;

    @Override
    public void onEnable() {
        commandBase = new CommandBase(this);
        commandBase.registerCommand(this);
        register(commandBase);
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
