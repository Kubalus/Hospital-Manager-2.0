package DatabaseConnector.src;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class ClientThread extends Thread {

    protected Socket socket;
    private InputStream inp = null;
    private BufferedReader in = null;
    private PrintWriter out = null;





    public ClientThread(Socket clientSocket) {
        this.socket = clientSocket;

    }

    public void run() {

        try {

            inp = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream(), true);
            //out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection denied");
        }

        String messageClient;

        while (true) {
            try {

                messageClient = in.readLine();
                System.out.println("Przetwarzam aktualnie : " + messageClient);
                handleInfoFromClient(messageClient);

            } catch (IOException e) {
                System.out.println("Cannot connect null message");
                System.exit(-1);
            }
        }
    }


    public void handleInfoFromClient(String messageClient) {

        String[] dataArray = null;
        String[] parameterArray;
        System.out.println("handling -> " + messageClient);
        dataArray = messageClient.split("#");

        if ("login".equals(dataArray[0])) {
            parameterArray = dataArray[1].split("&");
            if(parameterArray.length == 2) {
                try {
                    out.println(MySQLConnector.login(parameterArray[0], parameterArray[1]));
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }
            }
            else
                out.println("failed");


        }
        if ("diseases".equals(dataArray[0])) {
            parameterArray = dataArray[1].split("$");
            if(parameterArray.length == 1) {
                out.println(MySQLConnector.patientDiseases(parameterArray[0]));
            }
            else
                out.println("failed");


        }
        if ("Pname".equals(dataArray[0])) {
            parameterArray = dataArray[1].split("$");
            if(parameterArray.length == 1) {
                out.println(MySQLConnector.patientName(parameterArray[0]));
            }
            else
                out.println("failed");


        }
        if ("Dname".equals(dataArray[0])) {
            parameterArray = dataArray[1].split("$");
            if(parameterArray.length == 1) {
                out.println(MySQLConnector.doctorName(parameterArray[0]));
            }
            else
                out.println("failed");


        }

        if ("patients".equals(dataArray[0])) {
            parameterArray = dataArray[1].split("$");
            if(parameterArray.length == 1) {
                out.println(MySQLConnector.doctorPatients(parameterArray[0]));
            }
            else
                out.println("failed");


        }
    }


        //checking the action which client expect and return

}