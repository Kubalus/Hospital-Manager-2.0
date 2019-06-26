package sample;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServerConnector {


    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private static ServerConnector INSTANCE;

    private ServerConnector() {
        connectToServer();
    }

    //SINGLETON
    public static ServerConnector getINSTANCE(){
        if (INSTANCE == null)
            INSTANCE = new ServerConnector();

        return INSTANCE;
    }

    public void connectToServer(){
        try {
            socket = new Socket("localhost", 6008);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost"); System.exit(1);
        }

        catch  (IOException e) {
            System.out.println("No I/O"); //System.exit(1);
        }
    }

    /**
     *
     * @param message wiadomość od klienta którą funkcja wysyła serwerowi
     * @return serverResponse odpowiedz servera na wyslaną wiadomość
     */

    public String sendInformation(String message){
        String serverResponse = null;

        out.println(message);

        try {
            serverResponse = in.readLine();
            System.out.println(serverResponse);
        }

        catch (IOException e) {
            System.out.println("Server doesn't respond!"); System.exit(1);
        }

        return serverResponse;
    }

    public String signIn(String login, String password)
    {
        String PESEL = login.substring(1);
        String HashedPassword ="";
        try {
            HashedPassword = Encoder.encrypt(password, PESEL);
            System.out.println(HashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println("Wysyłam: " + "login#" + login + "&" + HashedPassword);
      //  System.out.println("1000:3937303131303132303530:9eb49c0b6e62ec4899776374e7014633e57e8963ed1ee88f");
        String response = sendInformation("login#" + login + "&" + HashedPassword);
        if(response != "failed")
            Controller.setUser(new User(PESEL));
        return response;

    }

    public String getPatientDiseases(String PESEL)
    {
        String response = sendInformation("diseases#" + PESEL);
        return response;

    }

    public String getPatientName(String PESEL){
        String response = sendInformation("Pname#" + PESEL);
        return response;
    }

    public String getDoctorName(String PESEL){
        String response = sendInformation("Dname#" + PESEL);
        return response;
    }

    public String getPatients(String PESEL){
        String response = sendInformation("patients#" + PESEL);
        return response;
    }

    public String[][] createTable(String rawData)
    {

        int rowCount = rawData.length() - rawData.replace("#", "").length();
        if(rowCount == 0)
            return new String[0][0];
        int columnCount = (rawData.length() - rawData.replace("&", "").length())/rowCount + 1;

        String[] rows = rawData.split("#");
        String[][] result = new String[rowCount][columnCount];

        for(int i=0;i<rowCount;i++)
        {
            String[] row = rows[i].split("&");
            for(int j=0;j<columnCount;j++)
            {
                result[i][j] = row[j];
            }
        }

        return result;
    }

//    public void addLog()
}