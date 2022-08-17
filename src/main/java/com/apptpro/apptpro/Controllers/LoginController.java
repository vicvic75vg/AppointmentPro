package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.DAO.UserManager;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;

import java.net.URL;
import java.util.Date;
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
     * The Resource Bundle loaded based on the users default locale
     */
    private ResourceBundle rb = ResourceBundle.getBundle("login",Locale.getDefault());
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
    private void onLogin() throws FileNotFoundException {
        if(passwordField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty())  {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(this.rb.getString("errorFieldEmpty"));
            alert.showAndWait();
            return;
        }

        UserManager userLogin = new UserManager();
        boolean isUserLoggedIn = userLogin.loginUser(usernameField.getText().trim(), passwordField.getText().trim());

        //Open file
        PrintWriter out =  new PrintWriter(new FileOutputStream("src/login_activity.txt", true));


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        if(!isUserLoggedIn) {
            //An error occurred when logging in
            String logActivityMessage = String.format("%s attempted login on %s and was not successful.\n",usernameField.getText().trim(), formatter.format(date));
            out.append(logActivityMessage);

            out.close();


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(this.rb.getString("errorLoggingIn"));
            alert.showAndWait();
            return;
        }

        String logActivityMessage = String.format("%s attempted login on %s and was successful.\n",usernameField.getText().trim(), formatter.format(date));

        out.append(logActivityMessage);
        out.close();

        System.out.println("User logged in successfully...");
        Appointment appointment = hasAppointmentsSoon();
        if(appointment != null) {

            //Convert appointment time to LocalTimeZone
            LocalDateTime apptStart  = LocalDateTime.parse(appointment.getStart(false).replaceAll("\\s+","T"));
            LocalDateTime apptEnd = LocalDateTime.parse(appointment.getEnd(false).replaceAll("\\s+","T"));

            ZonedDateTime zonedDateTimeStart = ZonedDateTime.of(apptStart,ZoneId.of("UTC"));
            ZonedDateTime zonedDateTimeEnd = ZonedDateTime.of(apptEnd,ZoneId.of("UTC"));



            zonedDateTimeStart = ZonedDateTime.ofInstant(zonedDateTimeStart.toInstant(),ZoneId.systemDefault());
            zonedDateTimeEnd = ZonedDateTime.ofInstant(zonedDateTimeEnd.toInstant(),ZoneId.systemDefault());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("You have an appointment soon! \n The appointment ID is: %d and it starts\n at %s and ends at %s",
                    appointment.getAppointmentID(),zonedDateTimeStart.toLocalTime(),zonedDateTimeEnd.toLocalTime()));
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No appointments soon.");
            alert.showAndWait();
        }
        try {
            Main.changeSceneToMainScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onSignup() {
        try {
            Main.changeSceneToSignup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks to see if any appointments exists. If they do,
     * an Appointment object will be returned.
     * LAMBDA EXPRESSION USED TO FIND ANY APPOINTMENTS WITHIN 15 MINUTES
     * @return An appointment within 15 minutes of the current time
     */
    private Appointment hasAppointmentsSoon() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        ObservableList<Appointment> allAppts = appointmentsDAO.getAllAppointments();
        allAppts = allAppts.filtered(appointment -> {
            String start = appointment.getStart(true);
            LocalDateTime apptTime = LocalDateTime.parse(start);
            return (apptTime.isBefore(now) && apptTime.isAfter(LocalDateTime.now(ZoneOffset.UTC)));
        });
        if(allAppts.size() == 0) {
            return null;
        } else {
            return allAppts.get(0);
        }
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

        System.out.println(ZoneId.systemDefault());
        passwordField.setPromptText(this.rb.getString("password"));
        usernameField.setPromptText(this.rb.getString("username"));
        loginButton.setText(this.rb.getString("login"));
        localeLabel.setText(getZoneIdMessage());
    }
}