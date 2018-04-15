/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Eche Michael
 */
public class SalesLine extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        checkProducts_tbl();
        checkAppointment_tbl();
        checkCustomers_tbl();
        checkExpAmount_tbl();
        checkIncomeAmount_tbl();
        createDownloadsDir();

        Parent root = FXMLLoader.load(getClass().getResource("SalesLine.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void checkAppointment_tbl() {

        String sqlCreateExpItems = "CREATE TABLE IF NOT EXISTS Appointment_tbl ("
                + "appointment_id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + "appointment_date	TEXT,"
                + "appointment_name	TEXT,"
                + "amount	TEXT,"
                + "notes	TEXT,"
                + "txnDay	TEXT,"
                + "txnMonth	TEXT,"
                + "txnYear	TEXT,"
                + "invoiceNo	TEXT,"
                + "customer	TEXT,"
                + "date_time	TEXT"
                + ")";

        // String CleanExpTbl = "DELETE FROM expItems WHERE itemName = \"\"";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlCreateExpItems);
            //stmt.execute(CleanExpTbl);
//		conn.commit();
//		conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void checkusersTables() {
        String sqlDropUsers = "drop table if exists users";
        String sqlCreateUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "username	TEXT,"
                + "password	TEXT,"
                + "PRIMARY KEY(username)"
                + ")";
        String sqlInsertUsers = "INSERT INTO users (username,password) VALUES ('admin','admin')";
        System.out.println(sqlInsertUsers);
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlDropUsers);
            stmt.execute(sqlCreateUsers);
            stmt.execute(sqlInsertUsers);
            stmt.close();
            //conn.commit();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void checkExpAmount_tbl() {
        String sqlCreateExpItems = "CREATE TABLE IF NOT EXISTS ExpAmount_tbl ("
                + "amount_id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + "amount_name	TEXT,"
                + "amount_details	TEXT,"
                + "amount_date	TEXT,"
                + "txnDay	TEXT,"
                + "txnMonth	TEXT,"
                + "txnYear	TEXT,"
                + "formated_date	TEXT,"
                + "amount	TEXT"
                + ")";

        // String CleanExpTbl = "DELETE FROM expItems WHERE itemName = \"\"";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlCreateExpItems);
            //stmt.execute(CleanExpTbl);
//		conn.commit();
//		conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    private void checkIncomeAmount_tbl() {
        String sqlCreateExpItems = "CREATE TABLE IF NOT EXISTS IncomeAmount_tbl ("
                + "amount_id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + "amount_name	TEXT,"
                + "amount_details	TEXT,"
                + "amount_date	TEXT,"
                + "txnDay	TEXT,"
                + "txnMonth	TEXT,"
                + "txnYear	TEXT,"
                + "formated_date	TEXT,"
                + "amount	TEXT"
                + ")";

        // String CleanExpTbl = "DELETE FROM expItems WHERE itemName = \"\"";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlCreateExpItems);
            //stmt.execute(CleanExpTbl);
//		conn.commit();
//		conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    private void checkProducts_tbl() {
        String sqlCreateExpItems = "CREATE TABLE IF NOT EXISTS Products_tbl ("
                + "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + "name	TEXT,"
                + "description	TEXT,"
                + "product_date	TEXT,"
                + "quantity	TEXT,"
                + "units	TEXT,"
                + "expiry_date	TEXT,"
                + "formated_date	TEXT,"
                + "price	TEXT"
                + ")";

        // String CleanExpTbl = "DELETE FROM expItems WHERE itemName = \"\"";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlCreateExpItems);
            //stmt.execute(CleanExpTbl);
//		conn.commit();
//		conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void checkCustomers_tbl() {
        String sqlCreateExpItems = "CREATE TABLE IF NOT EXISTS Customers_tbl ("
                + "id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + "name	TEXT,"
                + "description	TEXT,"
                + "address	TEXT,"
                + "phone	TEXT,"
                + "email	TEXT,"
                + "customer_date	TEXT,"
                + "type	TEXT"
                + ")";

        // String CleanExpTbl = "DELETE FROM expItems WHERE itemName = \"\"";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            stmt.execute(sqlCreateExpItems);
            //stmt.execute(CleanExpTbl);
//		conn.commit();
//		conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createDownloadsDir() {
        try {
            String directoryName = "C:\\Downloads\\";
            directoryName = directoryName.replace("\\", "/");
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
                System.out.println("Directory does not exist");
                System.out.println("Directory has been created");
            } else {
                System.out.println("Directory existe");
                System.out.println("Doing nothing");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
