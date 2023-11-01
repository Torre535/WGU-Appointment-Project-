package torres.wguappointmentapp.Model;

import java.time.LocalDateTime;

public class Customer {

    // Fields provided by the ERD table that are within the database
    private String customerCountry;
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;
    private String divisionName;

    // Constructor for Customers
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String customerCountry) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.customerCountry = customerCountry;
    }

    // Getter and Setter methods for Customers fields

    //Customer ID
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    // Customer Name
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    // Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    //Postal Code
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    //Phone Number
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    //Create Date
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    //Created By
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    //Last Updated
    public LocalDateTime getLastUpdate() {
        return lastUpdate; }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate; }


    // Last updated by
    public String getLastUpdatedBy() {
        return lastUpdatedBy; }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy; }


    //Division ID
    public int getDivisionId() {
        return divisionId; }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId; }

    // Country
    public String getCustomerCountry(){
        return customerCountry;
    }

    public void setCustomerCountry() {
        this.customerCountry = customerCountry;
    }

    //Return Name
    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    @Override
    public String toString() {
        return String.valueOf(customerId);
    }

}
