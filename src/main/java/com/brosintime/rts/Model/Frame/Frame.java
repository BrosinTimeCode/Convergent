package com.brosintime.rts.Model.Frame;

/**
 * A frame represents a target eligible for focus. Extending this class gives subclasses the ability
 * to have their {@link #onFocus()} and {@link #offFocus()} methods called automatically when they
 * receive or lose focus from a {@link FocusManager} respectively.
 */
public abstract class Frame implements Focusable {

    @Override
    public final FocusManager addFocus(FocusManager manager) {
        if (!manager.hasFocusOn(this)) {
            onFocus();
        }
        return manager;
    }

    @Override
    public final FocusManager removeFocus(FocusManager manager) {
        if (manager.hasFocusOn(this)) {
            offFocus();
        }
        return manager;
    }
}
