/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesline.entities;

/**
 *
 * @author Eche Michael
 */
public class Products {

    private String id;
    private String name;
    private String description;
    private String product_date;
    private String expiry_date;
    private String price;
    private String units;    
    private String quantity;

    public Products() {
    }

    public Products(String id, String name, String description, String product_date, String expiry_date, String price, String units, String quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.product_date = product_date;
        this.expiry_date = expiry_date;
        this.price = price;
        this.units = units;
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    

}
