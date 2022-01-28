package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactsDAO {

    private Connection connection = JDBC.getConnection();
    private ObservableList<Contact> allContacts = FXCollections.observableArrayList();


    /**
     * Gets all contacts in the database and creates new Contact objects for each returned row
     * @return All contact objects
     */
    public ObservableList<Contact> getAllContacts() {
        try(Statement st = connection.createStatement()) {
            String query = "SELECT * FROM contacts;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email")
                );
                allContacts.add(contact);
            }


        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return allContacts;
    }




}
