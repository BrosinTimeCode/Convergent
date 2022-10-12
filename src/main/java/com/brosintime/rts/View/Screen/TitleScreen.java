package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Model.TestBoard;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.View.Cell;
import com.brosintime.rts.View.GameView;
import com.brosintime.rts.View.Screen.Menu.MenuStyle;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.HashMap;
import java.util.Map;

public class TitleScreen extends Screen {

    private final Menu menu;
    private final String title;
    private final Node titleOrigin;

    public TitleScreen(GameController controller, GameView client) {

        if (controller == null) {
            throw new IllegalArgumentException("This screen has no game engine");
        }
        if (client == null) {
            throw new IllegalArgumentException("This screen has no game client");
        }
        this.controller = controller;
        this.client = client;
        this.origin = new Node(0, 0);
        this.width = client.width();
        this.height = client.height();

        this.title = ColorCode.GREEN.fgColor()
        /*
           .aMMMb  .aMMMb  dMMMMb  dMP dMP dMMMMMP dMMMMb  .aMMMMP dMMMMMP dMMMMb dMMMMMMP
          dMP"VMP dMP"dMP dMP dMP dMP dMP dMP     dMP.dMP dMP"    dMP     dMP dMP   dMP
         dMP     dMP dMP dMP dMP dMP dMP dMMMP   dMMMMK" dMP MMP"dMMMP   dMP dMP   dMP
        dMP.aMP dMP.aMP dMP dMP  YMvAP" dMP     dMP"AMF dMP.dMP dMP     dMP dMP   dMP
        VMMMP"  VMMMP" dMP dMP    VP"  dMMMMMP dMP dMP  VMMMP" dMMMMMP dMP dMP   dMP
         */
            + "   .aMMMb  .aMMMb  dMMMMb  dMP dMP dMMMMMP dMMMMb  .aMMMMP dMMMMMP dMMMMb dMMMMMMP\n"
            + "  dMP\"VMP dMP\"dMP dMP dMP dMP dMP dMP     dMP.dMP dMP\"    dMP     dMP dMP   dMP\n"
            + " dMP     dMP dMP dMP dMP dMP dMP dMMMP   dMMMMK\" dMP MMP\"dMMMP   dMP dMP   dMP\n"
            + "dMP.aMP dMP.aMP dMP dMP  YMvAP\" dMP     dMP\"AMF dMP.dMP dMP     dMP dMP   dMP\n"
            + "VMMMP\"  VMMMP\" dMP dMP    VP\"  dMMMMMP dMP dMP  VMMMP\" dMMMMMP dMP dMP   dMP";

        this.titleOrigin = Node.relativeTo(Drawable.originCenteredHorizontallyOn(
            Drawable.fromString(this.title, this.origin, true), this), 8, 0);

        Node menuLocation = Node.relativeTo(this.origin, 37, 8);
        this.menu = new Menu(this.client, menuLocation, MenuStyle.TITLE_SCREEN);
        this.menu.setParent(this);
        this.menu.add("SKIRMISH");
        this.menu.add("MULTIPLAYER");
        this.menu.add("EXIT GAME");

    }

    @Override
    public void onFocus() {
        this.menu.show();
    }

    @Override
    public void offFocus() {
        this.menu.hide();
    }

