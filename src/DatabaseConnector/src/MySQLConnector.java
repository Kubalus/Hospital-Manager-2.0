package DatabaseConnector.src;

import sample.Encoder;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.Date;

public class MySQLConnector {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/hospitalManager";

    static final String USER = "root";
    static final String PASS = "root";





    public static String login(String login, String password) throws DataFormatException{
        Connection conn = null;
        Statement stmt = null;
        String goodPass = "";
        String accesslvl = "failed";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String getPassword;
            String letter = login.substring(0,1);
            if(Objects.equals(letter, "P") || Objects.equals(letter, "D"))
                getPassword = ("SELECT Password FROM users WHERE Login = \'" + login + "\'");
            else
                throw new DataFormatException() ;

            String getLvl = ("SELECT Access_lvl FROM users WHERE Login = \'" + login + "\'");
            ResultSet rs = stmt.executeQuery(getPassword);
;

            while(rs.next()){

                goodPass  = rs.getString("Password");
                System.out.println("pass : " + password);
            }
            rs.close();


            ResultSet rs2 = stmt.executeQuery(getLvl);

            while(rs2.next()){

                accesslvl = rs2.getString("Access_lvl");
                System.out.println("lvl : " + accesslvl);
            }


            rs2.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        Encoder encode = new Encoder();

        boolean check = false;
        try{
            check = encode.validatePassword(password,goodPass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (check == true)
            return accesslvl;
        else
            return "failed";

    };

    public static String newPatientData(String name, String PESEL, Date date, String password ){
        Connection conn = null;
        Statement stmt = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String addPatient = ("CALL addNewPatientData(\"" + name + "\", \"" + PESEL +"\", \"" +
                    date + "\", \"" + password + "\")");

            stmt.executeQuery(addPatient);

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

       return "dodano pacjenta";

    }

    public static String patientDiseases(String patient){
        Connection conn = null;
        Statement stmt = null;
        String result = "";
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Disease_Name, Description, Date FROM DiseasesHistory INNER JOIN diseasesType " +
                    "ON DiseasesHistory.Disease = diseasesType.ID WHERE DiseasesHistory.Disease IN " +
                    "(SELECT ID FROM diseases WHERE diseases.Patient = \'" + patient + "\' )";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                String disease  = rs.getString("Disease_Name");
                String description = rs.getString("Description");
                String date = rs.getString("Date");
                result = result + disease + "&" + description + "&" + date + "#";

                System.out.print("Disease : " + disease);
                System.out.print(", Description : " + description);
                System.out.println(", Date : " + date);
            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public static String patientName(String patient){
        Connection conn = null;
        Statement stmt = null;
        String result = "";
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Full_Name FROM patients WHERE PESEL = " + patient;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                String full_name  = rs.getString("Full_Name");

                System.out.println("Patient Name : " + full_name);
                result = full_name;
            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public static String doctorName(String doctor){
        Connection conn = null;
        Statement stmt = null;
        String result = "";
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Full_Name FROM doctors WHERE PESEL = " + doctor;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                String full_name  = rs.getString("Full_Name");

                System.out.println("Doctor Name : " + full_name);
                result = full_name;
            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }

    public static String doctorPatients(String doctor){

        Connection conn = null;
        Statement stmt = null;
        String result = "";
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT patients.Full_Name as patient, Disease_Name, diseases.ID, patients.PESEL as pesel, " +
                    "doctors.Full_Name as doctor FROM diseases " +
                    "JOIN patients ON diseases.Patient = patients.PESEL " +
                    "JOIN diseasesType ON diseases.Diseases_Type = diseasesType.ID " +
                    "JOIN doctors ON diseases.diagnosis_doctor = doctors.PESEL " +
                    " WHERE State = \'PROCESSED\' AND Leading_Doctor = \'" + doctor + " \' ";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                String patient_name  = rs.getString("patient");
                String disease_name = rs.getString("Disease_Name");
                String pesel = rs.getString("pesel");
                String diag_doctor = rs.getString("doctor");
                String disease = rs.getString("diseases.ID");
                String sql2 = "SELECT Date, Description FROM diseaseshistory WHERE diseaseshistory.Disease = " + disease;
                ResultSet rs2 = stmt.executeQuery(sql2);
                String notes = "";
                while(rs2.next()){
                    String date_note = rs2.getString("Date");
                    String desc = rs2.getString("Description");
                    notes  = notes + date_note + "$" + desc + "@";
                }
                rs2.close();
                result = result + patient_name + "&" + disease_name + "&" + pesel + "&"
                        + diag_doctor + "&" + notes + "#";
            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("result : " + result);
        return result;

    }

    public static void main(String[] args) {
//        Connection conn = null;
//        Statement stmt = null;
//        try{
//
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(DB_URL,USER,PASS);
//            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT Full_Name, PESEL, Birth_Date FROM patients";
//            ResultSet rs = stmt.executeQuery(sql);
//
//            while(rs.next()){
//
//                String name  = rs.getString("Full_Name");
//                String PESEL = rs.getString("PESEL");
//                String date = rs.getString("Birth_Date");
//
//                System.out.print("Name : " + name);
//                System.out.print(", PESEL : " + PESEL);
//                System.out.println(", Date : " + date);
//            }
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//
//            se.printStackTrace();
//        }catch(Exception e){
//
//            e.printStackTrace();
//        }finally{
//
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }
//            try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//
//

        Encoder coder = new Encoder();
        try {
            String passs= coder.encrypt("kot", "97011012050");
            System.out.println(passs);
            System.out.println("P97011012050".substring(0,1));
            System.out.println(login("P97011012050",
                    "1000:3937303131303132303530:9eb49c0b6e62ec4899776374e7014633e57e8963ed1ee88f"));
        }catch(Exception ex){
            System.out.println("exception : " + ex);
        }
    }
}