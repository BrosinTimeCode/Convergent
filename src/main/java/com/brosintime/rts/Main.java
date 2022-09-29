package com.brosintime.rts;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.Server.Client;

public class Main {

    public static void main(String[] args) {
        int width;
        int height;
        if (args.length >= 2) {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } else {
            width = 0;
            height = 0;
        }
        GameController controller = new GameController(-1, BoardType.RANDOM, width, height);
        Client client = new Client();
        client.start();
        controller.initialize();
        while (true) {
            controller.getUserInput();
        }
    }
}
