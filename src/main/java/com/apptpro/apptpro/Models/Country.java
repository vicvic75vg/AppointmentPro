package com.apptpro.apptpro.Models;

public class Country {
    private int countryID;
    private String country;


    /**
     * Creates a new Country instance
     * @param countryID The ID of the Country
     * @param country The name of the Country
     */
    public Country(int countryID, String country) {
        setCountry(country);
        setCountryID(countryID);
    }

    /**
     * Gets the ID of the Country, as seen in the database
     * @return The ID of the country
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Gets the name of the country
     * @return The name of the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country
     * @param country The name of the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the ID of the country as seen in the database
     * @param countryID The ID of the country
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Overiding the toString method for populating ComboBox
     * @return
     */
    public String toString() {
      return country;
    }
}
