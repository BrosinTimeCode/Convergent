package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.Map;

public class DebugScreen extends Screen {

    public DebugScreen(GameController controller, GameView client) {
        if (controller == null) {
            throw new IllegalArgumentException("This screen has no game engine");
        }
        if (client == null) {
            throw new IllegalArgumentException("This screen has no game client");
        }
        this.controller = controller;
        this.client = client;
        this.origin = new Node(this.client.height() - 1, 0);
        this.client.setDebugScreen(this);
        this.height = 1;
        this.width = this.firstAncestor().columns();
    }

    public void renderDebugInfo(Map<String, Integer> debugInfo) {
        this.screen.putAll(Drawable.blankRow(this, 0));
        String debugString = "FPS: " + debugInfo.get("fps") + " TPS: " + debugInfo.get("tps");
        this.width = Math.max(this.width, debugString.length());
        this.screen.putAll(Drawable.fromString(debugString, this.origin, true));
    }

    @Override
    public void onRender() {

    }

    @Override
    public void onFocus() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void offFocus() {

    }

    @Override
    public void onKeyPress(KeyStroke key) {

    }
}
