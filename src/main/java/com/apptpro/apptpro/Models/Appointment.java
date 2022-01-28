package com.apptpro.apptpro.Models;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Appointment{

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String createdDate;
    private int customerID;
    private int userID;
    private int contactID;
    private Contact contact;
    private String formattedEnd;
    private String formattedStart;


    public Appointment(int appointmentID, String title, String description, Contact contact, String location,
                       String type, String start, String end, String createdDate, int customerID, int userID) {
        setAppointmentID(appointmentID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
        setCustomerID(customerID);
        setUserID(userID);
        setCreatedDate(createdDate);
        setContact(contact);

        setFormattedEnd();
        setFormattedStart();
    }

    public String getFormattedStart() {
        return DateTimeFormatting.UTCStringToDefaultTimeFormat(start,true,true);
    }
    public void setFormattedStart() {
        this.formattedStart = DateTimeFormatting.UTCStringToDefaultTimeFormat(start,true, true);
    }

    public String getFormattedEnd() {
        return DateTimeFormatting.UTCStringToDefaultTimeFormat(end,true,true);
    }
    public void setFormattedEnd() {
        this.formattedEnd = DateTimeFormatting.UTCStringToDefaultTimeFormat(end,true,true);
    }


    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    /**
     * Sets the Contact model assigned to this appointment
     * @param contact The Contact model
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Retrieves the Contact model assigned to this Appointment
     * @return Contact model
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Gets the unique ID of the appointment
     * @return Unique appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the description of the appointment
     * @return The appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the end date time of the appointment
     * @return A string representing the end date time
     */
    public String getEnd(boolean withT) {
        if(withT) {
            return end.replaceAll("\\s+","T");
        }
        return end;
    }

    /**
     * Gets the location of the appointment
     * @return A string of the location of the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the start DateTime of the appointment
     * @return A string representing the start DateTime
     */
    public String getStart(boolean withT) {
        if(withT) {
            return start.replaceAll("\\s+","T");
        }
        return start;
    }

    /**
     * Gets the title of the appointment
     * @return The title of the appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the type of appointment
     * @return The type of appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the ID of the appointment
     * @param appointmentID The ID of the appointment
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Sets the description of the appointment
     * @param description The appointment description
     */
    public void setDescription(String description)  {
        if(description.isEmpty()) {
            throw new StringIndexOutOfBoundsException();
        }
        this.description = description;
    }

    /**
     * Sets the end DateTime in a string format
     * @param end The ending DateTime
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Sets the location of the appointment
     * @param location The location of the appointment
     */
    public void setLocation(String location) {
        if(location.isEmpty()) throw new StringIndexOutOfBoundsException();
        this.location = location;
    }

    /**
     * Sets the start DateTime in a string format
     * @param start The start DateTime
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * Sets the title of the appointment
     * @param title The title of the appointment
     */
    public void setTitle(String title) {
        if(title.isEmpty()) throw new StringIndexOutOfBoundsException();
        this.title = title;
    }

    /**
     * Sets the type of the appointment
     * @param type The appointment type
     */
    public void setType(String type) {
        if(type.isEmpty()) throw new StringIndexOutOfBoundsException();
        this.type = type;
    }

    /**
     * Gets the id of the related contact
     * @return The contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * Gets the id of the related customer
     * @return
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Gets the ID of the user
     * @return The ID of the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the contact
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Sets the ID of the customer
     * @param customerID The ID of the customer
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Sets the ID of the User
     * @param userID The ID of the User
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the date when the appointment was originally created
     * @return The date created as a string
     */
    public String getCreatedDate() {
        return createdDate;
    }


}
