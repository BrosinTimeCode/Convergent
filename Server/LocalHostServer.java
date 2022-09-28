package Server;

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

    private void setup() throws IOException {
        serverSocket = new ServerSocket(1234);
    }

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
                System.out.println(stringMessage);
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
