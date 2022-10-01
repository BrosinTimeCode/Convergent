package com.brosintime.rts.Server;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Server.NetworkMessages.MoveMessage;
import com.brosintime.rts.Server.NetworkMessages.NETWORK_MESSAGE_TYPE;
import com.brosintime.rts.Server.NetworkMessages.NetworkMessage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Client extends Thread {

    DataInputStream inputStream;
    DataOutputStream outputStream;

    GameController controller;

    /**
     * Sends a message on the network attached to the outputstream.
     *
     * @param message Message to be sent out.
     * @return A boolean indicating success of sending message.
     */
    public boolean sendMessage(NetworkMessage message) {
        try {
            outputStream.write(message.messageString().getBytes());
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Parses incoming messages and has the gamecontroller update the board.
     *
     * @param message Message to be parsed.
     */
    protected void parseMessage(String message) {
        NETWORK_MESSAGE_TYPE messageType = NETWORK_MESSAGE_TYPE.fromValue(
            Integer.parseInt(message.split(":")[0]));
        switch (messageType) {
            case MOVE -> {
                MoveMessage moveMessage = new MoveMessage(message);
                controller.receiveMove(moveMessage.unitID, moveMessage.targetID,
                    moveMessage.xCoordinate, moveMessage.yCoordinate);

            }
        }
    }

    /**
     * Receives messages coming in on inputstream. Parses message passed in.
     */
    protected void receiveMessage() {
        while (true) {
            byte[] message = new byte[1000];
            try {
                inputStream.read(message);
                String stringMessage = new String(message).trim();
                System.out.println(stringMessage);
                parseMessage(stringMessage);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }
}
