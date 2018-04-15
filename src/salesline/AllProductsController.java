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
import salesline.entities.Products;

/**
 * FXML Controller class
 *
 * @author Eche Michael
 */
public class AllProductsController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TableView tableProducts;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn expiryCol;
    @FXML
    private TableColumn priceCol;
    @FXML
    private TableColumn unitsCol;
    @FXML
    private TableColumn quantityCol;
    @FXML
    private TableColumn dateCol;
    @FXML
    private Button btnDownloadReport;
    @FXML
    private TextField name_box;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField units_box;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField expiry_box;
    @FXML
    private TextField description_box;
    @FXML
    private TextField price_box;
    @FXML
    private TextField quantity_box;
    private final String REPORT_NAME = "ALL_PRODUCTS";
    private String path;
    private ObservableList<Object> ProductsData;
    private Connection con;
    private Statement stat;
    private String orderby;
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
        ProductsData = FXCollections.observableArrayList();

        idCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("id")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("name")
        );
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("description")
        );

        expiryCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("expiry_date")
        );
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("price")
        );

        unitsCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("units")
        );
        quantityCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("quantity")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Products, String>("product_date")
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
            String todRepQuery = "SELECT * FROM Products_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Products theProducts = new Products();

                String id = rs.getString("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String expiry = rs.getString("expiry_date");
                String price = rs.getString("price");
                String units = rs.getString("units");
                String quantity = rs.getString("quantity");
                System.out.println("quantity is :" + quantity);
                String date_time = rs.getString("product_date");

                System.out.println("id = " + id);
                System.out.println("name = " + name);
                System.out.println("description = " + description);
                System.out.println("expiry = " + expiry);
                System.out.println("price = " + price);
                System.out.println("quantity is :" + quantity);

                System.out.println("units = " + units);
                System.out.println("ProductssDate = " + date_time);

                theProducts.setId(id);
                System.out.println("after seting id is :" + theProducts.getId());

                theProducts.setName(name);
                System.out.println("after seting firstName is :" + theProducts.getName());

                theProducts.setDescription(description);
                System.out.println("after seting Productss_description1 is :" + theProducts.getDescription());

                theProducts.setExpiry_date(expiry);
                System.out.println("after seting Productss_expiry_1 is :" + theProducts.getExpiry_date());

                theProducts.setPrice(price);
                System.out.println("after seting Productss_price is :" + theProducts.getPrice());

                theProducts.setQuantity(quantity);
                System.out.println("quantity is :" + theProducts.getQuantity());

                theProducts.setUnits(units);
                System.out.println("after seting units is :" + theProducts.getUnits());

                theProducts.setProduct_date(date_time);
                System.out.println("after seting ProductssDate is :" + theProducts.getProduct_date());

                ProductsData.add(theProducts);
                System.out.println("after Productss  data");
            }
            tableProducts.setItems(ProductsData);

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    @FXML
    private void onClickDownloadReport(ActionEvent event) throws FileNotFoundException, IOException {
        System.err.println("inside onClickDownloadReport");
        TableView<Products> table = new TableView<Products>();

        ObservableList<Products> teamMembers = getReportDownloadMembers();
        table.setItems(teamMembers);
//column names and values
        TableColumn<Products, String> excelIdCol = new TableColumn<Products, String>("ID");
        excelIdCol.setCellValueFactory(new PropertyValueFactory<Products, String>("id"));

        TableColumn<Products, String> excelnameCol = new TableColumn<Products, String>("NAME");
        excelnameCol.setCellValueFactory(new PropertyValueFactory<Products, String>("name"));

        TableColumn<Products, String> exceldescriptionCol = new TableColumn<Products, String>("DESCRIPTION");
        exceldescriptionCol.setCellValueFactory(new PropertyValueFactory<Products, String>("description"));

        TableColumn<Products, String> excelexpiryCol = new TableColumn<Products, String>("EXPIRY DATE");
        excelexpiryCol.setCellValueFactory(new PropertyValueFactory<Products, String>("expiry_date"));

        TableColumn<Products, String> excelpriceCol = new TableColumn<Products, String>("PRICE");
        excelpriceCol.setCellValueFactory(new PropertyValueFactory<Products, String>("price"));

        TableColumn<Products, String> excelquantityCol = new TableColumn<Products, String>("QUANTITY");
        excelquantityCol.setCellValueFactory(new PropertyValueFactory<Products, String>("quantity"));

        TableColumn<Products, String> excelunitsCol = new TableColumn<Products, String>("UNITS");
        excelunitsCol.setCellValueFactory(new PropertyValueFactory<Products, String>("units"));

        TableColumn<Products, String> exceldateCol = new TableColumn<Products, String>("REG DATE");
        exceldateCol.setCellValueFactory(new PropertyValueFactory<Products, String>("product_date"));

        System.err.println("after property factory");

        ObservableList<TableColumn<Products, ?>> columns = table.getColumns();
        columns.add(excelIdCol);
        columns.add(excelnameCol);
        columns.add(exceldescriptionCol);
        columns.add(excelexpiryCol);
        columns.add(excelpriceCol);
        columns.add(excelquantityCol);
        columns.add(excelunitsCol);
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

    private ObservableList<Products> getReportDownloadMembers() {

        ObservableList<Products> theHmos = FXCollections.observableArrayList();
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
            String todRepQuery = "SELECT * FROM Products_tbl"
                    + " ORDER BY " + orderby;
            System.out.println("todRepQuery is :" + todRepQuery);
            ResultSet rs = con.createStatement().executeQuery(todRepQuery);
            while (rs.next()) {
                Products myProduct = new Products();

                String id = rs.getString("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String expiry = rs.getString("expiry_date");
                String price = rs.getString("price");
                String units = rs.getString("units");
                String quantity = rs.getString("quantity");
                String date_time = rs.getString("product_date");

                System.out.println("id = " + id);
                System.out.println("name = " + name);
                System.out.println("description = " + description);
                System.out.println("expiry = " + expiry);
                System.out.println("price = " + price);
                System.out.println("units = " + units);
                System.out.println("ProductssDate = " + date_time);

                myProduct.setId(id);
                System.out.println("after seting id is :" + myProduct.getId());

                myProduct.setName(name);
                System.out.println("after seting firstName is :" + myProduct.getName());

                myProduct.setDescription(description);
                System.out.println("after seting Productss_description1 is :" + myProduct.getDescription());

                myProduct.setExpiry_date(expiry);
                System.out.println("after seting Productss_expiry_1 is :" + myProduct.getExpiry_date());

                myProduct.setPrice(price);
                System.out.println("after seting Productss_price is :" + myProduct.getPrice());

                myProduct.setQuantity(quantity);
                System.out.println("after seting Productss_price is :" + myProduct.getPrice());

                myProduct.setUnits(units);
                System.out.println("after seting units is :" + myProduct.getUnits());

                myProduct.setProduct_date(date_time);
                System.out.println("after seting ProductssDate is :" + myProduct.getProduct_date());

                theHmos.add(myProduct);
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
        description_box.clear();
        expiry_box.clear();
        price_box.clear();
        units_box.clear();

        Products myProducts = new Products();
        Date date = new Date();
        String theDate = date.toString();

        String name;
        String Products_expiry;
        String Products_description;
        String price;
        String units;
        String quantity;
        String id;
        myProducts = (Products) tableProducts.getSelectionModel().getSelectedItem();
        id = myProducts.getId();
        name = myProducts.getName();
        Products_expiry = myProducts.getExpiry_date();
        Products_description = myProducts.getDescription();
        price = myProducts.getPrice();
        quantity = myProducts.getQuantity();
        units = myProducts.getUnits();

        name_box.setText(name);
        description_box.setText(Products_description);
        expiry_box.setText(Products_expiry);
        price_box.setText(price);

        quantity_box.setText(quantity);
        units_box.setText(units);

    }

    @FXML
    private boolean onClickUpdate(ActionEvent event) throws IOException {
        Products myProducts = new Products();
        Date date = new Date();
        int theId = Integer.parseInt(id);
        String theDate = date.toString();
        //format for report queries
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cal.add(Calendar.DATE, 0);
        String name = name_box.getText();
        String Products_description = description_box.getText().toUpperCase();
        String Products_expiry = expiry_box.getText().toUpperCase();
        String price = price_box.getText().toUpperCase();
        String quantity = quantity_box.getText().toUpperCase();
        String units = units_box.getText().toUpperCase();

        System.out.println("name is: " + name);

        if ((name.trim().length() == 0) || (name == "") || (name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Product Record");

            alert.showAndWait();
            //System.clearProperty(name);
            return false;
        }
        //myProducts.setId(rs.getString("id"));

        myProducts.setName(name);
        myProducts.setProduct_date(theDate);
        myProducts.setDescription(Products_description);
        myProducts.setExpiry_date(Products_expiry);
        myProducts.setPrice(price);
        myProducts.setQuantity(quantity);
        myProducts.setUnits(units);

        ObservableList highlightedProductsRecord, allProductsRecords;

        highlightedProductsRecord = tableProducts.getSelectionModel().getSelectedItems();

        System.out.println("name is : " + name);
        System.out.println("the ID IS: " + theId);
        String query = "UPDATE Products_tbl set "
                + "description ='"
                + Products_description + "',"
                + "expiry_date ='"
                + Products_expiry + "',"
                + "price ='"
                + price + "',"
                + "quantity ='"
                + quantity + "',"
                + "units ='"
                + units + "',"
                + "product_date ='"
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

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllProducts.fxml"));
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
        String name = name_box.getText();

        if ((name.trim().length() == 0) || (name == "") || (name.trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please Highlight A Class Room Record");

            alert.showAndWait();
            System.clearProperty(name);
        }
        //myProducts.setId(rs.getString("id"));

        ObservableList highlightedProductsRecord, allProductsRecords;
        allProductsRecords = tableProducts.getItems();
        highlightedProductsRecord = tableProducts.getSelectionModel().getSelectedItems();
        System.out.println("name is : " + name);

        String query = "DELETE FROM Products_tbl "
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

        Parent TodayReport_page_parent = FXMLLoader.load(getClass().getResource("AllProducts.fxml"));
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
