package com.brosintime.rts.View.Screen;

import com.googlecode.lanterna.input.KeyStroke;

/**
 * A target of focus by a {@link FocusManager}. Classes that implement this interface are permitted
 * to a method each for when it gains focus and when it loses focus. They are also permitted to
 * process keystrokes from the player.
 * <p>Generally, an abstract class is used to implement {@link #addFocus} and {@link #removeFocus}
 * to call their subclasses’ {@link #onFocus()} and {@link #offFocus()} methods respectively.
 * <p>This interface should be referenced as the <i>type</i> for object creation and is required by
 * {@link FocusManager} unless extended. This is to give targets the general ability to be focused
 * on and to process player keystrokes via {@link #onKeyPress}.
 *
 * @see Screen
 */
public interface Focusable {

    /**
     * Called when a {@link FocusManager} focuses on this object. Add any methods in here that
     * should be called when this happens, like moving a window or frame to the top or animating a
     * blinking cursor.
     */
    void onFocus();

    /**
     * Called when a {@link FocusManager} loses focus on this object. Add any methods in here that
     * should be called when this happens, like dulling colors or stopping animations.
     */
    void offFocus();

    /**
     * Generally implemented by a super class to call on a subclass’ {@link #onFocus()} method. This
     * method should be called by a {@link FocusManager}. The focus manager is passed in as an
     * argument so it can be referenced by the implementing class and is ultimately returned.
     *
     * @see Screen
     */
    void onKeyPress(KeyStroke key);
}
