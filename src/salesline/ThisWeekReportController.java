/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline;

import salesline.entities.Appointment;
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
public class ThisWeekReportController implements Initializable {

    @FXML
    private Button btnClose;
    String orderby;

    String ascdesc;
    String myTatal;
    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotal2;
    @FXML
    private Label lblTotalExp;
    @FXML
    private Label lblBallance;
    int intBal;

    private ObservableList<Appointment> AppointmentData;
    @FXML
    private TableView tableReport;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;

    @FXML
    private TableColumn dateCol;
    @FXML
    private TableColumn amountCol;

    @FXML
    private TableColumn notesCol;

    //START | SQLITE
    private static Connection con;
    private static Statement stat;
    private PreparedStatement prep;
    //END | SQLITE
    private String appointmentId;
    private String Student;
    private String Teacher;
    private String date;
    private String amount;

    private String notes;
    @FXML
    private Button btnDownloadReport;
    private final String REPORT_NAME = "THIS_WEEKS_REPORT";
    private String path;
    String strTotal;
    String strTotalExp;
    String strBallance;

    @FXML
    private TableColumn invoiceCol;
    @FXML
    private TableColumn customerCol;
    private String customer;

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
        lblTotal2.setText(strTotal);

        strTotalExp = String.valueOf(calculateMyTotalExp());

        strTotalExp = "N" + strTotalExp;
        strTotalExp = toCommaAmount(strTotalExp);

        System.out.println(strTotalExp);
        lblTotalExp.setText(strTotalExp);
        strTotal = strTotal.substring(1);
        strTotal = strTotal.replaceAll(",", "");
        strTotalExp = strTotalExp.substring(1);
        strTotalExp = strTotalExp.replaceAll(",", "");
        intBal = Integer.parseInt(strTotal) - Integer.parseInt(strTotalExp);
        System.out.println(strTotal + " - " + strTotalExp);
        strBallance = intBal + "";
        strBallance = "N" + strBallance;
        strBallance = toCommaAmount(strBallance);
        System.out.println(strBallance);
        lblBallance.setText(strBallance);
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
        AppointmentData = FXCollections.observableArrayList();

        idCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("appointment_id")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("appointment_name")
        );
        customerCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("customer")
        );

        dateCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("date_time")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("amount")
        );

        notesCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("notes")
        );

        invoiceCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("invoiceNo")
        );
        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;

            // SQLiteConfig config = new SQLiteConfig();
            orderby = "appointment_id";
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "appointment_id";
            String reportQuery = "SELECT * FROM Appointment_tbl WHERE appointment_date  BETWEEN datetime('now','-7 days') "
                    + "AND datetime('now', 'localtime') ORDER BY " + orderby;
            System.out.println("reportQuery is :" + reportQuery);
            ResultSet rs = con.createStatement().executeQuery(reportQuery);
            while (rs.next()) {
                Appointment myAppointment = new Appointment();

                appointmentId = rs.getString("appointment_id");
                customer = rs.getString("customer");
                date = rs.getString("date_time");
                amount = rs.getString("amount");

                notes = rs.getString("notes");

                System.out.println("appointmentId = " + appointmentId);
                System.out.println("Student = " + Student);
                System.out.println("date = " + date);

                System.out.println("notes = " + notes);

                myAppointment.setAppointment_id(rs.getString("appointment_Id"));
                myAppointment.setAppointment_name(rs.getString("appointment_name"));
                myAppointment.setCustomer(rs.getString("customer"));
                myAppointment.setDate_time(rs.getString("date_time"));
                myAppointment.setAmount(rs.getString("amount"));
                myAppointment.setNotes(rs.getString("notes"));

                AppointmentData.add(myAppointment);
            }
            tableReport.setItems(AppointmentData);

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
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            String totQuery = "SELECT SUM(amount) FROM Appointment_tbl WHERE appointment_date BETWEEN datetime('now','-7 days') AND datetime('now', 'localtime')";
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

    private int calculateMyTotalExp() {
        int sum = 0;
        Connection conn = null;
        Statement stmt = null;
        java.util.Date date = new java.util.Date();
        String theDate = date.toString();
        String theYear = theDate.substring(24, 28);
        String theDay = theDate.substring(0, 10) + " " + theYear;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stmt = conn.createStatement();
            String totQuery = "SELECT SUM(amount) FROM ExpAmount_tbl WHERE formated_date BETWEEN datetime('now','-7 days')AND datetime('now', 'localtime')";
            System.out.println(totQuery);
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

    @FXML
    private void onClickDownloadReport(ActionEvent event) throws FileNotFoundException, IOException {
        System.err.println("inside onClickDownloadReport");
        TableView<Appointment> table = new TableView<Appointment>();

        ObservableList<Appointment> teamMembers = getReportDownloadMembers();
        table.setItems(teamMembers);
//column names and values
        TableColumn<Appointment, String> excelIdCol = new TableColumn<Appointment, String>("ID");
        excelIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointment_id"));

        TableColumn<Appointment, String> excelnameCol = new TableColumn<Appointment, String>("NAME");
        excelnameCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appointment_name"));
        
         TableColumn<Appointment, String> excelcustomerCol = new TableColumn<Appointment, String>("CUSTOMER");
        excelcustomerCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));

        TableColumn<Appointment, String> excelDateCol = new TableColumn<Appointment, String>("DATE");
        excelDateCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("date_time"));

        TableColumn<Appointment, String> excelAmountCol = new TableColumn<Appointment, String>("AMOUNT");
        excelAmountCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("amount"));

        TableColumn<Appointment, String> excelNotesCol = new TableColumn<Appointment, String>("NOTES");
        excelNotesCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("notes"));

        TableColumn<Appointment, String> excelInvoiceNoCol = new TableColumn<Appointment, String>("INVOICE No.");
        excelInvoiceNoCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("invoiceNo"));

        TableColumn<Appointment, String> excelEmptyCol = new TableColumn<Appointment, String>("");
        excelEmptyCol.setText("");

        TableColumn<Appointment, String> excelSumTextCol = new TableColumn<Appointment, String>("");
        excelSumTextCol.setText("Total income - Expenses is :");

        TableColumn<Appointment, String> excelSum = new TableColumn<Appointment, String>("");
        excelSum.setText(strTotal);

        TableColumn<Appointment, String> excelMinusTextCol = new TableColumn<Appointment, String>("");
        excelMinusTextCol.setText("-");

        TableColumn<Appointment, String> excelExpSum = new TableColumn<Appointment, String>("");
        excelExpSum.setText(strTotalExp);

        TableColumn<Appointment, String> excelEqualsTextCol = new TableColumn<Appointment, String>("");
        excelEqualsTextCol.setText("=");

        TableColumn<Appointment, String> excelBallance = new TableColumn<Appointment, String>("");
        excelBallance.setText(strBallance);

        System.err.println("after property factory");

        ObservableList<TableColumn<Appointment, ?>> columns = table.getColumns();
        columns.add(excelIdCol);
        columns.add(excelnameCol);
        columns.add(excelDateCol);
        columns.add(excelAmountCol);

        columns.add(excelNotesCol);

        columns.add(excelInvoiceNoCol);
        //calculation columns
        columns.add(excelSumTextCol);
        columns.add(excelSum);
        columns.add(excelMinusTextCol);
        columns.add(excelExpSum);
        columns.add(excelEqualsTextCol);
        columns.add(excelBallance);

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

    private ObservableList<Appointment> getReportDownloadMembers() {

        ObservableList<Appointment> theAppointment = FXCollections.observableArrayList();
        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "appointment_id";
            String todRepQuery = "SELECT * FROM Appointment_tbl WHERE appointment_date  BETWEEN datetime('now','-7 days') "
                    + "AND datetime('now', 'localtime') ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Appointment myAppointment = new Appointment();

                appointmentId = rs.getString("appointment_id");
                Student = rs.getString("appointment_name");
                date = rs.getString("date_time");
                amount = rs.getString("amount");

                notes = rs.getString("notes");

                System.out.println("appointmentId = " + appointmentId);
                System.out.println("Student = " + Student);
                System.out.println("date = " + date);
                System.out.println("amount = " + amount);

                System.out.println("notes = " + notes);

                myAppointment.setAppointment_id(rs.getString("appointment_Id"));
                myAppointment.setAppointment_name(rs.getString("appointment_name"));
                 myAppointment.setCustomer(rs.getString("customer"));
                myAppointment.setDate_time(rs.getString("date_time"));
                myAppointment.setAmount(rs.getString("amount"));
                myAppointment.setNotes(rs.getString("notes"));

                myAppointment.setInvoiceNo(rs.getString("invoiceNo"));
                myAppointment.setCustomer(rs.getString("customer"));

                theAppointment.add(myAppointment);
            }

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return theAppointment;
    }

    private void onClickExit(ActionEvent event) {
        Platform.exit();
    }
}
