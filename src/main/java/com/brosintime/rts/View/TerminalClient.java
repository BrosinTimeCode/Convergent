package com.brosintime.rts.View;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.Screen.DebugScreen;
import com.brosintime.rts.View.Screen.Drawable;
import com.brosintime.rts.View.Screen.FocusManager;
import com.brosintime.rts.View.Screen.Focusable;
import com.brosintime.rts.View.Screen.Screen;
import com.brosintime.rts.View.Screen.TitleScreen;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;

public class TerminalClient implements GameView {

    private final TerminalRenderer renderer;
    private final GameController controller;
    private final FocusManager focusManager;
    private final int width;
    private final int height;
    private Drawable screenAncestor;
    private final Player player;
    private DebugScreen debugScreen;

    public TerminalClient(GameController controller, Player player, int width, int height) {

        if (controller == null) {
            throw new IllegalArgumentException("This client has no game engine");
        }
        if (player == null) {
            throw new IllegalArgumentException("This client has no player");
        }
        this.controller = controller;
        this.player = player;
        this.width = width;
        this.height = height;
        this.renderer = new TerminalRenderer(this, this.width, this.height);
        this.focusManager = new FocusManager();
        this.screenAncestor = null;

    }

    @Override
    public void setScreen(Drawable screen) {
        if (screen != null) {
            if (this.screenAncestor != null) {
                if (!this.screenAncestor.equals(screen.firstAncestor())) {
                    this.screenAncestor.close();
                }
            }
            this.screenAncestor = screen.firstAncestor();
            this.focusManager.focus((Focusable) screen);
        }
    }

    @Override
    public void processPlayerKeys() {
        try {
            KeyStroke keyStroke = this.renderer.pollInput();
            while (keyStroke != null) {
                this.focusManager.target().onKeyPress(keyStroke);
                keyStroke = this.renderer.pollInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        this.renderer.clear();
    }

    @Override
    public void renderScreen() {
        this.renderer.putScreen(this.screenAncestor);
        this.renderer.flush();
    }

    @Override
    public void titleScreen() {
        Screen titleScreen = new TitleScreen(this.controller, this);
        this.setScreen(titleScreen);
        this.focusManager.focus(titleScreen);
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public Player player() {
        return this.player;
    }

    @Override
    public void setFocus(Focusable target) {
        if (target != null) {
            this.focusManager.focus(target);
        }
    }

    @Override
    public void toggleDebugScreen() {
        if (this.debugScreen.visible()) {
            this.debugScreen.hide();
        } else {
            this.debugScreen.show();
        }
    }

    @Override
    public DebugScreen debugScreen() {
        return this.debugScreen;
    }

    @Override
    public void setDebugScreen(DebugScreen debugScreen) {
        this.debugScreen = debugScreen;
    }

}
