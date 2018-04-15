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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class SpentMoneyController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private ChoiceBox choicebox_name;
    @FXML
    private TextArea description_box;
    @FXML
    private TextField cost_box;
    // private TextField units_box;
    @FXML
    private TextField date_time_box;
    @FXML
    private TextField quantity_box;

    String orderby;
    String ascdesc;
    ObservableList items;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDateTime();
        refreshItemListInCombo();
        selectFirstOne();
    }

    private void selectFirstOne() {

        choicebox_name.getSelectionModel().selectFirst();
    }

    private void loadDateTime() {

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String strDateInFormat = dateFormat.format(cal.getTime());
        System.out.println("strDateInFormat is: " + strDateInFormat);
        date_time_box.setText(strDateInFormat);

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

    @FXML
    private void onClearButtonClicked(ActionEvent event) throws IOException {
        choicebox_name.getSelectionModel().clearSelection();
        description_box.clear();
        cost_box.clear();
        loadDateTime();
        //date_time_box.clear();
        quantity_box.clear();
    }

    @FXML
    private boolean onAddButtonClicked(ActionEvent event) {
        //insert into db if valid
        Date date = new Date();
        String theDate = date.toString();
        String amount_name = "";
        if (!choicebox_name.getValue().toString().isEmpty()) {
            amount_name = choicebox_name.getValue().toString();
        }
        else{
            selectFirstOne();
        }
        String str_description = description_box.getText();  //description_box.getText();
        String str_quantity = quantity_box.getText();

        String str_cost = cost_box.getText();

        if ((!str_cost.matches("[0-9]*")) || (str_cost.trim().length() == 0) || (str_cost == "") || (str_cost.trim().isEmpty())) {

            Alert alert1 = new Alert(AlertType.INFORMATION);
            alert1.setTitle("Information Dialog");
            alert1.setHeaderText(null);
            alert1.setContentText("AMOUNT has to be numeric.");

            alert1.showAndWait();
            //System.clearProperty(str_cost);
            cost_box.setText("0");
            return false;

        }

        if ((amount_name.trim().length() == 0) || (amount_name == "") || (amount_name.trim().isEmpty()) || (amount_name == "--SELECT ONE--")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("TITLE is missing.");

            alert.showAndWait();
            System.clearProperty(amount_name);
            return false;
        }

        date_time_box.setText(theDate);
        String txnMonth = theDate.substring(4, 7);
        String txnYear = theDate.substring(24, 28);
        String txnDay = theDate.substring(0, 10) + " " + txnYear;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String strDateInFormat = dateFormat.format(cal.getTime());
        System.out.println("strDateInFormat is: " + strDateInFormat);

        String query = "INSERT INTO ExpAmount_tbl (amount,amount_name,amount_details,txnDay,txnMonth,txnYear,formated_date,amount_date ) VALUES ("
                + "'" + str_cost + "'," + "'" + amount_name + "'," + "'" + str_description + "','"
                + txnDay + "'," + "'" + txnMonth + "'," + "'" + txnYear + "'," + "'"
                + strDateInFormat + "','" + theDate + "');";

        System.out.println("Inserting\n" + query);
        insertStatement(query);
        System.out.println("Succesfully Inserted");
        choicebox_name.getSelectionModel().clearSelection();
        description_box.clear();
        cost_box.clear();
        loadDateTime();
        //date_time_box.clear();
        quantity_box.clear();

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

    //code for combo starts
    private void refreshItemListInCombo() {
        //Set items equal to an empty ArrayList
        items = FXCollections.observableArrayList();

        //Select out of the DB, fill accordingly
        getIncomeTxn(items);

        //Set the listview to what we just populated with DB contents
        choicebox_name.setItems(items);

    }
    /* Queries for tasks as per   the parameters passed in */

    private void getIncomeTxn(ObservableList items) {
        Connection c = null;
        Statement stmt = null;

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
            orderby = "amount_id";
            System.out.println("Query is: SELECT distinct amount_name FROM ExpAmount_tbl");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct amount_name FROM ExpAmount_tbl");
            items.add("--SELECT ONE--");
            while (rs.next()) {
                String theItemName = rs.getString("amount_name");

                System.out.println("theItemName = " + theItemName);

                System.out.println();
                //IMPORTANT STATEMENT HERE:
                items.add(theItemName);
            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
    //code for combo box ends


}
