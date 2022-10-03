package com.brosintime.rts.Model.Frame;

public class FocusManager {

    private Focusable target = null;

    public void focus(Focusable target) {
        if (!hasFocusOn(target)) {
            target.addFocus(this);
            this.target = target;
            this.target.removeFocus(this);
        }
    }

    public boolean hasFocusOn(Focusable target) {
        return this.target == target;
    }

    public Focusable target() {
        return this.target;
    }
}
