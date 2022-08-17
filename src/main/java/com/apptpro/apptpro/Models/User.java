package com.apptpro.apptpro.Models;

/**
 * User model
 */
public class User {
    /**
     * String representing the name of the user
     */
    private String userName;

    /**
     * String representation of the user password
     */
    private String userSaltedPassword;

    /**
     * First name of the user
     */
    private String firstName;

    /**
     * Last name of the user
     */
    private String lastName;

    /**
     *
     */
    private String userSalt;


    /**
     * Constructor that creates a new user object
     * @param userName The username of the user
     */
    public User(String userName, String userSaltedPassword, String firstName, String lastName, String userSalt) {
        this.userName = userName;
        this.userSaltedPassword = userSaltedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userSalt = userSalt;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public String getUserSaltedPassword() {
        return userSaltedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }
}
