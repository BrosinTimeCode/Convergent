package com.brosintime.rts.Server;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.TestBoard;
import com.brosintime.rts.Model.TestBoard.BoardType;
import com.brosintime.rts.Server.NetworkMessages.BoardMessage;
import com.brosintime.rts.Server.NetworkMessages.NETWORK_MESSAGE_TYPE;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalHostServer extends Thread {

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    Board board;

    private void setup() throws IOException {
        serverSocket = new ServerSocket(1234);
        board = new TestBoard(BoardType.SEEDEDRANDOM, 15, 15);
    }

    @Override
    public void run() {
        try {
            setup();
            socket = serverSocket.accept();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                byte[] message = new byte[1000];
                inputStream.read(message);
                String stringMessage = new String(message).trim();
                System.out.println(board);
                parseMessage(stringMessage);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void parseMessage(String message) {
        NETWORK_MESSAGE_TYPE messageType = NETWORK_MESSAGE_TYPE.fromValue(Integer.parseInt(message.split(":")[0]));
        switch (messageType) {
            case MOVE -> {

            }
            case BOARD -> {
                //TODO: Change remove from server. Currently here for test purposes to allow client to send initial board.
                BoardMessage boardMessage = new BoardMessage(message);
                this.board = boardMessage.board;
            }
        }
    }

    public static void main(String[] args) {
        LocalHostServer server = new LocalHostServer();
        server.start();
    }
}
