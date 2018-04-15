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
public class Transaction {

    private String amount_id;
    private String amount_name;
    private String amount_details;
    private String amount_date;
    private String txnDay;
    private String txnMonth;
    private String txnYear;
    private String formated_date;
    private String amount;

    public Transaction() {
    }

    public Transaction(String amount_id, String amount_name, String amount_details, String amount_date, String txnDay, String txnMonth, String txnYear, String formated_date, String amount) {
        this.amount_id = amount_id;
        this.amount_name = amount_name;
        this.amount_details = amount_details;
        this.amount_date = amount_date;
        this.txnDay = txnDay;
        this.txnMonth = txnMonth;
        this.txnYear = txnYear;
        this.formated_date = formated_date;
        this.amount = amount;
    }

    public String getAmount_id() {
        return amount_id;
    }

    public void setAmount_id(String amount_id) {
        this.amount_id = amount_id;
    }

    public String getAmount_name() {
        return amount_name;
    }

    public void setAmount_name(String amount_name) {
        this.amount_name = amount_name;
    }

    public String getAmount_details() {
        return amount_details;
    }

    public void setAmount_details(String amount_details) {
        this.amount_details = amount_details;
    }

    public String getAmount_date() {
        return amount_date;
    }

    public void setAmount_date(String amount_date) {
        this.amount_date = amount_date;
    }

    public String getTxnDay() {
        return txnDay;
    }

    public void setTxnDay(String txnDay) {
        this.txnDay = txnDay;
    }

    public String getTxnMonth() {
        return txnMonth;
    }

    public void setTxnMonth(String txnMonth) {
        this.txnMonth = txnMonth;
    }

    public String getTxnYear() {
        return txnYear;
    }

    public void setTxnYear(String txnYear) {
        this.txnYear = txnYear;
    }

    public String getFormated_date() {
        return formated_date;
    }

    public void setFormated_date(String formated_date) {
        this.formated_date = formated_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    

}
