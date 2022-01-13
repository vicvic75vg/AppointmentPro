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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {
    /**
     * The selected customer
     */
    private Customer selectedCustomer;

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
     * The constructor for the UpdateCustomerController
     * @param selectedCustomer The customer selected in the customersTable TableView to update
     */
    public UpdateCustomerController(Customer selectedCustomer) {
        this.selectedCustomer= selectedCustomer;
    }

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


    @FXML
    private void saveUpdate() {
        Customer customer = new Customer(selectedCustomer.getCustomerID(),customerNameField.getText(),
                customerAddressField.getText(),customerPostalCodeField.getText(),customerPhoneField.getText(),
                selectedCustomer.getCreatedDate(), selectedCustomer.getCreatedBy(), selectedCustomer.getLastUpdate(),
                selectedCustomer.getLastUpdatedBy(), customerFirstLevelField.getSelectionModel().getSelectedItem(),
                customerCountryField.getSelectionModel().getSelectedItem());
        CustomersDAO customersDAO = new CustomersDAO();
        boolean isSuccess = customersDAO.update(customer);
        if(isSuccess) {
            toMainScreen();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerNameField.setText(selectedCustomer.getCustomerName());
        customerAddressField.setText(selectedCustomer.getCustomerAddress());
        customerPostalCodeField.setText(selectedCustomer.getCustomerPostalCode());
        customerPhoneField.setText(selectedCustomer.getCustomerPhone());

        CountriesDAO countriesDAO = new CountriesDAO();
        customerCountryField.setItems(countriesDAO.getAllCountries());
        customerCountryField.getSelectionModel().select(selectedCustomer.getCountry());


        FirstLevelDivisionsDAO firstLevelDivisionsDAO = new FirstLevelDivisionsDAO();
        customerFirstLevelField.setItems(firstLevelDivisionsDAO.getAllFirstLevelDivisions());
        customerFirstLevelField.getSelectionModel().select(selectedCustomer.getDivision());
    }
}
