package xyz.anana.pinecommons.commands;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import xyz.anana.pinecommons.Pine;
import xyz.anana.pinecommons.PineCommons;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
@RequiredArgsConstructor
public class CommandHandler implements Listener {

    @NonNull
    private PineCommons pineCommons;
    private List<Method> commands = new ArrayList<>();

    /**
     * Register a class who contains methods with the {@link Command} annotation.
     *
     * @param object The class who contains methods with the {@link Command} annotation.
     */
    public void registerCommand(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(Command.class) == null)
                continue;

            Command command = method.getAnnotation(Command.class);

            //Try to get the plugin command, if it failed returns.
            PluginCommand pluginCommand;
            try {
                Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                c.setAccessible(true);
                pluginCommand = c.newInstance(command.commandLabel(), pineCommons);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
                return;
            }

            //Try to get the simple command map, if it failed returns.
            SimpleCommandMap map;
            try {
                if (pineCommons.getServer().getPluginManager() instanceof SimplePluginManager) {
                    Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                    f.setAccessible(true);
                    map = (SimpleCommandMap) f.get(Bukkit.getPluginManager());
                } else return;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
                return;
            }
            map.register("", pluginCommand);

            commands.add(method);

            Bukkit.getLogger().log(Level.INFO, Pine.format("The ${1} command have been registered.", method.getName()));
        }
    }

    /**
     * Register classes who contains methods with the {@link Command} annotation.
     *
     * @param objects Classes who contains methods with the {@link Command} annotation.
     */
    public void registerCommands(Object... objects) {
        for (Object object : objects)
            registerCommand(object);
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {
        for (Method command : commands) {
            Command commandAnnotation = command.getAnnotation(Command.class);

            String entryLabel = e.getMessage().split(" ")[0].replace("/", "");
            String[] entryArgs = e.getMessage().replace(entryLabel, "").split(" ");

            if (entryArgs.length < commandAnnotation.minimumArgs())
                return;

            if (entryLabel.equals(commandAnnotation.commandLabel()))
                try {
                    command.invoke(null, e.getPlayer(), entryArgs);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, Pine.format("Couldn't invoke ${1} method.", command.getName()));
                }
        }
    }

}
