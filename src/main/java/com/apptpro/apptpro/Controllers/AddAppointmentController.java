package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.DAO.ContactsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIDTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField locationTextField;
    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField userIDTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startTimeBox;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> endTimeBox;

    /**
     * An observable list of all the times that
     * a user can choose from, in 24-hour format appropriate for their region.
     */
    private ObservableList<LocalTime> allTimes = FXCollections.observableArrayList();

    /**
     * Constructor for AddAppointment. In this constructor,
     * the initial times are put into the private field LocalTime list
     */
    public AddAppointmentController() {
        LocalTime hour = LocalTime.MIN;
        for(int i = 0;i< 48;i++)  {
            allTimes.add(hour);
            hour = hour.plusMinutes(30);
        }
    }
    /**
     * Checks the fields for any misconfigured fields and attempts to add the
     * appointment to the database
     */
    @FXML
    private void saveUpdate() {
        LocalDateTime newStart;
        LocalDateTime newEnd;

        LocalDate startDate =  startDatePicker.getValue();
        LocalTime startTime = startTimeBox.getSelectionModel().getSelectedItem();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeBox.getSelectionModel().getSelectedItem();
        try {
            newStart = LocalDateTime.of(startDate,startTime);
            newEnd = LocalDateTime.of(endDate,endTime);
        } catch(DateTimeParseException | NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be empty!");
            alert.showAndWait();
            return;
        }
        ZonedDateTime zonedStart = ZonedDateTime.of(newStart, ZoneId.systemDefault());
        ZonedDateTime zonedEnd = ZonedDateTime.of(newEnd,ZoneId.systemDefault());

        ZonedDateTime startUTC =  ZonedDateTime.ofInstant(zonedStart.toInstant(),ZoneId.of("UTC"));
        ZonedDateTime endUTC =  ZonedDateTime.ofInstant(zonedEnd.toInstant(),ZoneId.of("UTC"));

        if(startUTC.isAfter(endUTC)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The chosen start date and time occurs after the end date and time!");
            alert.showAndWait();
            return;
        }
        //checks against office utc hours, which translate from 3AM to 12PM
        if((startUTC.getHour() >= 3 && startUTC.getHour() <= 12) || endUTC.getHour() >= 2 && endUTC.getHour() <= 12) {

            System.out.println(":in");
            if(endUTC.getHour() == 3 && endUTC.getMinute() != 0) {
                System.out.println("in2");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR: The head office is closed during the selected time.");
                alert.showAndWait();
                return;
            } else if(endUTC.getHour() > 3 && endUTC.getHour() <= 12){
                System.out.println("in3");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR: The head office is closed during the selected time.");
                alert.showAndWait();
                return;
            } else if(startUTC.getHour() >= 3 && startUTC.getHour() <= 13) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR: The head office is closed during the selected time.");
                alert.showAndWait();
                return;
            }
        }

        try {
            String isSuccess = "";
            AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

            //Create appointment using the UTC end and start time
            Appointment appointment = new Appointment(appointmentsDAO.generateID(), titleTextField.getText().trim(), descriptionTextArea.getText().trim(),
                    contactComboBox.getSelectionModel().getSelectedItem(),locationTextField.getText().trim(), typeTextField.getText().trim(),
                    startUTC.toLocalDateTime().toString(),endUTC.toLocalDateTime().toString(),Instant.now().toString(),Integer.parseInt(customerIDTextField.getText().trim()),Integer.parseInt(userIDTextField.getText().trim()));
            try {
                isSuccess = appointmentsDAO.add(appointment);
            } catch(SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User ID or Customer ID does not exist!!");
                alert.showAndWait();
                ex.printStackTrace();
            }
            if(Objects.equals("True",isSuccess)) {
                toApptScreen();
            } else if(Objects.equals("Booked",isSuccess)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("ERROR: Customer of ID %d is already booked for an appointment.", appointment.getCustomerID()));
                alert.showAndWait();
            } else if(Objects.equals("Does Not Exist",isSuccess)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The User Id of Customer ID does not exist!");
                alert.showAndWait();
            } else if(Objects.equals("False",isSuccess)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Customer ID is already booked for an appointment during the selected time.");
                alert.showAndWait();
            }

        } catch(StringIndexOutOfBoundsException | NumberFormatException | NullPointerException ex) {
            try {
                Integer.parseInt(userIDTextField.getText().trim());
                Integer.parseInt(customerIDTextField.getText().trim());
            } catch(NumberFormatException ex2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User ID or Customer ID field is not a valid integer!");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be empty!");
            alert.showAndWait();
        }
    }

    /**
     * Changes the current stage to the Appointment main stage
     */
    @FXML
    private void toApptScreen() {
        try {
            Main.changeSceneToAppointments();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ContactsDAO contactsDAO = new ContactsDAO();
        contactComboBox.setItems(contactsDAO.getAllContacts());

        appointmentIDTextField.setDisable(true);
        startTimeBox.setItems(allTimes);
        endTimeBox.setItems(allTimes);
    }
}
