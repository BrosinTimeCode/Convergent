package com.brosintime.rts.Server;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.TestBoard;
import com.brosintime.rts.Model.TestBoard.BoardType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerToPeerHost extends Client {

    ServerSocket serverSocket;
    Socket socket;
    Board board;

    GameController controller;

    private void setup() throws IOException {
        serverSocket = new ServerSocket(1234);
        board = new TestBoard(BoardType.SEEDEDRANDOM, 15, 15);
    }

    @Override
    public void run() {
        try {
            setup();
            socket = serverSocket.accept();
            super.inputStream = new DataInputStream(socket.getInputStream());
            super.outputStream = new DataOutputStream(socket.getOutputStream());
            receiveMessage();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
