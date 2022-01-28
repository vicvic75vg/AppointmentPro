package com.apptpro.apptpro.Models;
import java.lang.String;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Date;
public class Customer{

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private String createdDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String divisionID;
    private FirstLevelDivision division;
    private Country country;

    /**
     * Creates a new Customer object
     * @param customerID The Id of the customer
     * @param customerName The customer's name
     * @param customerAddress The address of the customer
     * @param customerPostalCode The postal code of the customer address
     * @param customerPhone The phone number of the customer
     * @param createdDate The string of the date when the customer was
     *                    added to the database
     * @param createdBy The method by which the customer was created
     * @param lastUpdated The string of the datetime when the customer was last updated in the database
     * @param lastUpdatedBy The method by which the customer data was updated in the database
     * @param division The FirstLevelDivision pertaining to the customer's address
     * @param country The Country object by which the customer FirstLevelDivision pertains to
     */
    public Customer(int customerID, String customerName, String customerAddress,
                    String customerPostalCode, String customerPhone, String createdDate,
                    String createdBy, String lastUpdated, String lastUpdatedBy,
                    FirstLevelDivision division, Country country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.division = division;
        this.country  = country;

    }

    /**
     * Sets the ID of the customer
     * @param customerID The customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Sets the name of the customer
     * @param customerName The name of the customer
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Sets the address of the customer
     * @param customerAddress The customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Sets the phone number of the customer
     * @param customerPhone The phone number of the customer
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * Sets the postal code of the customer
     * @param customerPostalCode The customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * Returns the ID of the customer
     * @return Customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Returns the name of the customer
     * @return The name of the customer
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the address of the customer
     * @return The customers address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Gets the phone number of the customer
     * @return The customers phone number
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Gets the postal code of the customer
     * @return The customers postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * Gets the date on which the Customer was created.
     * @return A string of the date the customer was created
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Gets the date of the last time the customer was updated
     * @return The date of the time the customer was last updated
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the noun of the creator of this customer
     * @return The noun of the creator
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets the date of the last time the customer was updated
     * @return The date of the last time the customer was updated
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Gets the ID of the division
     * @return The ID of the division
     */
    public int getDivisionID() {
        return getDivision().getDivisionID();
    }
    /**
     * Gets the Division ID of the customer
     * @return The division ID
     */
    public FirstLevelDivision getDivision() {
        return division;
    }

    /**
     * Gets the Country object belonging to the customer
     * @return The Country object of the customers address
     */
    public Country getCountry() {
        return country;
    }
}
