package xyz.anana.pinecommons.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Set the label command. example: /help -> help
     *
     * @return The label command.
     */
    String label();

    /**
     * Set the minimum number of arguments to execute the content of the command.
     *
     * @return The minimum number of arguments, default to 0.
     */
    int minimumArgs() default 0;

}
