/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class AddNewAppointmentController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;

    @FXML
    private TextField amount_box;
    @FXML
    private TextArea notes_box;

    @FXML
    private ChoiceBox<?> choicebox_Name;
 @FXML
    private ChoiceBox choicebox_customer;

    @FXML
    private TextField date_time_box;
    //String Student_id = "";

    String orderby;
    String ascdesc;
    ObservableList Products;
    ObservableList Customers;
    ObservableList Grades;

    @FXML
    private TextField invoiceNo_box;


//    String[] arrayIndexStore ;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDateTime();
        refreshItemListInCombo();
        selectFirstOne();
    }

    @FXML
    private void onClickClose(ActionEvent event) throws IOException {
        Parent clpage_parent = FXMLLoader.load(getClass().getResource("SalesLine.fxml"));
        Scene page_scene = new Scene(clpage_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(page_scene);
        app_stage.show();
    }

    private void loadDateTime() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String strDateInFormat = dateFormat.format(cal.getTime());
        System.out.println("strDateInFormat is: " + strDateInFormat);
        date_time_box.setText(strDateInFormat);

    }

    //code for combo starts
    private void refreshItemListInCombo() {
        //Set items equal to an empty ArrayList
        Products = FXCollections.observableArrayList();
        Customers = FXCollections.observableArrayList();

        //Select out of the DB, fill accordingly
        getProducts(Products);
        getAllCustomers(Customers);
       // getAllGrades(Grades);

        //Set the listview to what we just populated with DB contents
        choicebox_Name.setItems(Products);
        choicebox_customer.setItems(Customers);

    }

    private void getProducts(ObservableList Products) {
        Connection c = null;
        Statement stmt = null;

        //orderby = sort_menubutton.getText();
        // ascdesc = ascdesc_menubutton.getText();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            c.setAutoCommit(false);
            System.out.println("Opened database getStudents successfully");

            //if (orderby.equals("Time")) { orderby = "TIMING";}
            //else if (orderby.equals("Title")) { orderby = "TITLE";}
            //else { orderby = "LOCATION";}
            String orderby = "id";
            System.out.println("Query is: SELECT * FROM Products_tbl" + " ORDER BY " + orderby);
            stmt = c.createStatement();
            System.out.println("after stmt");

            ResultSet rs = stmt.executeQuery("SELECT * FROM Products_tbl" + " ORDER BY " + orderby);
            Products.add("--SELECT ONE--");
            int arrayIndexCounter = 0;
            ArrayList<String> arrayIndexStore = new ArrayList<String>();
            System.out.println("before while");

            while (rs.next()) {
                String name = rs.getString("name");

                arrayIndexCounter++;
                //IMPORTANT STATEMENT HERE:
                Products.add(name);
            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    @FXML
    private boolean onAddButtonClicked(ActionEvent event) {
        //insert into db if valid
        Date date = new Date();
        String theDate = date.toString();

        String product_name = choicebox_Name.getValue().toString();
        String amount = amount_box.getText();
        String notes = notes_box.getText();
        String invoiceNo = invoiceNo_box.getText();
        String customer = choicebox_customer.getValue().toString();

        if ((!amount.matches("[0-9]*")) || (amount.trim().length() == 0) || (amount == "") || (amount.trim().isEmpty())) {

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Information Dialog");
            alert1.setHeaderText(null);
            alert1.setContentText("AMOUNT has to be numeric.");

            alert1.showAndWait();
            //System.clearProperty(str_cost);
            amount_box.setText("0");
            return false;

        }

        if ((product_name.trim().length() == 0) || (product_name == "") || (product_name.trim().isEmpty()) || (product_name == "--SELECT ONE--")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a product.");

            alert.showAndWait();
            System.clearProperty(product_name);
            return false;
        }

       
        product_name = product_name.toUpperCase();
        notes = notes.toUpperCase();

        String txnMonth = theDate.substring(4, 7);
        String txnYear = theDate.substring(24, 28);
        String txnDay = theDate.substring(0, 10) + " " + txnYear;
        //format for report queries
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String strDateInFormat = dateFormat.format(cal.getTime());
        System.out.println("strDateInFormat is: " + strDateInFormat);

        String query = "INSERT INTO Appointment_tbl (appointment_name,customer,amount,notes,txnDay,txnMonth,txnYear,appointment_date"
                + ",invoiceNo,date_time ) VALUES ("
                + "'" + product_name + "'," + "'" + customer + "'," + "'" + amount + "'," + "'"
                + notes + "','" + txnDay + "'," + "'" + txnMonth + "'," + "'" + txnYear + "'," + "'" + strDateInFormat + "'," + "'"
                + invoiceNo + "',"  + "'"
                + theDate + "');";

        System.out.println("Inserting\n" + query);
        insertStatement(query);

        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record Added Succesfully.");

        alert3.showAndWait();

        System.out.println("Succesfully Inserted");

        choicebox_customer.getSelectionModel().clearSelection();

        amount_box.clear();
        notes_box.clear();
        choicebox_Name.getSelectionModel().clearSelection();
        invoiceNo_box.clear();

        loadDateTime();
        selectFirstOne();
        return true;

    }

    private void insertStatement(String insert_query) {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            System.out.println("Our query was: " + insert_query);
            stmt.executeUpdate(insert_query);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    @FXML
    private void onClearButtonClicked(ActionEvent event) throws IOException {

        amount_box.clear();
        notes_box.clear();
        choicebox_Name.getSelectionModel().clearSelection();
        invoiceNo_box.clear();
    }

    private void selectFirstOne() {
        choicebox_Name.getSelectionModel().selectFirst();
        choicebox_customer.getSelectionModel().selectFirst();
    }

    private String getSelectedStudentId() {
        System.out.println("inside getSelectedStudentId");
        Connection c = null;
        Statement stmt = null;
        String selectedStudentId = "";
        int choiceIndex = choicebox_Name.getSelectionModel().getSelectedIndex();

        System.out.println("Student choiceIndex is: " + choiceIndex);

        //orderby = sort_menubutton.getText();
        // ascdesc = ascdesc_menubutton.getText();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            //if (orderby.equals("Time")) { orderby = "TIMING";}
            //else if (orderby.equals("Title")) { orderby = "TITLE";}
            //else { orderby = "LOCATION";}
            String orderby = "Student_id";
            System.out.println("Query is: SELECT * FROM Student_tbl WHERE Student_id = " + choiceIndex + " ORDER BY " + orderby);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Student_tbl WHERE Student_id = " + choiceIndex + " ORDER BY " + orderby);

            int arrayIndexCounter = 0;
            while (rs.next()) {

                String Student_id = rs.getString("Student_id");
                System.out.println("Student_id = " + Student_id);
                selectedStudentId = Student_id;

            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return selectedStudentId;

    }

    private void getAllCustomers(ObservableList Customers) {
        Connection c = null;
        Statement stmt = null;

        //orderby = sort_menubutton.getText();
        // ascdesc = ascdesc_menubutton.getText();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            c.setAutoCommit(false);
            System.out.println("Opened database getStudents successfully");

            //if (orderby.equals("Time")) { orderby = "TIMING";}
            //else if (orderby.equals("Title")) { orderby = "TITLE";}
            //else { orderby = "LOCATION";}
            String orderby = "id";
            System.out.println("Query is: SELECT * FROM Customers_tbl" + " ORDER BY " + orderby);
            stmt = c.createStatement();
            System.out.println("after stmt");

            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers_tbl" + " ORDER BY " + orderby);
            Customers.add("--NONE SELECTED--");
            int arrayIndexCounter = 0;
            ArrayList<String> arrayIndexStore = new ArrayList<String>();
            System.out.println("before while");

            while (rs.next()) {
                String name = rs.getString("name");

                arrayIndexCounter++;
                //IMPORTANT STATEMENT HERE:
                Customers.add(name);
            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

   

}
