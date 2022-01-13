package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Country;
import com.apptpro.apptpro.Models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstLevelDivisionsDAO implements DAO<FirstLevelDivision>{


    /**
     * The existing connection/session of the database
     */
    Connection connection = JDBC.getConnection();

    /**
     * Observable list of all First level divisions
     */
    ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();


    public ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() {
        try (Statement st =  connection.createStatement()) {
            String query = "SELECT * FROM first_level_divisions;";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                FirstLevelDivision newFirstLevelDivision = new FirstLevelDivision(rs.getInt("Division_ID"),rs.getString("Division"),rs.getInt("Country_ID"));
                allFirstLevelDivisions.add(newFirstLevelDivision);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allFirstLevelDivisions;
    }

    @Override
    public void save(FirstLevelDivision firstLevelDivision) {

    }

    @Override
    public boolean update(FirstLevelDivision firstLevelDivision) {
        return false;
    }

    @Override
    public boolean delete(FirstLevelDivision firstLevelDivision) {
        return false;
    }
}
