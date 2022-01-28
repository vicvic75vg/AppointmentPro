package com.apptpro.apptpro;

import com.apptpro.apptpro.Controllers.*;
import com.apptpro.apptpro.DAO.JDBC;
import com.apptpro.apptpro.Models.Appointment;
import com.apptpro.apptpro.Models.Customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    /**
     * The main JavaFX stage to use
     */
    private static Stage mainStage;

    /**
     * Opens the stage in JavaFX
     * @param stage The stage to use.
     * @throws IOException Failure on fxmlloader.load() throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        LoginController controller = new LoginController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setTitle("ApptPro");
        mainStage.setScene(scene);
        mainStage.show();
    }


    /**
     * Changes to current scene to the UpdateCustomer fxml view
     * @param customer Customer to update
     * @throws IOException Throws IOException failure to load file
     */
    public static void changeSceneToUpdateCustomer(Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("UpdateCustomer.fxml"));
        UpdateCustomerController controller = new UpdateCustomerController(customer);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    /**
     * Changes the scene to add customer view
     * @throws IOException Throws IOException on failure to load view
     */
    public static void changeSceneToAddCustomer() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("AddCustomer.fxml"));
        AddCustomerController controller = new AddCustomerController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    /**
     * Changes the scene to ApoointmentsPage view
      * @throws IOException Throws exception on failure to load file
     */
    public static void changeSceneToAppointments() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("AppointmentsPage.fxml"));
        AppointmentController controller = new AppointmentController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

    }

    /**
     * Change scene to AddAppointment view
     * @throws IOException throws exception on failure to load view
     */
    public static void changeSceneToAddAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("AddAppointment.fxml"));
        AddAppointmentController controller = new AddAppointmentController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    /**
     * Changes the scene to Reports view
     * @throws IOException throws execution on failure to load view
     */
    public static void changeSceneToReports() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Reports.fxml"));
        ReportsController controller = new ReportsController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

    }

    /**
     * Changes scene to contact metrics view
     * @throws IOException throws exception on failure ot load view
     */
    public static void changeSceneToContactMetrics() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ContactMetrics.fxml"));
        ContactMetricsController controller = new ContactMetricsController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    /**
     * Changes the scene to UpdateAppointment view
     * @param appointment The appointment object to update
     * @throws IOException Throws excepction on failure to load view
     */
        public static void changeSceneToUpdateAppointments(Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("UpdateAppointment.fxml"));
        UpdateAppointmentController controller = new UpdateAppointmentController(appointment);
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);

    }

    /**
     * Changes scene to MainScreen
     * @throws IOException Throws failure to load view
     */
    public static void changeSceneToMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("MainScreen.fxml"));
        MainScreenController controller = new MainScreenController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    /**
     * The main method entry point
     * @param args string arguments
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}