package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.CustomersDAO;
import com.apptpro.apptpro.DAO.JDBC;
import com.apptpro.apptpro.Main;
import com.apptpro.apptpro.Models.Customer;
import com.apptpro.apptpro.Models.Tables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
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
    private TableColumn<Customer, String> createdDate;
    @FXML
    private TableColumn<Customer, String> createdBy;
    @FXML
    private TableColumn<Customer, String> lastUpdate;
    @FXML
    private TableColumn<Customer, String> lastUpdatedBy;
    @FXML
    private TableColumn<Customer, Integer> divisionID;




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
        }
    }
    @FXML
    private void updateCustomer() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if(customer == null) {
            return;
        }
        try {
            Main.changeScene(UpdateCustomerController.class,customer, "UpdateCustomer.fxml", "Update Customer");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void addCustomer() {
        try {
            Main.changeScene(AddCustomerController.class,null,"AddCustomer.fxml","Add Customer");
        } catch(IOException ex) {
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
        createdDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        CustomersDAO customersDAO = new CustomersDAO();

        customerTable.setItems(customersDAO.getAllCustomers());
        customerTable.refresh();
    }
}
