package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.TopContacts;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactMetricsController implements Initializable {


    @FXML
    private TableView<TopContacts> topContactsTable;
    @FXML
    private TableColumn<TopContacts, String> columnContactName;
    @FXML
    private TableColumn<TopContacts, String> columnAppointments;


    @FXML
    private void viewCustomers() {
        try {
            Main.changeSceneToMainScreen();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void viewReports() {
        try {
            Main.changeSceneToReports();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

        ObservableList<TopContacts> topContacts =  appointmentsDAO.getTopContacts();



        columnContactName.setCellValueFactory(new PropertyValueFactory<TopContacts,String>("contactName"));
        columnAppointments.setCellValueFactory(new PropertyValueFactory<TopContacts,String>("appointmentsCount"));

        topContactsTable.setItems(topContacts);
        topContactsTable.refresh();

    }

}
