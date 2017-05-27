package xyz.anana.pinecommons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xyz.anana.pinecommons.commands.CommandHandler;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pine {

    @Getter
    @Setter(value = AccessLevel.PROTECTED)
    private static PineCommons pineCommons;
    @Getter
    private static Pine pine = new Pine(new CommandHandler(pineCommons));

    private CommandHandler commandHandler;

    /**
     * Allows to format a message with values to replace.
     *
     * @param message The message to format.
     * @param format  The values to replace.
     * @return The formatted message.
     */
    public static String format(String message, String... format) {
        String messageToLog = "[pine-commons] " + message;
        for (int i = 0; i < format.length; i++)
            messageToLog = messageToLog.replace("${" + i + "}", format[i]);

        return messageToLog;
    }

}
