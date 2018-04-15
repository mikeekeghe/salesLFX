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
public class Appointment {

    private String appointment_id;
    private String full_name;
    private String appointment_date;
    private String appointment_name;
    private String amount;
    private String notes;
    private String type;
    private String date_time;
    private String invoiceNo;
    private String customer;

    public Appointment(String appointment_id, String full_name, String appointment_date, String appointment_name, String amount, String notes, String type, String date_time, String invoiceNo, String customer) {
        this.appointment_id = appointment_id;
        this.full_name = full_name;
        this.appointment_date = appointment_date;
        this.appointment_name = appointment_name;
        this.amount = amount;
        this.notes = notes;
        this.type = type;
        this.date_time = date_time;
        this.invoiceNo = invoiceNo;
        this.customer = customer;
    }

  

    public Appointment() {
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_name() {
        return appointment_name;
    }

    public void setAppointment_name(String appointment_name) {
        this.appointment_name = appointment_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

   
}
