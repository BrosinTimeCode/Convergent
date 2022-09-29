package com.brosintime.rts.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalHostServer extends Thread {

    ServerSocket serverSocket;
    Socket socket;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;

    private void setup() throws IOException {
        serverSocket = new ServerSocket(1234);
    }

    public void run() {
        try {
            setup();
            socket = serverSocket.accept();
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            while (true) {
                System.out.println(bufferedReader.readLine());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LocalHostServer server = new LocalHostServer();
        server.start();
    }
}
