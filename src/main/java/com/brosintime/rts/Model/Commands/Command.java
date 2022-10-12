package com.brosintime.rts.Model.Commands;

import java.util.List;

/**
 * A command registrable in {@link CommandList}.
 *
 * <p>Every command implementing this interface should be a singleton instance with static
 * fields and a public static {@code #instance()} method that retrieves said instance.
 */
public interface Command extends CommandList {

    /**
     * Defines the result of {@link #validateArguments()}:
     * <ul>
     * <li>{@link #GOOD}: arguments are in valid format (i.e. parsable as integers)
     * <li>{@link #TOOMANY}: too many arguments given
     * <li>{@link #NOARGS}: no arguments given
     * <li>{@link #BAD}: arguments are in invalid format
     * </ul>
     */
    enum ArgStatus {
        GOOD, TOOMANY, NOARGS, BAD
    }

    /**
     * Retrieves the list of arguments that were assigned to the command by
     * {@link #setArguments(List)}.
     *
     * @return a list of arguments
     */
    List<String> arguments();

    /**
     * Clears any previously set arguments and sets new arguments.
     *
     * @param args list of arguments
     */
    void setArguments(List<String> args);

    /**
     * Retrieves a list of usages for the command. Used as part of the player’s help documentation.
     *
     * @return a list of usages (e.g. {@code "(x) (y)"} for a command that accepts two arguments)
     */
    List<String> usages();

    /**
     * Retrieves the name of the command, starting with a capital letter. Used as part of the
     * player’s help documentation.
     *
     * @return the name of the command
     */
    String name();

    /**
     * Retrieves the preferred alias for calling the command. Should be present in the list of the
     * command’s aliases. Used as part of the player’s help documentation.
     *
     * @return the default (preferred) alias
     */
    String defaultAlias();

    /**
     * Retrieves a full example of one usage of the command. Used as part of the player’s help
     * documentation.
     *
     * @return a string representing a fully typed command
     */
    String basicUsage();

    /**
     * Retrieves a description of the command. Used as part of the player’s help documentation.
     *
     * @return a description of the command
     */
    String description();

    /**
     * Checks if the size of the list of arguments exceeds the accepted count.
     *
     * @return if argument count exceeds acceptable count
     */
    boolean hasTooManyArguments();

    /**
     * Checks if the command’s arguments are valid based on what the command accepts and returns
     * {@link ArgStatus} result. Examples include:
     * <ul>
     * <li>arguments are parsable as integers
     * <li>too many arguments
     * <li>arguments are in wrong format
     * </ul>
     *
     * @return {@link ArgStatus} result
     */
    ArgStatus validateArguments();

    /**
     * Retrieves one of the command’s arguments by the given index.
     *
     * @param index of argument in list
     * @return argument by index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    String argument(int index) throws IndexOutOfBoundsException;

    /**
     * Retrieves the maximum acceptable count of arguments set by the command.
     *
     * @return maximum argument count
     */
    byte maxArguments();

}
