package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Country;
import com.apptpro.apptpro.Models.Customer;
import com.apptpro.apptpro.Models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DAO of the customers' table in the database
 */
public class CustomersDAO implements DAO<Customer>{

    /**
     * List of all customers in an Observable Array List.
     */
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    /**
     * The active JDBC Connection
     */
    private Connection connection = JDBC.getConnection();

    /**
     * Creates a new CustomersDAO
     */



    /**
     * Updates a list of all the customers from the database and returns it.
     * @return The list of all customers
     */
    public ObservableList<Customer> getAllCustomers() {
        try (Statement st =  connection.createStatement()) {
            String query = "SELECT * FROM customers INNER JOIN first_level_divisions ON customers.Division_ID =" +
                    " first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = \n" +
                    "countries.Country_ID;";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getTimestamp("Create_Date"));
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(rs.getInt("Division_ID"),rs.getString("Division"),rs.getInt("Country_ID"));
                Country country = new Country(rs.getInt("Country_ID"),rs.getString("Country"));
                Customer newCustomer = new Customer(rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getString("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getString("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        firstLevelDivision,
                        country);
                allCustomers.add(newCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       return allCustomers;
    }

    public int generateID() {
        try(Statement st = connection.createStatement()) {
            String query = "SELECT MAX(Customer_ID) FROM customers;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                return rs.getInt("MAX(Customer_ID)") + 1;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    public boolean add(Customer customer) {
       try (Statement st = connection.createStatement())  {
          String query = String.format("INSERT INTO customers VALUES(%d , '%s' , '%s' , '%s' , '%s' , " +
                  "NOW(), 'apptpro', NOW(), 'apptpro', %d);", customer.getCustomerID(),customer.getCustomerName(),
                  customer.getCustomerAddress(),customer.getCustomerPostalCode(),customer.getCustomerPhone(),
                  customer.getDivision().getDivisionID());
          int result = st.executeUpdate(query);
           System.out.println(result);
          if(result == 1) {
              return true;
          }
       } catch(SQLException ex) {
           ex.printStackTrace();
       }
       return false;
    }

    @Override
    public void save(Customer customer) {
        allCustomers.add(customer);
    }

    @Override
    public boolean update(Customer customer) {
        try(Statement st = connection.createStatement()) {
           String query = String.format("UPDATE customers SET Customer_Name='%s', " +
                   "Address='%s',Postal_Code='%s', Phone='%s', Division_ID= " + customer.getDivision().getDivisionID(),
                   customer.getCustomerName(),customer.getCustomerAddress(),customer.getCustomerPostalCode(),
                   customer.getCustomerPhone());
           query = query.concat(" WHERE Customer_ID = " + customer.getCustomerID());
           var result = st.executeUpdate(query);
           if(result == 1) {
               return true;
           }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the customer selected from the TableView
     * @param selectedCustomer
     * @return
     */
    @Override
    public boolean delete(Customer selectedCustomer) {
        try(Statement st = connection.createStatement()) {
            String checkConstraints = String.format("SELECT * FROM customers INNER JOIN appointments ON customers.Customer_ID = appointments.Customer_ID WHERE appointments.Customer_ID = %d ;",selectedCustomer.getCustomerID());
            ResultSet rs = st.executeQuery(checkConstraints);

            if(rs.next()) {
                return false;
            }
            String query = String.format("DELETE FROM customers WHERE customers.Customer_ID= %d", selectedCustomer.getCustomerID());
            int result = st.executeUpdate(query);
            return result == 1;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
