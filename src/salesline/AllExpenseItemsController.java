/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import salesline.entities.Transaction;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class AllExpenseItemsController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TableColumn amountCol;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn dateCol;
    @FXML
    private Button btnDownloadReport;
    @FXML
    private TextField name_box;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField amount_box;
    @FXML
    private TextField description_box;
    private final String REPORT_NAME = "ALL EXPENSE ITEMS";
    private String path;
    private ObservableList<Object> TransactionData;
    private Connection con;
    private Statement stat;
    private String orderby;
    @FXML
    private TableView tableExpenses;
    private String id;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("before refresh");
        refreshList();

        System.out.println("after refresh");
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

    private void refreshList() {
        TransactionData = FXCollections.observableArrayList();

        idCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_id")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_name")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount")
        );
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_details")
        );

        dateCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_date")
        );

        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "amount_id";
            String todRepQuery = "SELECT * FROM ExpAmount_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Transaction theTransaction = new Transaction();

                String id = rs.getString("amount_id");
                String amount_name = rs.getString("amount_name");
                String amount_descriptiom = rs.getString("amount_details");
                String amount = rs.getString("amount");
                String date_time = rs.getString("amount_date");

   

                theTransaction.setAmount_id(id);
                System.out.println("after seting id is :" + theTransaction.getAmount_id());

                theTransaction.setAmount_name(amount_name);
                System.out.println("after seting firstName is :" + theTransaction.getAmount_name());

                theTransaction.setAmount_details(amount_descriptiom);
                System.out.println("after seting firstName is :" + theTransaction.getAmount_details());

                theTransaction.setAmount(amount);
                System.out.println("after seting Transactions_address1 is :" + theTransaction.getAmount());

                theTransaction.setAmount_date(date_time);
                System.out.println("after seting Transactions_phone_1 is :" + theTransaction.getAmount_date());


                TransactionData.add(theTransaction);
                System.out.println("after Transactions  data");
            }
            tableExpenses.setItems(TransactionData);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    @FXML
    private void onClickDownloadReport(ActionEvent event) throws FileNotFoundException, IOException {
        System.err.println("inside onClickDownloadReport");
        TableView<Transaction> table = new TableView<Transaction>();

        ObservableList<Transaction> teamMembers = getReportDownloadMembers();
        table.setItems(teamMembers);
//column names and values
        TableColumn<Transaction, String> excelIdCol = new TableColumn<Transaction, String>("ID");
        excelIdCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount_id"));

        TableColumn<Transaction, String> excelnameCol = new TableColumn<Transaction, String>("NAME");
        excelnameCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount_name"));

        TableColumn<Transaction, String> exceldescriptionCol = new TableColumn<Transaction, String>("DESCRIPTION");
        exceldescriptionCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount_details"));

        TableColumn<Transaction, String> excelamountCol = new TableColumn<Transaction, String>("AMOUNT");
        excelamountCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount"));

        TableColumn<Transaction, String> exceldateCol = new TableColumn<Transaction, String>("REG DATE");
        exceldateCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount_date"));

        System.err.println("after property factory");

        ObservableList<TableColumn<Transaction, ?>> columns = table.getColumns();
        columns.add(excelIdCol);
        columns.add(excelnameCol);
        columns.add(exceldescriptionCol);
        columns.add(excelamountCol);
        columns.add(exceldateCol);

        System.err.println("after adding columns");

        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet(REPORT_NAME);
        System.err.println("after creating sheet in excel file");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < table.getColumns().size(); j++) {
            row.createCell(j).setCellValue(table.getColumns().get(j).getText());
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if (table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        try {
//            java.util.Date date = new java.util.Date();
//            String theDate = date.toString();
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            cal.add(Calendar.DATE, 0);
            String strDateInFormat = dateFormat.format(cal.getTime());
            System.out.println("strDateInFormat is: " + strDateInFormat);
            strDateInFormat = strDateInFormat.replace("-", "");
            strDateInFormat = strDateInFormat.replace(" ", "");
            strDateInFormat = strDateInFormat.replace(":", "");
            System.out.println("strDateInFormat is: " + strDateInFormat);

            path = "C:\\Downloads\\";
            path = path.replace("\\", "/");
            FileOutputStream fileOut = new FileOutputStream(path + REPORT_NAME + strDateInFormat + ".xls");
            workbook.write(fileOut);
            System.out.println("after creating excel file");
            fileOut.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String strDateInFormat = dateFormat.format(cal.getTime());
        System.out.println("strDateInFormat is: " + strDateInFormat);
        strDateInFormat = strDateInFormat.replace("-", "");
        strDateInFormat = strDateInFormat.replace(" ", "");
        strDateInFormat = strDateInFormat.replace(":", "");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Report Downloaded Succesfully to " + path + ".\n with Filename: " + REPORT_NAME + strDateInFormat + ".xls");

        alert.showAndWait();
    }

    private ObservableList<Transaction> getReportDownloadMembers() {

        ObservableList<Transaction> theHmos = FXCollections.observableArrayList();
        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "amount_id";
            String todRepQuery = "SELECT * FROM ExpAmount_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Transaction myTransaction = new Transaction();

                String id = rs.getString("amount_id");
                String amount_name = rs.getString("amount_name");
                String amount_details = rs.getString("amount_details");
                String amount = rs.getString("amount");
                String date_time = rs.getString("amount_date");

                System.out.println("id = " + id);
                System.out.println("amount_name = " + amount_name);

                System.out.println("TransactionsDate = " + date_time);

                myTransaction.setAmount_id(id);
                System.out.println("after seting id is :" + myTransaction.getAmount_id());

                myTransaction.setAmount_name(amount_name);
                System.out.println("after seting firstName is :" + myTransaction.getAmount_name());

                myTransaction.setAmount(amount);

                myTransaction.setAmount_details(amount_details);

                myTransaction.setAmount_date(date_time);

                theHmos.add(myTransaction);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return theHmos;
    }

    private void onClickExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onClickTable(MouseEvent event) {
        name_box.clear();
        amount_box.clear();
        description_box.clear();
 

        Transaction myTransaction = new Transaction();
        Date date = new Date();
        String theDate = date.toString();
        String amount_name;
        String amount_phone;
        String amount_address;
        String amount_email;
        String amount_type;
        String amount ;
        String amount_details;

        myTransaction = (Transaction) tableExpenses.getSelectionModel().getSelectedItem();
        id = myTransaction.getAmount_id();
        amount_name = myTransaction.getAmount_name();
        amount = myTransaction.getAmount();
         amount_details = myTransaction.getAmount_details();

        name_box.setText(amount_name);
       amount_box.setText(amount);
       description_box.setText(amount_details);

    }

    @FXML
    private boolean onClickUpdate(ActionEvent event) throws IOException {
        Transaction myTransaction = new Transaction();
        Date date = new Date();
        int theId = Integer.parseInt(id);
        String theDate = date.toString();
        //format for report queries
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String amount_name = name_box.getText();
        String amount_details = description_box.getText().toUpperCase();
        String amount = amount_box.getText().toUpperCase();
      

        System.out.println("amount_name is: " + amount_name);

        if ((amount_name.trim().length() == 0) || (amount_name == "") || (amount_name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Transaction Record");

            alert.showAndWait();
            //System.clearProperty(amount_name);
            return false;
        }
        //myTransaction.setId(rs.getString("id"));

        myTransaction.setAmount_name(amount_name);
        myTransaction.setAmount_date(theDate);
        myTransaction.setAmount_details(amount_details);
        myTransaction.setAmount(amount);
      

        ObservableList highlightedTransactionRecord, allTransactionRecords;

        highlightedTransactionRecord = tableExpenses.getSelectionModel().getSelectedItems();

        System.out.println("amount_name is : " + amount_name);
        System.out.println("the ID IS: " + theId);
        String query = "UPDATE ExpAmount_tbl set "
                + "amount_details ='"
                + amount_details + "',"
                + "amount ='"
                + amount + "',"
                + "amount_date ='"
                + theDate + "'"
                + " WHERE amount_id = "
                + theId;

        System.out.println("updating\n" + query);
        updateStatement(query);

        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record updated Succesfully.");

        alert3.showAndWait();

        System.out.println("Succesfully Updated");

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllExpenseItems.fxml"));
        Scene TodayReport_page_scene = new Scene(TodayReport_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(TodayReport_page_scene);
        app_stage.show();

        return true;

    }

    @FXML
    private void onClickDelete(ActionEvent event) throws IOException {
        int theId = Integer.parseInt(id);
        String amount_name = name_box.getText();

        if ((amount_name.trim().length() == 0) || (amount_name == "") || (amount_name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Record");

            alert.showAndWait();
            System.clearProperty(amount_name);
        }
        //myTransaction.setId(rs.getString("id"));

        ObservableList highlightedTransactionRecord, allTransactionRecords;
        allTransactionRecords = tableExpenses.getItems();
        highlightedTransactionRecord = tableExpenses.getSelectionModel().getSelectedItems();
        System.out.println("amount_name is : " + amount_name);

        String query = "DELETE FROM ExpAmount_tbl "
                + " WHERE amount_id = "
                + theId;

        System.out.println("updating\n" + query);
        updateStatement(query);

        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record Deleted Succesfully.");

        alert3.showAndWait();

        System.out.println("Succesfully Updated");

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllExpenseItems.fxml"));
        Scene TodayReport_page_scene = new Scene(TodayReport_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide(); //optional
        app_stage.setScene(TodayReport_page_scene);
        app_stage.show();

    }

    private void updateStatement(String update_query) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            System.out.println("Our query was: " + update_query);
            stmt.executeUpdate(update_query);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
