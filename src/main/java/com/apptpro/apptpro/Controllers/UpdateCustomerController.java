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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {

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
            Main.changeSceneToMainScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reflects the changes necessary to load appropriate
     * first-level divisions in the customerFirstLevelField, based on a chosen country
     * LAMBDA EXPRESSION USED TO FILTER FIRST LEVEL DIVISIONS BY SELECTED COUNTRY
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


    /**
     * Converts all user TextFields and combo box options and creates a new Customer
     * object. This object is then passed to the Customers Data Access Object to
     * update existing row in the database. If the update is successful, the application will
     * return to the main screen. If not successful, an error Alert message will be displayed, notifying the user.
     */
    @FXML
    private void saveUpdate() {
        Customer customer = new Customer(selectedCustomer.getCustomerID(),customerNameField.getText().trim(),
                customerAddressField.getText().trim(),customerPostalCodeField.getText().trim(),customerPhoneField.getText().trim(),
                selectedCustomer.getCreatedDate(), selectedCustomer.getCreatedBy(), selectedCustomer.getLastUpdate(),
                selectedCustomer.getLastUpdatedBy(), customerFirstLevelField.getSelectionModel().getSelectedItem(),
                customerCountryField.getSelectionModel().getSelectedItem());
        CustomersDAO customersDAO = new CustomersDAO();
        boolean isSuccess = customersDAO.update(customer);
        if(isSuccess) {
            toMainScreen();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Could not update user in the database!");
        alert.showAndWait();
    }


    /**
     * Initializes the customer text fields and country/first level ComboBoxes
     * @param url URL to use, unused in this override
     * @param resourceBundle The resource bundle to use, unused in this override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerIDField.setDisable(true);
        customerNameField.setText(selectedCustomer.getCustomerName());
        customerAddressField.setText(selectedCustomer.getCustomerAddress());
        customerPostalCodeField.setText(selectedCustomer.getCustomerPostalCode());
        customerPhoneField.setText(selectedCustomer.getCustomerPhone());

        CountriesDAO countriesDAO = new CountriesDAO();
        customerCountryField.setItems(countriesDAO.getAllCountries());
        customerCountryField.getSelectionModel().select(selectedCustomer.getCountry());

        FirstLevelDivisionsDAO firstLevelDivisionsDAO = new FirstLevelDivisionsDAO();
        Country country = customerCountryField.getSelectionModel().getSelectedItem();
        ObservableList<FirstLevelDivision> filteredFirstLevel = firstLevelDivisionsDAO.getAllFirstLevelDivisions().filtered(
                object -> object.getCountryID() == country.getCountryID()
        );

        customerFirstLevelField.setItems(filteredFirstLevel);
        customerFirstLevelField.getSelectionModel().select(selectedCustomer.getDivision());
    }
}
