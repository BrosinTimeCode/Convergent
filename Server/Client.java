package Server;

import Server.NetworkMessages.NetworkMessage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    Socket clientSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public Client() {
    }

    public void run() {
        try {
            clientSocket = new Socket("localhost", 1234);
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendToServer(NetworkMessage message) {
        try {
            outputStream.write(message.getMessageString().getBytes());
            outputStream.flush();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}
