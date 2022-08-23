package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.AppointmentsDAO;
import com.apptpro.apptpro.DAO.CustomersDAO;
import com.apptpro.apptpro.DAO.JDBC;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Customer;
import com.apptpro.apptpro.Models.Tables;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Date;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer,Integer> customerID;
    @FXML
    private TableColumn<Customer,String> customerName;
    @FXML
    private TableColumn<Customer,String> customerAddress;
    @FXML
    private TableColumn<Customer,String>  customerPostalCode;
    @FXML
    private TableColumn<Customer,String> customerPhone;
    @FXML
    private TableColumn<Customer, Integer> divisionID;

    @FXML
    private ImageView addCustomerImageView;

    @FXML
    private ImageView updateCustomerImageView;

    @FXML
    private ImageView deleteCustomerImageView;

    @FXML
    private ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    @FXML
    private TextField customerSearchTextField;

    /**
     * Removes the selected customer from the database.
     */
    @FXML
    private void deleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null) {
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure you want to delete this customer?");
        confirmation.showAndWait();
        if(confirmation.getResult().getText().equals("Cancel")) {
            return;
        }
        CustomersDAO customersDAO = new CustomersDAO();
        boolean isDeleted = customersDAO.delete(selectedCustomer);
        if(isDeleted) {
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setHeaderText("User Deleted");
            alertSuccess.setTitle("Deleted");
            alertSuccess.setContentText(String.format("User \"%s\" successfully deleted.",selectedCustomer.getCustomerName()));
            alertSuccess.showAndWait();
            customerTable.setItems(customersDAO.getAllCustomers());
            customerTable.refresh();
            return;
        }
        Alert alertFailure = new Alert(Alert.AlertType.ERROR);
        alertFailure.setContentText("The customer has current appointments open!\n Remove customer from appointments to delete customer.");
        alertFailure.setHeaderText("Error");
        alertFailure.showAndWait();
    }

    /**
     * Calls the ChangeSceneToUpdateCustomer method and passes in
     * the selected customer to be updated
     */
    @FXML
    private void updateCustomer() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if(customer == null) {
            return;
        }
        try {
            Main.changeSceneToUpdateCustomer(customer);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes the current stage to the AddCustomer.fxml view
     */
    @FXML
    private void addCustomer() {
        try {
            Main.changeSceneToAddCustomer();
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
     * Calls the changeSceneToAppointments() method, which changes the view
     */
    @FXML
    private void viewAppointments() {
        try {
            Main.changeSceneToAppointments();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Calls the changeSceneToReports() method, which changes the view
     */
    @FXML
    private void viewReports() {
        try {
            Main.changeSceneToReports();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        CustomersDAO customersDAO = new CustomersDAO();


        //Search by customer name logic
        customerObservableList = customersDAO.getAllCustomers();

        FilteredList<Customer> filteredList = new FilteredList<>(customerObservableList, p -> true);

        customerSearchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredList.setPredicate(customer -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    }
                    return false; // Does not match.
                });
            }
        });
        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTable.comparatorProperty());
        customerTable.setItems(sortedList);




        Tooltip addCustomerImageViewTooltip = new Tooltip("Add Customer");
        Tooltip deleteCustomerImageViewTooltip = new Tooltip("Delete Customer");
        Tooltip updateCustomerImageViewTooltip = new Tooltip("Update Customer");

        Tooltip.install(addCustomerImageView,addCustomerImageViewTooltip);
        Tooltip.install(deleteCustomerImageView,deleteCustomerImageViewTooltip);
        Tooltip.install(updateCustomerImageView,updateCustomerImageViewTooltip);


        //Set disabled and opacity by default
        deleteCustomerImageView.setOpacity(0.50);
        updateCustomerImageView.setOpacity(0.50);

        customerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
                if (newValue != null) {
                    //As long as some value is selected in the table, we enable the buttons to update or delete customer
                    deleteCustomerImageView.setOpacity(1);
                    updateCustomerImageView.setOpacity(1);

                }
            }
        });
    }
}
