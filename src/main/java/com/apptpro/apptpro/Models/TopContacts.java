package com.apptpro.apptpro.Models;

public class TopContacts {
    /**
     * String representing the name of the contact
     */
    private String contactName;
    /**
     * Integer representing the amount of appointments for the contact
     */
    private int appointmentsCount;

    /**
     * Creates a new TopContact object that represents the contact and their
     * appointment count
     * @param contactName The name of the contact
     * @param appointmentsCount The integer representing the amount of appointments for this contact
     */
    public TopContacts(String contactName, int appointmentsCount) {
        this.contactName = contactName;
        this.appointmentsCount = appointmentsCount;
    }


    /**
     * Gets the name of the contact, implemented to
     * satisfy table population.
     * @return the name of the contact
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Gets the count of all appointments pertaining to the contact
     * @return integer of appointment count for the contact
     */
    public int getAppointmentsCount() {
        return appointmentsCount;
    }
}
