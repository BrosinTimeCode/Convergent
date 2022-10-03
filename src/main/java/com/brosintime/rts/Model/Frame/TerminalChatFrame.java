package com.brosintime.rts.Model.Frame;

import static com.brosintime.rts.Controller.PlayerInputHistory.charListToString;
import static com.brosintime.rts.Controller.PlayerInputHistory.stringToCharList;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Controller.PlayerInputHistory;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.ArrayList;
import java.util.List;

public class TerminalChatFrame extends ChatFrame {

    private final GameView client;
    private final GameController controller;
    private final PlayerInputHistory inputHistory;

    private final List<Character> input;

    public TerminalChatFrame(GameView client, GameController controller) {
        this.client = client;
        this.controller = controller;
        this.inputHistory = new PlayerInputHistory();
        this.input = new ArrayList<>();
    }

    @Override
    public void onFocus() {
        this.client.clearControls();
        this.client.setControlsString("");
    }

    @Override
    public void offFocus() {

    }

    @Override
    public void executeKey(KeyStroke key) {

        switch (key.getKeyType()) {
            case Escape -> {
                this.input.clear();
                this.client.clearInput();
                this.controller.focusBoard();
            }
            case Character -> {
                if (this.input.size() < 80) {
                    this.input.add(key.getCharacter());
                }
            }
            case Enter -> {
                this.inputHistory.add(charListToString(this.input));
                this.controller.handleUserInput(charListToString(this.input));
                this.input.clear();
                this.controller.focusBoard();
            }
            case Backspace -> {
                if (this.input.size() >= 1) {
                    this.input.remove(this.input.size() - 1);
                }
                this.client.clearInput();
            }
            case ArrowDown -> {
                this.input.clear();
                this.client.clearInput();
                this.input.addAll(stringToCharList(this.inputHistory.next()));
            }
            case ArrowUp -> {
                this.input.clear();
                this.client.clearInput();
                this.input.addAll(stringToCharList(this.inputHistory.previous()));
            }
            case F3 -> this.controller.toggleDebugScreen();
        }
    }

    @Override
    public List<Character> getInput() {
        return this.input;
    }

}
