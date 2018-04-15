/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import salesline.entities.Transaction;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class ExpYesterdayReportController implements Initializable {

    @FXML
    private Button btnClose;
    String orderby;

    String ascdesc;
    String myTatal;
    @FXML
    private Label lblTotal;

    int intBal;

    private ObservableList<Transaction> ExpensetransactionData;

    @FXML
    private TableView tableExpenses;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn amountCol;
    @FXML
    private TableColumn unitslCol;
    @FXML
    private TableColumn dateCol;

    //START | SQLITE
    private static Connection con;
    private static Statement stat;
    private PreparedStatement prep;
    //END | SQLITE
    private String ezpenseid;
    private String name;
    private String desctription;
    private String expdate;
    private String amount;
    private String units;
    
    @FXML
    private Button btnDownloadReport;
    private final String REPORT_NAME = "YESTERDAYS_EXPENSES_REPORT";
    private String path;
    String strTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("before refresh");
        refreshList();
        strTotal = String.valueOf(calculateMyTotal());

        strTotal = "N" + strTotal;
        strTotal = toCommaAmount(strTotal);

        System.out.println(strTotal);
        lblTotal.setText(strTotal);

//        String strTotalExp = String.valueOf(calculateMyTotalExp());
//        lblTotalExp.setText(strTotalExp);
        String strBallance;
        strTotal = strTotal.substring(1);
        strTotal = strTotal.replaceAll(",", "");

        strBallance = intBal + "";
        strBallance = "N" + strBallance;
        strBallance = toCommaAmount(strBallance);
        System.out.println(strBallance);
//        lblBallance.setText(strBallance);
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
        ExpensetransactionData = FXCollections.observableArrayList();

        idCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_id")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_name")
        );
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_details")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount_date")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount")
        );

        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            cal.add(Calendar.DATE, -1);
            String yesterday = dateFormat.format(cal.getTime());

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "amount_id";
            String todRepQuery = "SELECT * FROM ExpAmount_tbl WHERE txnDay ='" + yesterday + "'"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Transaction myExpTransaction = new Transaction();

                ezpenseid = rs.getString("amount_id");
                name = rs.getString("amount_name");
                desctription = rs.getString("amount_details");
                expdate = rs.getString("amount_date");
                amount = rs.getString("amount");

                System.out.println("ezpenseid = " + ezpenseid);
                System.out.println("name = " + name);
                System.out.println("desctription = " + desctription);
                System.out.println("expdate = " + expdate);
                System.out.println("amount = " + amount);
                System.out.println("units = " + units);

                myExpTransaction.setAmount_id(rs.getString("amount_id"));
                myExpTransaction.setAmount_name(rs.getString("amount_name"));
                myExpTransaction.setAmount_details(rs.getString("amount_details"));
                myExpTransaction.setAmount_date(rs.getString("amount_date"));
                myExpTransaction.setAmount(rs.getString("amount"));

                ExpensetransactionData.add(myExpTransaction);
            }
            tableExpenses.setItems(ExpensetransactionData);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    private int calculateMyTotal() {
        int sum = 0;
        Connection conn = null;
        Statement stmt = null;
        java.util.Date date = new java.util.Date();
        String theDate = date.toString();
        String theYear = theDate.substring(24, 28);
        String theDay = theDate.substring(0, 10) + " " + theYear;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
        cal.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(cal.getTime());
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            String totQuery = "SELECT SUM(amount) FROM ExpAmount_tbl WHERE txnDay = '" + yesterday + "'";
            ResultSet res = stmt.executeQuery(totQuery);

            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            System.out.println("Sum of column = " + sum);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return sum;
    }

    private String toCommaAmount(String strTotal) {
        int strLen = strTotal.length();
        StringBuilder builder;
        String newamt = "";
        // Initialize StringBuilder with this value.
        builder = new StringBuilder(strTotal);

        if (strLen < 5) {
            newamt = strTotal;
        }

        if (strLen == 5) {

            builder.insert(2, ",");
            newamt = builder.toString();
        }

        if (strLen == 6) {
            builder.insert(3, ",");
            newamt = builder.toString();
        }

        if (strLen == 7) {
            builder.insert(4, ",");
            newamt = builder.toString();
        }
        if (strLen == 8) {
            builder.insert(2, ",");
            builder.insert(6, ",");
            newamt = builder.toString();
        }
        if (strLen == 9) {
            builder.insert(3, ",");
            builder.insert(7, ",");
            newamt = builder.toString();
        }

        if (strLen == 10) {
            builder.insert(4, ",");
            builder.insert(8, ",");
            newamt = builder.toString();
        }
        if (strLen == 11) {
            builder.insert(2, ",");
            builder.insert(6, ",");
            builder.insert(9, ",");
            newamt = builder.toString();
        }
        return newamt;
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

        TableColumn<Transaction, String> exceldateCol = new TableColumn<Transaction, String>("DATE");
        exceldateCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount_date"));

        TableColumn<Transaction, String> excelamountCol = new TableColumn<Transaction, String>("AMOUNT");
        excelamountCol.setCellValueFactory(new PropertyValueFactory<Transaction, String>("amount"));

        TableColumn<Transaction, String> excelEmptyCol = new TableColumn<Transaction, String>("");
        excelEmptyCol.setText("");

        TableColumn<Transaction, String> excelSumTextCol = new TableColumn<Transaction, String>("");
        excelSumTextCol.setText("Total is :");

        TableColumn<Transaction, String> excelSum = new TableColumn<Transaction, String>("");
        excelSum.setText(strTotal);

        System.err.println("after property factory");

        ObservableList<TableColumn<Transaction, ?>> columns = table.getColumns();
        columns.add(excelIdCol);
        columns.add(excelnameCol);
        columns.add(exceldescriptionCol);
        columns.add(exceldateCol);
        columns.add(excelamountCol);
        columns.add(excelEmptyCol);
        columns.add(excelSumTextCol);
        columns.add(excelSum);

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

        ObservableList<Transaction> allExpTransactions = FXCollections.observableArrayList();
        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            cal.add(Calendar.DATE, -1);
            String yesterday = dateFormat.format(cal.getTime());

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "amount_id";
            String todRepQuery = "SELECT * FROM ExpAmount_tbl WHERE txnDay = '" + yesterday + "'"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Transaction myExpTransaction = new Transaction();

                ezpenseid = rs.getString("amount_id");
                name = rs.getString("amount_name");
                desctription = rs.getString("amount_details");
                expdate = rs.getString("amount_date");
                amount = rs.getString("amount");

                System.out.println("ezpenseid = " + ezpenseid);
                System.out.println("name = " + name);
                System.out.println("desctription = " + desctription);
                System.out.println("expdate = " + expdate);
                System.out.println("amount = " + amount);
                System.out.println("units = " + units);

                myExpTransaction.setAmount_id(rs.getString("amount_id"));
                myExpTransaction.setAmount_name(rs.getString("amount_name"));
                myExpTransaction.setAmount_details(rs.getString("amount_details"));
                myExpTransaction.setAmount_date(rs.getString("amount_date"));
                myExpTransaction.setAmount(rs.getString("amount"));

                allExpTransactions.add(myExpTransaction);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return allExpTransactions;
    }

    private void onClickExit(ActionEvent event) {
        Platform.exit();
    }
}
