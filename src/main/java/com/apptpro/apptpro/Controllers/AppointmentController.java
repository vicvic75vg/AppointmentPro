package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import com.apptpro.apptpro.Models.DateTimeFormatting;
import com.apptpro.apptpro.Models.Tables;
import com.apptpro.apptpro.TableHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

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

    @FXML
    private ImageView addAppointmentImageView;

    @FXML
    private ImageView deleteAppointmentImageView;

    @FXML
    private ImageView updateAppointmentImageView;

    @FXML
    private TextField appointmentSearchTextField;

    private ObservableList<String> allMonths = FXCollections.observableArrayList();

    private ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();

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
     * Logs the user out of the application. The user must verify credentials to view data
     */
    @FXML
    private void logoutAction() {
        try {
            Main.changeSceneToLogin();
        } catch (Exception e) {
            e.printStackTrace();
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



        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        appointmentObservableList = appointmentsDAO.getAllAppointments();

        FilteredList<Appointment> filteredList = new FilteredList<>(appointmentObservableList,p -> true);

        appointmentSearchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredList.setPredicate(appointment -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (appointment.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    }
                    return false; // Does not match.
                });
            }
        });
        SortedList<Appointment> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(apptTable.comparatorProperty());
        apptTable.setItems(sortedList);


        Tooltip addAppointmentTooltip = new Tooltip("Add Appointment");
        Tooltip deleteAppointmentTooltip = new Tooltip("Delete Appointment");
        Tooltip updateAppointmentTooltip = new Tooltip("Update Appointment");


        Tooltip.install(addAppointmentImageView,addAppointmentTooltip);
        Tooltip.install(deleteAppointmentImageView,deleteAppointmentTooltip);
        Tooltip.install(updateAppointmentImageView,updateAppointmentTooltip);

        //Disable/enable of update and delete appointment imageview


        deleteAppointmentImageView.setOpacity(0.5);
        updateAppointmentImageView.setOpacity(0.5);
        apptTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appointment>() {
            @Override
            public void changed(ObservableValue<? extends Appointment> observable, Appointment oldValue, Appointment newValue) {
                if (newValue != null) {
                    deleteAppointmentImageView.setOpacity(1);
                    updateAppointmentImageView.setOpacity(1);
                }
            }
        });

    }
}
