package com.apptpro.apptpro.Models;

public class FirstLevelDivision {

    private int divisionID;
    private String division;

    private int countryID;


    /**
     * Creates a FirstLevelDivision object
     * @param divisionID The Division ID
     * @param division The name of the division
     * @param countryID The ID of the country for the division
     */
    public FirstLevelDivision(int divisionID,String division,int countryID) {
         setDivisionID(divisionID);
         setDivision(division);
         setCountryID(countryID);
    }
    /**
     * Gets the ID of the division
     * @return The divison ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Gets the name of the division
     * @return The name of the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Get the ID of the country
     * @return The ID of the country
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Sets the name of the divison
     * @param division The name of the division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Sets the ID of the divison
     * @param divisionID The ID of the division
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Overrides the toString method to return the name of the division
     * @return The name of the division
     */
    public String toString() {
        return division;
    }
}
