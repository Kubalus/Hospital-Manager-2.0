package DatabaseConnector.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static final int PORT = 6008;
    private static ServerSocket serverSocket;
    private static ClientThread client;


    public static void main(String[] args) throws IOException {


        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);


        }catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }

            client = new ClientThread(socket);
            client.start();
        }
    }

}