package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The UserLogin class that gets the open
 * connection session and attempts matching user
 * credentials.
 */
public class UserLogin {


    /**
     * The currentUser. This will be null if no user
     * matches the input fields OR loginUser() not called yet.
     */
    private User currentUser;

    /**
     *  Checks whether the username and password entered match
     *  in the users' database.
     * @return Boolean whether the login was successful or not.
     */
    public boolean loginUser(String username, String password) {
        String query = String.format("SELECT User_Name,Password FROM users WHERE User_Name='%s'" +
                " AND Password='%s';", username,password);

        Connection cn = JDBC.getConnection();
        try (Statement statement = cn.createStatement()) {

            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                setCurrentUser(new User(username));
                statement.close();
                return true;
            }
            return false;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Returns the current user object.
     * @return The current user object. Returns null if no User is set.
     */
    public User getCurrentUser() { return this.currentUser; }

    /**
     * Sets the current user to a new User object
     * @param user A new User object to set.
     */
    public void setCurrentUser(User user){ this.currentUser = user; };
}
