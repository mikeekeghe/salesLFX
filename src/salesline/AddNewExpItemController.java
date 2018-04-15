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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class AddNewExpItemController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnClose;

    @FXML
    private TextField item_name_box;

    @FXML
    private TextArea item_desc_box;

    @FXML
    private TextField item_cost_box;

    @FXML
    private Button btnClear;
    
    @FXML
    private Button addButton;

    @FXML
    private boolean onAddExpButtonClicked(ActionEvent event) {
        //insert into db if valid
        Date date = new Date();
        String theDate = date.toString();
        String amount_name = item_name_box.getText();
        String amount_description = item_desc_box.getText();
        String amount = item_cost_box.getText();

        if ((!amount.matches("[0-9]*")) || (amount.trim().length() == 0) || (amount == "") || (amount.trim().isEmpty())) {

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Information Dialog");
            alert1.setHeaderText(null);
            alert1.setContentText("AMOUNT has to be numeric.");

            alert1.showAndWait();
            //System.clearProperty(str_cost);
            item_cost_box.setText("0");
            return false;

        }

        if ((amount_name.trim().length() == 0) || (amount_name == "") || (amount_name.trim().isEmpty()) || (amount_name == "--SELECT ONE--")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("TITLE is missing.");

            alert.showAndWait();
            System.clearProperty(amount_name);
            return false;
        }
        
        amount_name = amount_name.toUpperCase();
        amount_description = amount_description.toUpperCase();

        //format for report queries
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        System.out.println("amount_name is : " + amount_name);
        System.out.println("amount_description is : " + amount_description);
        System.out.println("amount is : " + amount);
        String query = "INSERT INTO ExpAmount_tbl (amount_name,amount_details,amount,formated_date,amount_date ) VALUES ("
                + "'" + amount_name + "'," + "'" + amount_description + "'," + "'"
                + amount + "','" + dateFormat + "','" + theDate + "');";

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
        item_cost_box.clear();
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
        item_cost_box.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


}
