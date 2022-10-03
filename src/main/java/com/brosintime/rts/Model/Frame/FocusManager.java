package com.brosintime.rts.Model.Frame;

/**
 * The {@code FocusManager} class manages the focus of the player’s cursor. As an object, it keeps
 * track of one {@link Focusable} target at a time, which can be switched out for another.
 */
public class FocusManager {

    private Focusable target = null;

    /**
     * Changes the current target of focus to a new target, if different. Nothing happens if you
     * focus on a target already in focus. If the new target is different, the new target’s
     * {@link Focusable#onFocus()} method is called and the old target’s
     * {@link Focusable#offFocus()} is too.
     *
     * @param target the target to give focus
     */
    public void focus(Focusable target) {
        if (!hasFocusOn(target)) {
            target.addFocus(this);
            this.target = target;
            this.target.removeFocus(this);
        }
    }

    /**
     * Determines if the given target is in focus by this manager.
     *
     * @param target the target to check if it has focus
     * @return {@code true} if target is in focus, {@code false} if not
     */
    public boolean hasFocusOn(Focusable target) {
        return this.target == target;
    }

    /**
     * Retrieves the target currently in focus by this manager.
     *
     * @return target in focus
     */
    public Focusable target() {
        return this.target;
    }
}
