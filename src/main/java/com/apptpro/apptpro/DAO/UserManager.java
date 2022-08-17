package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.User;
import com.apptpro.apptpro.SecurePasswordStorage;

/**
 * The UserLogin class that gets the open
 * connection session and attempts matching user
 * credentials.
 */
public class UserManager {


    /**
     * The currentUser. This will be null if no user
     * matches the input fields OR loginUser() not called yet.
     */
    private User currentUser;

    public UserManager() {

    }

    /**
     *  Checks whether the username and password entered match
     *  in the users' database.
     * @return Boolean whether the login was successful or not.
     */
    public boolean loginUser(String username, String password) {
        //Create new instance of SecurePasswordStorage, compare password and authenticate
        SecurePasswordStorage securePasswordStorage = new SecurePasswordStorage();
        try {
            User user = securePasswordStorage.authenticateUser(username,password);
            if (user != null) {
                setCurrentUser(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Signs up user to the application
     * @return Boolean whether the sign-up is successful or not.
     */
    public boolean signupUser(String firstName, String lastName, String username, String password)  {
        if (firstName.trim().isEmpty() || lastName.isEmpty()
                || username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }
        SecurePasswordStorage securePasswordStorage = new SecurePasswordStorage();
        try {
            return securePasswordStorage.signUp(username,password,firstName,lastName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
