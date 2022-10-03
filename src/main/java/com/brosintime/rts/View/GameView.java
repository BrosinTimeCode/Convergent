package com.brosintime.rts.View;

import com.googlecode.lanterna.input.KeyStroke;
import java.util.Map;

/**
 * The GameViewInterface interface follows the view design in the MVC design pattern. This class
 * handles player input and sends information and commands to the controller.
 */
public interface GameView {

    /**
     * Displays the current state of the board.
     */
    void displayBoard();

    /**
     * Displays a notification to the player that the provided command is invalid.
     */
    void displayInvalidCommand();

    /**
     * Retrieves a player’s keystroke off the input queue.
     *
     * @return the player’s first key press
     */
    KeyStroke getPlayerKey();

    /**
     * Displays the chat history.
     */
    void displayConsoleLog();

    /**
     * Displays the player’s current input buffer.
     *
     * @param input the input buffer
     */
    void displayInput(String input);

    /**
     * Clears the player’s current input buffer.
     */
    void clearInput();

    /**
     * Retrieves the current height of the chat history box.
     *
     * @return the chat box height
     */
    int getConsoleLogHeight();

    /**
     * Flushes the display render buffer to the player’s screen.
     */
    void flush();

    /**
     * Blanks out the player’s screen.
     */
    void clear();

    /**
     * Displays a screen populated with game system statistics for debugging.
     *
     * @param debugInfo the map of statistics
     */
    void renderDebugScreen(Map<String, Integer> debugInfo);

    /**
     * Displays the valid player controls for the currently focused frame.
     */
    void displayControls();

    /**
     * Erases the player controls from the screen.
     */
    void clearControls();

    /**
     * Sets the player controls string to the provided string.
     *
     * @param string the new controls to ultimately render
     */
    void setControlsString(String string);
}
