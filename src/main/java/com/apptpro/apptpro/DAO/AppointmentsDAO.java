package com.apptpro.apptpro.DAO;

import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Contact;
import com.apptpro.apptpro.Models.DateTimeFormatting;
import com.apptpro.apptpro.Models.TopContacts;
import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashMap;

public class AppointmentsDAO {
    private Connection connection = JDBC.getConnection();

    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * Makes an SQL query to get all Appointments in the database
     * and adds to the allAppointments list
     * @return All appointments in an observable list
     */
    public ObservableList<Appointment> getAllAppointments() {
        try(Statement st = connection.createStatement()) {
           String query = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID;";
           ResultSet rs = st.executeQuery(query);
           while(rs.next()) {
               Contact contact = new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),
                       rs.getString("Email"));
               Appointment appointment = new Appointment(
                       rs.getInt("Appointment_ID"),
                       rs.getString("title"),
                       rs.getString("Description"),
                       contact,
                       rs.getString("Location"),
                       rs.getString("Type"),
                       rs.getString("Start"),
                       rs.getString("End"),
                       rs.getString("Create_Date"),
                       rs.getInt("Customer_ID"),
                       rs.getInt("User_ID")
               );
               allAppointments.add(appointment);
           }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * Makes an SQL query to the database for getting a count on all appointments for
     * each contact.
     * @return An Observable List of TopContact objects
     */
    public ObservableList<TopContacts> getTopContacts() {
        ObservableList<TopContacts> topContacts = FXCollections.observableArrayList();
        try(Statement st = connection.createStatement()) {
            String query = "SELECT Contact_Name,COUNT(Contact_Name) AS AppointmentCount FROM Appointments INNER JOIN contacts ON Appointments.Contact_ID = Contacts.Contact_ID GROUP BY Contact_Name ORDER BY AppointmentCount DESC;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                TopContacts currContact = new TopContacts(rs.getString("Contact_Name"),rs.getInt("AppointmentCount"));
                topContacts.add(currContact);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return topContacts ;
    }


    /**
     * Deletes an appointments of existing appointment ID
     * @param appointment The appointment to delete
     * @return True if successful, false if not
     */
    public boolean deleteAppointment(Appointment appointment) {
        try(Statement st = connection.createStatement()) {
            String query = String.format("DELETE FROM appointments WHERE appointments.Appointment_ID=%d",appointment.getAppointmentID());
            int rs = st.executeUpdate(query);
            return rs == 1;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing Appointment in the database
     * @param appointment The new Appointment object that will update an existing appointment with same ID
     * @return True if the update was successful, false if not
     * @throws SQLIntegrityConstraintViolationException throws if any integrity constraint is violated
     */
    public String update(Appointment appointment) throws SQLIntegrityConstraintViolationException {
        try (Statement st = connection.createStatement()) {
                String checkIfCustomerBooked = String.format("SELECT * FROM appointments INNER JOIN customers ON appointments.Customer_Id = customers.Customer_Id WHERE appointments.Customer_Id = %d AND appointments.Appointment_Id != %d;",
                        appointment.getCustomerID(),appointment.getAppointmentID());
                ResultSet rs = st.executeQuery(checkIfCustomerBooked);

                if(rs.isBeforeFirst()) {
                    while (rs.next()) {
                        //Getting UTC start and end dates straight from the database
                        String oldStart = rs.getString("Start").replaceAll("\\s+", "T");
                        String oldEnd = rs.getString("End").replaceAll("\\s+", "T");



                        LocalDateTime oldApptStart = LocalDateTime.parse(oldStart);
                        LocalDateTime oldApptEnd = LocalDateTime.parse(oldEnd);

                        String newStart = appointment.getStart(true);
                        String newEnd = appointment.getEnd(true);

                        LocalDateTime newApptStart = LocalDateTime.parse(newStart);
                        LocalDateTime newApptEnd = LocalDateTime.parse(newEnd);


                        if (oldApptStart.isBefore(newApptEnd) && oldApptEnd.isAfter(newApptStart) || oldApptStart.isEqual(newApptStart)) {
                            return "Booked";
                        }
                    }
                }
                Statement st2 = connection.createStatement();
                String checkIfCustomerExists = String.format("SELECT * FROM customers WHERE Customer_ID =%d", appointment.getCustomerID());
                ResultSet rs2 = st2.executeQuery(checkIfCustomerExists);

                    System.out.println("RS2 " + rs2.isBeforeFirst());
                if (rs2.isBeforeFirst()) {
                    Statement st3 = connection.createStatement();
                    String query = String.format("UPDATE appointments SET title='%s'," +
                                    "Description='%s', Location='%s', Type='%s', Start= '%s',End= '%s',Customer_ID=%s, " +
                                    "User_ID=%d,Contact_ID= %d  WHERE appointments.Appointment_ID=%d",
                            appointment.getTitle(),appointment.getDescription(),appointment.getLocation(),
                            appointment.getType(),appointment.getStart(false),appointment.getEnd(false),appointment.getCustomerID(),
                            appointment.getUserID(),appointment.getContact().getContactID(),appointment.getAppointmentID());
                    int result = st3.executeUpdate(query);
                    System.out.println("RESULT IS: " + result);
                    if (result == 1) {
                        return "True";
                    }
                } else if (!rs2.isBeforeFirst()) {
                    return "Does Not Exist";
                }
                return "False";
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return "False";
    }

    /**
     * Attempts to INSERT a new Appointments object into
     * the database.
     * @param appointment The appointment object to insert
     * @return True if the insertion was successfully done, false if not.
     */
    public String add(Appointment appointment) throws SQLException {
        try(Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE)) {
            String checkIfCustomerBooked = String.format("SELECT * FROM appointments INNER JOIN customers ON appointments.Customer_Id = customers.Customer_Id WHERE appointments.Customer_Id = %d;", appointment.getCustomerID());
            ResultSet rs = st.executeQuery(checkIfCustomerBooked);
            if(rs.isBeforeFirst()) {
                while(rs.next()) {
                    String oldStart = rs.getString("Start").replaceAll("\\s+","T");
                    String oldEnd = rs.getString("End").replaceAll("\\s+","T");

                    LocalDateTime oldApptStart = LocalDateTime.parse(oldStart);
                    LocalDateTime oldApptEnd = LocalDateTime.parse(oldEnd);

                    String newStart = appointment.getStart(true);
                    String newEnd = appointment.getEnd(true);

                    LocalDateTime newApptStart = LocalDateTime.parse(newStart);
                    LocalDateTime newApptEnd = LocalDateTime.parse(newEnd);



                    if(oldApptStart.isBefore(newApptEnd) && oldApptEnd.isAfter(newApptStart) || oldApptStart.isEqual(newApptStart)) {
                        return "Booked";
                    }
                }
            }



            Statement st2 = connection.createStatement();
            String checkIfCustomerExists = String.format("SELECT * FROM customers WHERE Customer_ID =%d", appointment.getCustomerID());
            ResultSet rs2 = st2.executeQuery(checkIfCustomerExists);

            if (!rs.isBeforeFirst() && rs2.isBeforeFirst()) {
                Statement st3 = connection.createStatement();
                String query = String.format("INSERT INTO appointments VALUES(%d, '%s', '%s', '%s', '%s', '%s', '%s', NOW(), 'apptpro', NOW(), 'apptpro', %d, %d, %d);", appointment.getAppointmentID(), appointment.getTitle(), appointment.getDescription(),
                        appointment.getLocation(), appointment.getType(), appointment.getStart(false), appointment.getEnd(false), appointment.getCustomerID(),
                        appointment.getUserID(), appointment.getContact().getContactID());
                int result = st3.executeUpdate(query);
                if (result == 1) {
                    return "True";
                }
            } else if (!rs2.isBeforeFirst()) {
                return "Does Not Exist";
            }
            return "False";
        }
    }


    /**
     * Gets all appointments of the selected month
     * @param month The month of queried appointments
     * @return A list of all the pertaining appointments
     */
    public ObservableList<Appointment> getByMonth(String month) {
        HashMap<String,Integer> months = new HashMap<>() ;

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

        ObservableList<Appointment> returnVal = FXCollections.observableArrayList();

        try(Statement st = connection.createStatement()) {
           String query = String.format("SELECT * FROM appointments INNER JOIN contacts ON" +
                   " appointments.Contact_Id = contacts.Contact_Id WHERE MONTH(appointments.Start)=%d;",months.get(month));
           ResultSet rs = st.executeQuery(query);
           while(rs.next()) {
               Contact contact = new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),
                       rs.getString("Email"));



               Appointment appointment = new Appointment(
                       rs.getInt("Appointment_ID"),
                       rs.getString("title"),
                       rs.getString("Description"),
                       contact,
                       rs.getString("Location"),
                       rs.getString("Type"),
                       rs.getString("Start"),
                       rs.getString("End"),
                       rs.getString("Create_Date"),
                       rs.getInt("Customer_ID"),
                       rs.getInt("User_ID") );
              returnVal.add(appointment);
           }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return returnVal;

    }

    /**
     * Gets all appointments in the next 8 days, all appointments in the following week
     * @return Appointments in the upcoming week
     */
    public ObservableList<Appointment> getByCurrentWeek() {
        ObservableList<Appointment> returnVal = FXCollections.observableArrayList();
        try(Statement st = connection.createStatement()) {
        String query = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE DATEDIFF(appointments.start,NOW()) < 8 AND DATEDIFF(appointments.start,NOW()) >= 0;";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            Contact contact = new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),
                    rs.getString("Email"));
            Appointment appointment = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getString("title"),
                    rs.getString("Description"),
                    contact,
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getString("Start"),
                    rs.getString("End"),
                    rs.getString("Create_Date"),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID") );
            returnVal.add(appointment);
        }
    } catch(SQLException ex) {
        ex.printStackTrace();
    }
        return returnVal;

    }

