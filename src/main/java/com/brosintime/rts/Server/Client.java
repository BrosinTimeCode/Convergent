package com.brosintime.rts.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client extends Thread {

    Socket clientSocket;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    OutputStreamWriter outputStreamWriter;
    BufferedWriter bufferedWriter;

    public Client() {
    }

    public void run() {
        try {
            clientSocket = new Socket("localhost", 1234);
            inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            while (true) {
                bufferedWriter.write("hello world");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
