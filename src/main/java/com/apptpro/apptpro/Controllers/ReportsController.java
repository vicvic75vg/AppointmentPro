package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
    private Text byTypeText;
    @FXML
    private Text byMonthText;


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

    @FXML
    private void typeHandler() {
        String selectedType = byTypeBox.getSelectionModel().getSelectedItem();
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        int totalNumType = appointmentsDAO.getTotalApptsOfType(selectedType);
        byTypeText.setText(String.valueOf(totalNumType));
    }
    @FXML
    private void monthHandler() {
        String selectedMonth = byMonthBox.getSelectionModel().getSelectedItem();

        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
        int totalNumMonths = appointmentsDAO.getTotalApptsOfMonth(months.get(selectedMonth));
        byMonthText.setText(String.valueOf(totalNumMonths));
    }
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



        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

         byTypeBox.setItems(appointmentsDAO.getAllTypes());




         byMonthBox.setItems(monthsStrings);

    }
}
