package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import com.apptpro.apptpro.Models.DateTimeFormatting;
import com.apptpro.apptpro.Models.Tables;
import com.apptpro.apptpro.TableHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    //TableViews

    @FXML
    private TableView<Appointment> apptTable;




    //Columns for "All" appointments table
    @FXML
    private TableColumn<Appointment,Integer> appointmentID;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment,String> description;
    @FXML
    private TableColumn<Appointment,String> location;
    @FXML
    private TableColumn<Appointment, Contact> contact;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment,String> start;
    @FXML
    private TableColumn<Appointment,String> end;
    @FXML
    private TableColumn<Appointment,Integer> customerID;
    @FXML
    private TableColumn<Appointment,Integer> userID;

    @FXML
    private ComboBox<String> monthsCombo;
    @FXML
    private RadioButton radioButtonWeek;
    @FXML
    private RadioButton radioButtonAll;

    private ObservableList<String> allMonths = FXCollections.observableArrayList();

    /**
     * Changes the main stage to view all customers
     */
    @FXML
    private void viewCustomers() {
        try {
            Main.changeSceneToMainScreen();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes the view to the Appointments view
     */
    @FXML
    private void addAppointment() {
        try {
            Main.changeSceneToAddAppointment();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes the view to the UpdateAppointments view and passes in the selected
     * appointment to the controller
     */
    @FXML
    private void updateAppointment() {
        Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
        if(appointment == null) {
            return;
        }
        try {
            Main.changeSceneToUpdateAppointments(appointment);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Attempts to delete an appointment from the table and database
     */
    @FXML
    private void deleteAppointment() {
        Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
        if(appointment == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(String.format("Are you sure you want to cancel appointment \n of type %s,with appointment ID %d?", appointment.getType(),appointment.getAppointmentID()));
        alert.showAndWait();
        if(alert.getResult().getText().equals("Cancel")) {
            return;
        }
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        boolean isSuccess = appointmentsDAO.deleteAppointment(appointment);
        if(!isSuccess) {
            Alert failure = new Alert(Alert.AlertType.ERROR);
            failure.setContentText("An error has occurred, the appointment could not be deleted.");
            failure.showAndWait();
            return;
        }
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setContentText(String.format("Appointment of type %s and ID of %d has been deleted.", appointment.getType(),appointment.getAppointmentID()));
        successAlert.showAndWait();
        apptTable.setItems(appointmentsDAO.getAllAppointments());
        apptTable.refresh();
    }

    /**
     * Populates the table with appointments in the next 7 days
     */
    @FXML
    private void handleRadioWeek() {
        if(!radioButtonWeek.isSelected()) {
            radioButtonWeek.setSelected(true);
            radioButtonAll.setSelected(false);
            monthsCombo.getSelectionModel().select(null);
        }
        radioButtonWeek.setSelected(true);
        radioButtonAll.setSelected(false);
        monthsCombo.getSelectionModel().select(null);


        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        apptTable.setItems(appointmentsDAO.getByCurrentWeek());
        apptTable.refresh();

    }

    /**
     * Populates the table by the selected month
     */
    @FXML private void handleComboMonth() {
        if(monthsCombo.getSelectionModel().getSelectedItem() != null) {
            radioButtonAll.setSelected(false);
            radioButtonWeek.setSelected(false);
        }
        radioButtonAll.setSelected(false);
        radioButtonWeek.setSelected(false);
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        apptTable.setItems(appointmentsDAO.getByMonth(monthsCombo.getSelectionModel().getSelectedItem()));
        apptTable.refresh();
    }

    /**
     * Changes the current view to the Reports view
     */
    @FXML
    private void viewReports() {
        try {
            Main.changeSceneToReports();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Populates the table with all the appointments
     */
    @FXML
    private void handleRadioAll() {
        if(!radioButtonAll.isSelected()) {
            radioButtonAll.setSelected(true);
            radioButtonWeek.setSelected(false);
            monthsCombo.setValue(null);
        }
        radioButtonAll.setSelected(true);

        radioButtonWeek.setSelected(false);
        monthsCombo.setValue(null);
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

        apptTable.setItems(appointmentsDAO.getAllAppointments());
        apptTable.refresh();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableHelper.initAllApptTables(appointmentID,title,description,location,contact,type,start,end,customerID,userID);


        allMonths.add("JANUARY");
        allMonths.add("FEBRUARY");
        allMonths.add("MARCH");
        allMonths.add("APRIL");
        allMonths.add("MAY");
        allMonths.add("JUNE");
        allMonths.add("JULY");
        allMonths.add("AUGUST");
        allMonths.add("SEPTEMBER");
        allMonths.add("OCTOBER");
        allMonths.add("NOVEMBER");
        allMonths.add("DECEMBER");

        monthsCombo.setItems(allMonths);

        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();



        apptTable.setItems(appointmentsDAO.getByMonth(LocalDateTime.now().getMonth().toString()));

        handleRadioAll();
    }
}
