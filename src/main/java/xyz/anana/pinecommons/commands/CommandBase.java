package xyz.anana.pinecommons.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
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
public class CommandBase implements Listener {

    private PineCommons pineCommons;
    private List<Method> commands;

    public CommandBase(PineCommons pineCommons) {
        this.pineCommons = pineCommons;
        this.commands = new ArrayList<>();
    }

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

            PluginCommand pluginCommand = getCommand(command.label());
            SimpleCommandMap map = (SimpleCommandMap) getCommandMap();
            map.register("", pluginCommand);

            commands.add(method);

            PineCommons.getPineLogger().log(Level.INFO, String.format("The %s command have been registered.", method.getName()));
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

    /**
     * Retrieves the command map.
     *
     * @return The command map.
     */
    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (pineCommons.getServer().getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            PineCommons.getPineLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return commandMap;
    }

    /**
     * Retrieves the bukkit command from the name.
     *
     * @param name The command name.
     * @return The bukkit command.
     */
    private PluginCommand getCommand(String name) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = c.newInstance(name, pineCommons);
        } catch (Exception e) {
            PineCommons.getPineLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return command;
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {
        for (Method command : commands) {
            Command commandAnnotation = command.getAnnotation(Command.class);

            String entryLabel = e.getMessage().split(" ")[0].replace("/", "");
            String[] entryArgs = e.getMessage().replace(entryLabel, "").split(" ");

            if (entryLabel.equals(commandAnnotation.label()))
                try {
                    command.invoke(null, e.getPlayer(), entryArgs);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    PineCommons.getPineLogger().log(Level.SEVERE, String.format("Couldn't invoke %s method.", command.getName()), ex);
                }
        }
    }

}
