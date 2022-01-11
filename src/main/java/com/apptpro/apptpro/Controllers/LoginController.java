package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DBO.UserLogin;
import com.apptpro.apptpro.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.apptpro.apptpro.Controllers.MainScreenController;

import java.io.IOException;
import java.time.ZoneId;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The controller for all elements on the main screen, both
 * logging in and setting locale.
 */
public class LoginController implements Initializable {
    /**
     * The username Text Field
     */
    @FXML
    private TextField usernameField;
    /**
     * The password TextField
     */
    @FXML
    private TextField passwordField;
    /**
     * The login Button
     */
    @FXML
    private Button loginButton;

    /**
     * The label used to display the users
     * normalized region on the login screen.
     */
    @FXML
    private Label localeLabel;

    /**
     * Attempts login using the username and password fields.
     *
     * This method will first check to see if the fields are empty. If not,
     * an error will appear.
     * Once the fields are checked, then the username and password will be checked against
     * the database. If not found, an error will display. If found and credentials check
     * out, the user will enter the application.
     */
    @FXML
    private void onLogin() throws IOException {
        if(passwordField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty())  {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Password or username cannot be empty!");
            alert.showAndWait();
            return;
        }

        UserLogin userLogin = new UserLogin();
        boolean isUserLoggedIn = userLogin.loginUser(usernameField.getText().trim(), passwordField.getText().trim());

        if(!isUserLoggedIn) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not not user in! Credentials invalid!");
            alert.showAndWait();
            return;
        }
        System.out.println("User logged in successfully...");
        Main.changeScene(MainScreenController.class,"MainScreen.fxml","Home");
    }

    /**
     * Gets the ZoneId message to return to the user's login
     * screen
     * @return The ZoneId String.
     */
    private String getZoneIdMessage() {
        return String.format("You are in %s ", ZoneId.systemDefault().normalized());
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale loc = new Locale("fr","FR");
        rb = ResourceBundle.getBundle("login",loc);
        passwordField.setPromptText(rb.getString("password"));
        usernameField.setPromptText(rb.getString("username"));
        loginButton.setText(rb.getString("login"));
        localeLabel.setText(getZoneIdMessage());
    }
}