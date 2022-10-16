package com.brosintime.rts;

import com.brosintime.rts.Controller.GameController;

public class Main {

    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.run();
        System.exit(0);
    }
}
