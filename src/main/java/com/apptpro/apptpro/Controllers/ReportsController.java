package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    HashMap<String,Integer> months = new HashMap<>() ;
    ObservableList<String> monthsStrings = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> byTypeBox;
    @FXML
    private ComboBox<String> byMonthBox;
    @FXML
    private ComboBox<Contact> contactBox;
    @FXML
    private Text byTypeText;
    @FXML
    private Text byMonthText;
    @FXML
    private TableColumn<Appointment, Integer> apptID;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> desc;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> end;
    @FXML
    private TableColumn<Appointment, Integer> custID;
    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private Button viewContactMetrics;


    /**
     * Creates a new ReportsController object that maps each month to the
     * appropriate number
     * LAMBDA EXPRESSION USED TO ADD ALL MONTH STRINGS TO THE BYMONTHBOX FOR SELECTION
     */
    public ReportsController() {
        months.put("JANUARY",1);
        months.put("FEBRUARY",2);
        months.put("MARCH",3);
        months.put("APRIL",4);
        months.put("MAY",5);
        months.put("JUNE",6);
        months.put("JULY",7);
        months.put("AUGUST",8);
        months.put("SEPTEMBER",9);
        months.put("OCTOBER",10);
        months.put("NOVEMBER",11);
        months.put("DECEMBER",12);

        months.forEach((key,value) -> {
            monthsStrings.add(key);
        });


    }

    /**
     * Populates the appointments table with the selected Contact
     */
    @FXML
    private void contactHandler() {
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        Contact contact = contactBox.getSelectionModel().getSelectedItem();

        ObservableList<Appointment> appointments =  appointmentsDAO.getContactSchedule(contact);

         scheduleTable.setItems(appointments);
         scheduleTable.refresh();
    }

    /**
     * Calls the ChangeSceneToContactMetrics() method
     */
    @FXML
    private void viewContactMetrics() {
        try {
            Main.changeSceneToContactMetrics();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets the total number of appointments of the specified type
     */
    @FXML
    private void typeHandler() {
        String selectedType = byTypeBox.getSelectionModel().getSelectedItem();
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        int totalNumType = appointmentsDAO.getTotalApptsOfType(selectedType);
        byTypeText.setText(String.valueOf(totalNumType));
    }

    /**
     * Sets the total number of appointments by the specified month
     */
    @FXML
    private void monthHandler() {
        String selectedMonth = byMonthBox.getSelectionModel().getSelectedItem();

        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        int totalNumMonths = appointmentsDAO.getTotalApptsOfMonth(months.get(selectedMonth));
        byMonthText.setText(String.valueOf(totalNumMonths));
    }

    /**
     * Calls the ChangeSceneToMainScreen, which changes the view
     */
    @FXML
    private void viewCustomers() {
        try {
            Main.changeSceneToMainScreen();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        desc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        start.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));
        end.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));
        apptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

         byTypeBox.setItems(appointmentsDAO.getAllTypes());
         byMonthBox.setItems(monthsStrings);
         contactBox.setItems(appointmentsDAO.getAllContacts());

    }
}
