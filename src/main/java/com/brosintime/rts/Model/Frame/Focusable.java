package com.brosintime.rts.Model.Frame;

import com.googlecode.lanterna.input.KeyStroke;

public interface Focusable {

    void onFocus();

    void offFocus();

    FocusManager addFocus(FocusManager manager);

    FocusManager removeFocus(FocusManager manager);

    void executeKey(KeyStroke key);
}
