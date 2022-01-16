package com.apptpro.apptpro.Models;

public class Contact {
    private int contactID;
    private String contactName;
    private String contactEmail;

    public Contact(int contactID,String contactName,String contactEmail) {
        setContactID(contactID);
        setContactName(contactName);
        setContactEmail(contactEmail);
    }


    /**
     * Gets the ID of the contact
     * @return The ID of the contact
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets the ID of the contact
     * @param contactID The ID of the contact
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Gets the email of the contact
     * @return The contacts' email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Gets the name of the contact
     * @return The name of the contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the email belonging to the contact
     * @param contactEmail The email of the contact
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Sets the name to be used for the contact
     * @param contactName The contacts name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }



    /**
     * Overriding toString method to satisfy the PropertyValueFactory requirement for
     * populating tables
     * @return The name of the contact
     */
    public String toString() {
        return getContactName();
    }
}
