package com.apptpro.apptpro;

import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableHelper {
    public static void initAllApptTables(TableColumn<Appointment,Integer> allApptID,
                                         TableColumn<Appointment,String> allApptTitle,
                                         TableColumn<Appointment,String> allApptDesc,
                                         TableColumn<Appointment,String> allApptLoc,
                                         TableColumn<Appointment, Contact> allApptContact,
                                         TableColumn<Appointment,String> allApptType,
                                         TableColumn<Appointment,String> allApptStartDT,
                                         TableColumn<Appointment,String> allApptEndDT,
                                         TableColumn<Appointment,Integer> allApptCustID,
                                         TableColumn<Appointment,Integer> allApptUserID) {
        allApptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        allApptContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        allApptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        allApptCustID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        allApptDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        allApptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        allApptLoc.setCellValueFactory(new PropertyValueFactory<>("location"));
        allApptStartDT.setCellValueFactory(new PropertyValueFactory<>("start"));
        allApptEndDT.setCellValueFactory(new PropertyValueFactory<>("end"));
        allApptUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
