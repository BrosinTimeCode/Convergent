package com.brosintime.rts.Model.Frame;

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
