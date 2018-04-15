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
import salesline.entities.Customers;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class AllCustomersController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TableView<?> tableCustomer;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn addressCol;
    @FXML
    private TableColumn phoneCol;
    @FXML
    private TableColumn emailCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn dateCol;
    @FXML
    private Button btnDownloadReport;
    @FXML
    private TextField name_box;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField type_box;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField phone_box;
    @FXML
    private TextField address_box;
    @FXML
    private TextField email_box;
    private ObservableList CustomersData;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TextField description_box;
    private Connection con;
    private Statement stat;
    private String orderby;
    private final String REPORT_NAME = "ALL_CUSTOMERS";
    private String path;
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
        CustomersData = FXCollections.observableArrayList();

        idCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("id")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("name")
        );
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("description")
        );
        addressCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("address")
        );

        phoneCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("phone")
        );
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("email")
        );

        typeCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("type")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Customers, String>("customer_date")
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
            orderby = "id";
            String todRepQuery = "SELECT * FROM Customers_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Customers theCustomers = new Customers();

                String id = rs.getString("id");
                String Customers_name = rs.getString("name");
                String description = rs.getString("description");
                String Customers_address1 = rs.getString("address");
                String Customers_phone = rs.getString("phone");
                String Customers_email = rs.getString("email");
                String Customers_type = rs.getString("type");
                String date_time = rs.getString("customer_date");

                System.out.println("id = " + id);
                System.out.println("Customers_name = " + Customers_name);
                System.out.println("Customers_address1 = " + Customers_address1);
                System.out.println("phone = " + Customers_phone);
                System.out.println("email = " + Customers_email);
                System.out.println("type = " + Customers_type);
                System.out.println("CustomerssDate = " + date_time);

                theCustomers.setId(id);
                System.out.println("after seting id is :" + theCustomers.getId());

                theCustomers.setName(Customers_name);
                System.out.println("after seting firstName is :" + theCustomers.getName());

                theCustomers.setAddress(Customers_address1);
                System.out.println("after seting Customerss_address1 is :" + theCustomers.getAddress());
                
                
                theCustomers.setDescription(description);
                System.out.println("after seting Customerss_phone_1 is :" + theCustomers.getDescription());

                theCustomers.setEmail(Customers_email);
                System.out.println("after seting Customerss_email is :" + theCustomers.getEmail());
      
                theCustomers.setPhone(Customers_phone);
                System.out.println("after seting type is :" + theCustomers.getPhone());
      
                theCustomers.setType(Customers_type);
                System.out.println("after seting type is :" + theCustomers.getPhone());

                theCustomers.setCustomer_date(date_time);
                System.out.println("after seting CustomerssDate is :" + theCustomers.getCustomer_date());

                CustomersData.add(theCustomers);
                System.out.println("after Customerss  data");
            }
            tableCustomer.setItems(CustomersData);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    @FXML
    private void onClickDownloadReport(ActionEvent event) throws FileNotFoundException, IOException {
        System.err.println("inside onClickDownloadReport");
        TableView<Customers> table = new TableView<Customers>();

        ObservableList<Customers> teamMembers = getReportDownloadMembers();
        table.setItems(teamMembers);
//column names and values
        TableColumn<Customers, String> excelIdCol = new TableColumn<Customers, String>("ID");
        excelIdCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("id"));

        TableColumn<Customers, String> excelnameCol = new TableColumn<Customers, String>("NAME");
        excelnameCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("name"));

        TableColumn<Customers, String> exceladdressCol = new TableColumn<Customers, String>("ADDRESS");
        exceladdressCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("address"));

        TableColumn<Customers, String> exceldescriptionCol = new TableColumn<Customers, String>("DESCRIPTION");
        exceldescriptionCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("description"));


        TableColumn<Customers, String> excelphoneCol = new TableColumn<Customers, String>("PHONE");
        excelphoneCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("phone"));

        TableColumn<Customers, String> excelemailCol = new TableColumn<Customers, String>("EMAIL ADDRESS");
        excelemailCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("email"));

        TableColumn<Customers, String> exceltypeCol = new TableColumn<Customers, String>("TYPE");
        exceltypeCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("type"));

        TableColumn<Customers, String> exceldateCol = new TableColumn<Customers, String>("REG DATE");
        exceldateCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("customer_date"));

        System.err.println("after property factory");

        ObservableList<TableColumn<Customers, ?>> columns = table.getColumns();
        columns.add(excelIdCol);
        columns.add(excelnameCol);
        columns.add(exceladdressCol);
        columns.add(exceldescriptionCol);
        columns.add(excelphoneCol);
        columns.add(excelemailCol);
        columns.add(exceltypeCol);
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

    private ObservableList<Customers> getReportDownloadMembers() {

        ObservableList<Customers> theHmos = FXCollections.observableArrayList();
        try {
            java.util.Date maindate = new java.util.Date();
            String theDate = maindate.toString();
            String theYear = theDate.substring(24, 28);
            String theDay = theDate.substring(0, 10) + " " + theYear;

            // SQLiteConfig config = new SQLiteConfig();
            con = DriverManager.getConnection("jdbc:sqlite:dbsalesline.db");
            stat = con.createStatement();
            System.out.println("Opened database successfully");
            orderby = "id";
            String todRepQuery = "SELECT * FROM Customers_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Customers myCustomer = new Customers();

       id = rs.getString("id");
                String Customers_name = rs.getString("name");
                String description = rs.getString("description");
                String Customers_address1 = rs.getString("address");
                String Customers_phone_1 = rs.getString("phone");
                String Customers_email = rs.getString("email");
                String Customers_type = rs.getString("type");
                String date_time = rs.getString("customer_date");

                System.out.println("id = " + id);
                System.out.println("Customers_name = " + Customers_name);
                System.out.println("Customers_address1 = " + Customers_address1);
                System.out.println("phone = " + Customers_phone_1);
                System.out.println("email = " + Customers_email);
                System.out.println("type = " + Customers_type);
                System.out.println("CustomerssDate = " + date_time);

                myCustomer.setId(id);
                System.out.println("after seting id is :" + myCustomer.getId());

                myCustomer.setName(Customers_name);
                System.out.println("after seting firstName is :" + myCustomer.getName());

                myCustomer.setAddress(Customers_address1);
                System.out.println("after seting Customerss_address1 is :" + myCustomer.getAddress());
                
                myCustomer.setDescription(description);
                System.out.println("after seting Customerss_address1 is :" + myCustomer.getDescription());

                myCustomer.setPhone(Customers_phone_1);
                System.out.println("after seting Customerss_phone_1 is :" + myCustomer.getPhone());

                myCustomer.setEmail(Customers_email);
                System.out.println("after seting Customerss_email is :" + myCustomer.getEmail());

                myCustomer.setType(Customers_type);
                System.out.println("after seting type is :" + myCustomer.getType());

                myCustomer.setCustomer_date(date_time);
                System.out.println("after seting CustomerssDate is :" + myCustomer.getCustomer_date());

                theHmos.add(myCustomer);
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
        address_box.clear();
        phone_box.clear();
        email_box.clear();
        type_box.clear();

        Customers myCustomers = new Customers();
        Date date = new Date();
        String theDate = date.toString();
      
        String Customers_name;
        String Customers_phone;
        String Customers_address;
        String Customers_email;
        String Customers_type;
         String Customer_description;

        myCustomers = (Customers) tableCustomer.getSelectionModel().getSelectedItem();
        id = myCustomers.getId();
        Customers_name = myCustomers.getName();
        Customers_phone = myCustomers.getPhone();
        Customer_description = myCustomers.getDescription();
        Customers_address = myCustomers.getAddress();
        Customers_email = myCustomers.getEmail();
        Customers_type = myCustomers.getType();

        name_box.setText(Customers_name);
        address_box.setText(Customers_address);
        phone_box.setText(Customers_phone);
        email_box.setText(Customers_email);
        type_box.setText(Customers_type);
        description_box.setText(Customer_description);
        phone_box.setText(Customers_phone);

    }

    @FXML
    private boolean onClickUpdate(ActionEvent event) throws IOException {
        Customers myCustomers = new Customers();
        Date date = new Date();
        int theId = Integer.parseInt(id);
        String theDate = date.toString();
        //format for report queries
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String Customers_name = name_box.getText();
        String Customers_address = address_box.getText().toUpperCase();
        String Customers_phone = phone_box.getText().toUpperCase();
        String Customers_email = email_box.getText().toUpperCase();
        String Customers_type = type_box.getText().toUpperCase();
        String Customers_description = description_box.getText().toUpperCase();

        System.out.println("Customers_name is: " + Customers_name);

        if ((Customers_name.trim().length() == 0) || (Customers_name == "") || (Customers_name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Customers Record");

            alert.showAndWait();
            //System.clearProperty(Customers_name);
            return false;
        }
        //myCustomers.setId(rs.getString("id"));

        myCustomers.setName(Customers_name);
        myCustomers.setCustomer_date(theDate);
        myCustomers.setAddress(Customers_address);
        myCustomers.setPhone(Customers_phone);
        myCustomers.setEmail(Customers_email);
        myCustomers.setType(Customers_type);
        myCustomers.setType(Customers_description);

        ObservableList highlightedCustomersRecord, allCustomersRecords;

        highlightedCustomersRecord = tableCustomer.getSelectionModel().getSelectedItems();

        System.out.println("Customers_name is : " + Customers_name);
        System.out.println("the ID IS: " + theId);
        String query = "UPDATE Customers_tbl set "
                + "address ='"
                + Customers_address + "',"
                + "description ='"
                + Customers_description + "',"
                + "phone ='"
                + Customers_phone + "',"
                + "email ='"
                + Customers_email + "',"
                + "type ='"
                + Customers_type + "',"
                + "customer_date ='"
                + theDate + "'"
                + " WHERE id = "
                + theId;

        System.out.println("updating\n" + query);
        updateStatement(query);

        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record updated Succesfully.");

        alert3.showAndWait();

        System.out.println("Succesfully Updated");

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllCustomers.fxml"));
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
        String Customers_name = name_box.getText();

        if ((Customers_name.trim().length() == 0) || (Customers_name == "") || (Customers_name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Class Room Record");

            alert.showAndWait();
            System.clearProperty(Customers_name);
        }
        //myCustomers.setId(rs.getString("id"));

        ObservableList highlightedCustomersRecord, allCustomersRecords;
        allCustomersRecords = tableCustomer.getItems();
        highlightedCustomersRecord = tableCustomer.getSelectionModel().getSelectedItems();
        System.out.println("Customers_name is : " + Customers_name);

        String query = "DELETE FROM Customers_tbl "
                + " WHERE id = "
                + theId;

        System.out.println("updating\n" + query);
        updateStatement(query);

        Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
        alert3.setTitle("Information Dialog");
        alert3.setHeaderText(null);
        alert3.setContentText("Record Deleted Succesfully.");

        alert3.showAndWait();

        System.out.println("Succesfully Updated");

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllCustomers.fxml"));
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
