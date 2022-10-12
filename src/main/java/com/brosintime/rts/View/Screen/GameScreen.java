package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;

public class GameScreen extends Screen {

    private final Drawable boardScreen;

    public GameScreen(GameController controller, GameView client) {
        if (controller == null) {
            throw new IllegalArgumentException("This screen has no game engine");
        }
        if (client == null) {
            throw new IllegalArgumentException("This screen has no game client");
        }
        this.controller = controller;
        this.client = client;
        this.origin = new Node(0, 0);
        this.width = this.client.width();
        this.height = this.client.height();
        new DebugScreen(this.controller, this.client).asChild(this).asHidden();
        this.boardScreen = new BoardScreen(this.controller, this.client).asChild(this);
    }

    @Override
    public void onFocus() {
        this.client.setFocus((Focusable) this.boardScreen);
    }

    @Override
    public void offFocus() {

    }

    @Override
    public void onKeyPress(KeyStroke key) {

    }

    @Override
    public void onRender() {

    }
}
