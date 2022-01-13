package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Country;
import com.apptpro.apptpro.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DAO of all Countries in the database
 */
public class CountriesDAO implements DAO<Country> {
    /**
     * All the Country objects from the database
     */
    ObservableList<Country> allCountries = FXCollections.observableArrayList();
    /**
     * The JDBC connection/session to the database
     */
    Connection connection = JDBC.getConnection();
    /**
     * Gets all the countries in an ObservableList
     * @return An ObservableList of all countries
     */
    public ObservableList<Country> getAllCountries() {
        try (Statement st =  connection.createStatement()) {
            String query = "SELECT * FROM countries;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Country newCountry = new Country(rs.getInt("Country_ID"),rs.getString("Country"));
                allCountries.add(newCountry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allCountries;
    }




    @Override
    public void save(Country country) {

    }

    @Override
    public boolean update(Country country) {
        return false;
    }



    @Override
    public boolean delete(Country country) {
        return false;
    }
}
