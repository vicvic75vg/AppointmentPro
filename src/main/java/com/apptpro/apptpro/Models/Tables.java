package com.apptpro.apptpro.Models;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class Tables {

    /**
     * Initializes the Customer table.
     * @param customerID The customerID column
     * @param customerName Customer Name column
     * @param customerAddress Customer Address column
     * @param customerPostalCode Customer Postal Code column
     * @param customerPhone Customer Phone column
     */
    public static void initCustomerTable(TableColumn<Customer, Integer> customerID,
                                         TableColumn<Customer,String> customerName,
                                         TableColumn<Customer,String> customerAddress,
                                         TableColumn<Customer, String> customerPostalCode,
                                         TableColumn<Customer,String> customerPhone) {

        customerID.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("Customer_ID"));
    }
}
