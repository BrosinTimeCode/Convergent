package com.brosintime.rts.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PeerToPeerClient extends Client {

    Socket clientSocket;

    public PeerToPeerClient() {
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("localhost", 1234);
            super.inputStream = new DataInputStream(clientSocket.getInputStream());
            super.outputStream = new DataOutputStream(clientSocket.getOutputStream());
            receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
