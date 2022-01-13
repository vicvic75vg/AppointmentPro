package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.CountriesDAO;
import com.apptpro.apptpro.DAO.CustomersDAO;
import com.apptpro.apptpro.DAO.FirstLevelDivisionsDAO;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Country;
import com.apptpro.apptpro.Models.Customer;
import com.apptpro.apptpro.Models.FirstLevelDivision;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {


    @FXML
    private TextField customerIDField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerPostalCodeField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private ComboBox<Country> customerCountryField;
    @FXML
    private ComboBox<FirstLevelDivision> customerFirstLevelField;
    @FXML
    private Button cancelButton;

    /**
     * Cancels the updating of the customer and returns to the MainScreen
     */
    @FXML
    private void toMainScreen() {
        try {
            Main.changeScene(MainScreenController.class,null,"MainScreen.fxml","Main");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void addCustomer() {
        CustomersDAO customersDAO = new CustomersDAO();
        Customer customer = new Customer(customersDAO.generateID(),customerNameField.getText().trim(),
                customerAddressField.getText().trim(),customerPostalCodeField.getText().trim(),customerPhoneField.getText().trim(),
                null,null,null,null,customerFirstLevelField.getSelectionModel().getSelectedItem(),
                customerCountryField.getSelectionModel().getSelectedItem());
        boolean isSuccess = customersDAO.add(customer);
        if(isSuccess) {
            toMainScreen();
        }
    }




    /**
     * Reflects the changes necessary to load appropriate
     * first-level divisions in the customerFirstLevelField
     */
    @FXML
    private void propagateFirstLevelField() {
        FirstLevelDivisionsDAO firstLevelDivisionsDAO = new FirstLevelDivisionsDAO();
        Country country = customerCountryField.getSelectionModel().getSelectedItem();
        ObservableList<FirstLevelDivision> filteredFirstLevel = firstLevelDivisionsDAO.getAllFirstLevelDivisions().filtered(
                object -> object.getCountryID() == country.getCountryID()
        );
        customerFirstLevelField.setItems(filteredFirstLevel);
        customerFirstLevelField.getSelectionModel().select(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CountriesDAO countriesDAO = new CountriesDAO();
        customerCountryField.setItems(countriesDAO.getAllCountries());
        customerCountryField.getSelectionModel().select(0);


        propagateFirstLevelField();
    }
}
