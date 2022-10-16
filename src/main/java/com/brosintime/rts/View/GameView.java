package com.brosintime.rts.View;

import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.Screen.DebugScreen;
import com.brosintime.rts.View.Screen.Drawable;
import com.brosintime.rts.View.Screen.Focusable;

/**
 * The GameViewInterface interface follows the view design in the MVC design pattern. This class
 * handles player input and sends information and commands to the controller.
 */
public interface GameView {

    /**
     * Sets the screen to the grand-most parent of the provided screen and sets focus to the
     * provided screen. The screen is set to the ancestor instead to display the entire tree.
     *
     * @param screen the screen to display and focus
     */
    void setScreen(Drawable screen);

    /**
     * Retrieves the player’s keystrokes off the input queue and sends them to the current screen in
     * focus.
     */
    void processPlayerKeys();

    /**
     * Blanks out the player’s screen.
     */
    void clear();

    /**
     * Renders the current screen tree to the game client.
     */
    void renderScreen();

    /**
     * Launches, displays, and sets focus to the title screen.
     */
    void titleScreen();

    /**
     * Returns the width of the game client in columns.
     * @return the width of the client
     */
    int width();

    /**
     * Returns the height of the game client in rows.
     * @return the height of the client
     */
    int height();

    /**
     * Returns the player that owns this game client.
     * @return this client’s player
     */
    Player player();

    /**
     * Sets focus to a new target.
     * @param target the target to focus
     */
    void setFocus(Focusable target);

    /**
     * Toggles rendering of the debugging info screen.
     */
    void toggleDebugScreen();

    /**
     * Returns the debugging info screen’s instance.
     * @return the debug screen
     */
    DebugScreen debugScreen();

    /**
     * Sets this game client’s debugging screen to the provided debug screen.
     * @param debugScreen the new debug screen to set
     */
    void setDebugScreen(DebugScreen debugScreen);
}
