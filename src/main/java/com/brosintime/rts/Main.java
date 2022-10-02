package com.brosintime.rts;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.Server.PeerToPeerClient;
import com.brosintime.rts.Server.PeerToPeerHost;
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
        Client client = null;
        if (args.length >= 3 && args[2].equals("server")) {
            client = new PeerToPeerHost();
            client.start();
        } else if (args.length >= 3 && args[2].equals("client")) {
            client = new PeerToPeerClient();
            client.start();
        }
        GameController controller = new GameController(client, -1, BoardType.SEEDEDRANDOM, width,
            height);
        controller.run();
    }
}
