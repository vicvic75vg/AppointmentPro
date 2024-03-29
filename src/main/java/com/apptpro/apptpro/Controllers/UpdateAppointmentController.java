package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.DAO.ContactsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import com.apptpro.apptpro.Models.DateTimeFormatting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * The controller for updating Appointment objects in the MySQL database.
 * On call, the controller initializes from a required initialize method
 */
public class UpdateAppointmentController implements Initializable {


    private Appointment selectedAppointment;
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
    private ZonedDateTime apptEnd ;
    private ZonedDateTime apptStart;


    /**
     * The constructor for creating a new UpdateAppointmentController.
     * @param appointment The appointment object to update
     */
    public UpdateAppointmentController(Appointment appointment) {
        this.selectedAppointment = appointment;
        LocalTime hour = LocalTime.MIN;
       for(int i = 0;i< 48;i++)  {
           allTimes.add(hour);
           hour = hour.plusMinutes(30);
       }
}
    /**
     * Checks for any misconfigurations in the fields and handles them appropriately.
     * Attempts an SQL INSERT into the database.
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
        } catch(DateTimeParseException ex) {
            ex.printStackTrace();
            return;
        }




        //TEST FOR ZONE ID
        ZonedDateTime zonedStart = ZonedDateTime.of(newStart,ZoneId.systemDefault());
        ZonedDateTime zonedEnd = ZonedDateTime.of(newEnd,ZoneId.systemDefault());


        ZonedDateTime startUTC =  ZonedDateTime.ofInstant(zonedStart.toInstant(),ZoneId.of("UTC"));
        ZonedDateTime endUTC =  ZonedDateTime.ofInstant(zonedEnd.toInstant(),ZoneId.of("UTC"));




        if(startUTC.isAfter(endUTC)) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText("The chosen start date and time occurs after the end date and time!");
           alert.showAndWait();
           return;

       }
        System.out.println("Start UTC " + startUTC);


        //After struggling with understanding why the given hour in the sample database was off by one hour,
        //I realized that the sample data was created during Daylight Savings Time
       if((startUTC.getHour() >= 3 && startUTC.getHour() <= 12) || endUTC.getHour() >= 3 && endUTC.getHour() <= 12) {

           if(endUTC.getHour() == 3 && endUTC.getMinute() != 0) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setContentText("ERROR: The head office is closed during the selected time.");
               alert.showAndWait();
               return;
           } else if(endUTC.getHour() > 3 && endUTC.getHour() <= 12){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setContentText("ERROR: The head office is closed during the selected time.");
               alert.showAndWait();
               return;
           } else if(startUTC.getHour() >= 3 && startUTC.getHour() <= 12) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setContentText("ERROR: The head office is closed during the selected time.");
               alert.showAndWait();
               return;
           }
       }
        try {
            Appointment appointment = new Appointment(Integer.parseInt(appointmentIDTextField.getText()), titleTextField.getText().trim(), descriptionTextArea.getText().trim(),
                    contactComboBox.getSelectionModel().getSelectedItem(),locationTextField.getText().trim(), typeTextField.getText().trim(),
                    startUTC.toLocalDateTime().toString(),endUTC.toLocalDateTime().toString(),selectedAppointment.getCreatedDate(),Integer.parseInt(customerIDTextField.getText().trim()),Integer.parseInt(userIDTextField.getText().trim()));
            AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
            String isSuccess = appointmentsDAO.update(appointment);
            if(Objects.equals(isSuccess,"True")) {
                toApptScreen();
            } else if(Objects.equals(isSuccess,"Booked")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Customer already booked during the selected time.");
                alert.showAndWait();
            } else if(Objects.equals(isSuccess,"Does Not Exist")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Customer ID does not exist.");
                alert.showAndWait();
            } else if(Objects.equals(isSuccess,"False")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.showAndWait();
            }

        } catch(StringIndexOutOfBoundsException | NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be empty!");
            alert.showAndWait();
        } catch(SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The user ID or customer ID entered are invalid,\n as they don't correspond to " +
                    "a record.");
            alert.showAndWait();
        }
    }

    /**
     *  Changes the scene to the MainScreen and does not update
     *  the selected Appointment.
     */
    @FXML
    private void toApptScreen() {
        try {
            Main.changeSceneToAppointments();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes and populates all fields with existing appointment data and
     * populates the contact and times fields
     * @param url URL string required by Initialize
     * @param resourceBundle resourceBundle required by Initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        ContactsDAO contactsDAO = new ContactsDAO();
        appointmentIDTextField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        titleTextField.setText(selectedAppointment.getTitle());
        descriptionTextArea.setText(selectedAppointment.getDescription());
        locationTextField.setText(selectedAppointment.getLocation());
        contactComboBox.setItems(contactsDAO.getAllContacts());
        contactComboBox.getSelectionModel().select(selectedAppointment.getContact());
        typeTextField.setText(selectedAppointment.getType());
        customerIDTextField.setText(String.valueOf(selectedAppointment.getCustomerID()));
        userIDTextField.setText(String.valueOf(selectedAppointment.getUserID()));


        startDatePicker.setValue(LocalDateTime.parse(selectedAppointment.getStart(true)).toLocalDate());
        endDatePicker.setValue(LocalDateTime.parse(selectedAppointment.getEnd(true)).toLocalDate());


        appointmentIDTextField.setDisable(true);
        startTimeBox.setValue(LocalDateTime.parse(DateTimeFormatting.UTCStringToDefaultTimeFormat(selectedAppointment.getStart(true),false,false)).toLocalTime());
        endTimeBox.setValue(LocalDateTime.parse(DateTimeFormatting.UTCStringToDefaultTimeFormat(selectedAppointment.getEnd(true),false,false)).toLocalTime());
        startTimeBox.setItems(allTimes);
        endTimeBox.setItems(allTimes);

    }
}
