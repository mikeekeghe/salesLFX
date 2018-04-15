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
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class AddNewCustomerController implements Initializable {

    @FXML
    private TextField item_name_box;
    @FXML
    private TextArea item_desc_box;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnClear;
    @FXML
    private Button addButton;
    @FXML
    private TextArea address_box;
    @FXML
    private TextField phone_box;
    @FXML
    private TextField email_box;
    @FXML
    private TextField type_box;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private boolean onAddButtonClicked(ActionEvent event) {
        //insert into db if valid
        Date date = new Date();
        String theDate = date.toString();
        String item_name = item_name_box.getText().toUpperCase();
        String item_desc = item_desc_box.getText().toUpperCase();
        String address = address_box.getText().toUpperCase();
        String phone = phone_box.getText().toUpperCase();
        String email = email_box.getText().toUpperCase();
        String type = email_box.getText().toUpperCase();

        if ((item_name.trim().length() == 0) || (item_name == "") || (item_name.trim().isEmpty()) || (item_name == "--SELECT ONE--")) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information Dialog");
            alert2.setHeaderText(null);
            alert2.setContentText("Type the Customer Name.");

            alert2.showAndWait();
            System.clearProperty(item_name);
            return false;
        }

        String query = "INSERT INTO Customers_tbl (name,description,address,phone,email,type,customer_date ) VALUES ("
                + "'" + item_name + "'," + "'" + item_desc + "'," + "'" + address + "'," + "'" + phone + "','" 
                + email + "','" + type + "','" + theDate + "');";

        System.out.println("Inserting\n" + query);
        insertStatement(query);
        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record Added Succesfully.");

        alert3.showAndWait();
        System.out.println("Succesfully Inserted");
        item_name_box.clear();
        item_desc_box.clear();
        address_box.clear();
        phone_box.clear();
        email_box.clear();
        type_box.clear();
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
    private void onClickClose(ActionEvent event) throws IOException {

        Parent clpage_parent = FXMLLoader.load(getClass().getResource("SalesLine.fxml"));
        Scene page_scene = new Scene(clpage_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(page_scene);
        app_stage.show();
    }

    @FXML
    private void onClearButtonClicked() {
        item_name_box.clear();
        item_desc_box.clear();
        address_box.clear();
        phone_box.clear();
        email_box.clear();
        type_box.clear();
    }

}