    @Override
    public void onKeyPress(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowUp -> this.menu.previous();
            case ArrowDown -> this.menu.next();
            case Enter -> {
                switch (this.menu.getSelectedOption()) {
                    case "SKIRMISH" -> this.skirmishScreen();
                    case "EXIT GAME" -> this.controller.exit();
                }
            }
            case Escape -> this.controller.exit();
        }
    }

    private void skirmishScreen() {

        Node origin = Node.relativeTo(this.origin, 17, 7);
        Drawable skirmish = new SkirmishOptions(this.controller, this.client, origin);
        skirmish.setParent(this);
        this.client.setScreen(skirmish);

    }

    @Override
    public void onRender() {
        Map<Node, Cell> cells = Drawable.fromString(this.title, this.titleOrigin, true);
        this.screen.putAll(cells);
    }

    private static class SkirmishOptions extends Screen {

        private final Menu menu;
        private final Map<String, Drawable> boards = new HashMap<>();

        private SkirmishOptions(GameController controller, GameView client, Node origin) {

            if (controller == null) {
                throw new IllegalArgumentException("This screen has no game engine");
            }
            if (client == null) {
                throw new IllegalArgumentException("This screen has no game client");
            }
            this.controller = controller;
            this.client = client;
            this.origin = origin != null ? origin : new Node(0, 0);
            this.width = 82;
            this.height = 25;
            Node menuLocation = Node.relativeTo(this.origin, 2, 2);
            this.menu = new Menu(this.client, menuLocation, MenuStyle.TITLE_SCREEN);
            this.menu.setParent(this);
            int boardWidth = 23;
            int boardHeight = 23;
            Node boardOrigin = Node.relativeTo(this.origin, 1, 58);
            this.menu.add("ONE VS ONE");
            this.boards.put("ONE VS ONE", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.MONOVSMONO, boardHeight, boardWidth)).asChild(this));
            this.menu.add("EMPTY");
            this.boards.put("EMPTY", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.EMPTY, boardHeight, boardWidth)).asChild(this).asHidden());
            this.menu.add("RANDOM");
            this.boards.put("RANDOM", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.RANDOM, boardHeight, boardWidth)).asChild(this).asHidden());
            this.menu.add("LONE SURVIVOR");
            this.boards.put("LONE SURVIVOR", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.LONESURVIVOR, boardHeight, boardWidth)).asChild(this)
                .asHidden());
            this.menu.add("ONE VS EVERYONE");
            this.boards.put("ONE VS EVERYONE", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.ONEVSEVERYONE, boardHeight, boardWidth)).asChild(this)
                .asHidden());
            this.menu.add("ONE VS CHECKERBOARD");
            this.boards.put("ONE VS CHECKERBOARD", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.ONEVSCHECKERBOARD, boardHeight, boardWidth)).asChild(this)
                .asHidden());
            this.menu.add("RANDOM BUT SEEDED");
            this.boards.put("RANDOM BUT SEEDED", new ExampleBoardScreen(boardOrigin,
                new TestBoard(BoardType.SEEDEDRANDOM, boardHeight, boardWidth)).asChild(this)
                .asHidden());

        }

        @Override
        public void onFocus() {

        }

        @Override
        public void offFocus() {

        }

        @Override
        public void onKeyPress(KeyStroke key) {
            switch (key.getKeyType()) {
                case ArrowUp -> {
                    if (this.menu.hasPrevious()) {
                        this.boards.get(this.menu.getSelectedOption()).hide();
                        this.menu.previous();
                        this.boards.get(this.menu.getSelectedOption()).show();
                    }
                }
                case ArrowDown -> {
                    if (this.menu.hasNext()) {
                        this.boards.get(this.menu.getSelectedOption()).hide();
                        this.menu.next();
                        this.boards.get(this.menu.getSelectedOption()).show();
                    }
                }
                case Enter -> {
                    switch (this.menu.getSelectedOption()) {
                        case "ONE VS ONE" -> this.controller.newSkirmish(BoardType.MONOVSMONO);
                        case "EMPTY" -> this.controller.newSkirmish(BoardType.EMPTY);
                        case "RANDOM" -> this.controller.newSkirmish(BoardType.RANDOM);
                        case "LONE SURVIVOR" -> this.controller.newSkirmish(BoardType.LONESURVIVOR);
                        case "ONE VS EVERYONE" ->
                            this.controller.newSkirmish(BoardType.ONEVSEVERYONE);
                        case "ONE VS CHECKERBOARD" ->
                            this.controller.newSkirmish(BoardType.ONEVSCHECKERBOARD);
                        case "RANDOM BUT SEEDED" ->
                            this.controller.newSkirmish(BoardType.SEEDEDRANDOM);
                    }
                }
                case Escape -> this.close();
            }
        }

        @Override
        public void onRender() {

        }
    }

}