    public ObservableList<String> getAllTypes() {
        ObservableList<String> allTypes = FXCollections.observableArrayList();
        try(Statement st = connection.createStatement()) {
            String query = "SELECT DISTINCT(Type) FROM appointments;";
            ResultSet rs =  st.executeQuery(query);

            while(rs.next()) {
                allTypes.add(rs.getString("Type"));
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return allTypes;
    }
    public int getTotalApptsOfType(String type) {
        try(Statement st = connection.createStatement()) {
           String query = String.format("SELECT COUNT(Type) FROM appointments WHERE Type='%s';",type);
            ResultSet rs = st.executeQuery(query);
            rs.next();
           return rs.getInt("COUNT(Type)");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return -1;

    }
    public ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        try(Statement st = connection.createStatement()) {
            String query = "SELECT * FROM contacts;";
            ResultSet rs =  st.executeQuery(query);
            while(rs.next()) {
                Contact contact = new Contact(rs.getInt("Contact_Id"),rs.getString("Contact_Name"),
                        rs.getString("Email"));
                allContacts.add(contact);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return allContacts;
    }

    public ObservableList<Appointment> getContactSchedule(Contact contact) {
        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
        try(Statement st = connection.createStatement()) {
            String query = String.format("SELECT * FROM appointments WHERE Contact_Id = %d;",contact.getContactID());
            ResultSet rs =  st.executeQuery(query);

            while(rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("title"),
                        rs.getString("Description"),
                        contact,
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getString("Start"),
                        rs.getString("End"),
                        rs.getString("Create_Date"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID") );
                allAppts.add(appointment);
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return allAppts;

    }
    public int getTotalApptsOfMonth(int monthNum) {
        try(Statement st = connection.createStatement()) {
            String query = String.format("SELECT COUNT(*) FROM appointments WHERE MONTH(Appointments.start)=%d;", monthNum);
            ResultSet rs = st.executeQuery(query);
            if(!rs.next()) return -1;
            return rs.getInt("COUNT(*)");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Generates an appointment ID based on largest existing ID
     * @return A new Appointment ID
     */
    public int generateID() {
        try(Statement st = connection.createStatement()) {
            String query = "SELECT MAX(Appointment_Id) FROM appointments;";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                return rs.getInt("MAX(Appointment_Id)") + 1;
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
