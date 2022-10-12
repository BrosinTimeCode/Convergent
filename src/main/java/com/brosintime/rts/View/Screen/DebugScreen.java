package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.Map;

/**
 * A debug screen is a screen with the sole purpose of displaying debugging info it receives from
 * the game controller. This class is not intended to be given focus and will set focus to its
 * parent when this happens.
 */
public class DebugScreen extends Screen {

    /**
     * Constructs a new debug screen. If the provided controller or client is null, an
     * {@link IllegalArgumentException} is thrown.
     *
     * @param controller the game controller to interface with
     * @param client     the game client to interface with
     */
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

    /**
     * Updates the debugging info displayed with the provided map of info.
     *
     * @param debugInfo the map of debug information
     */
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
