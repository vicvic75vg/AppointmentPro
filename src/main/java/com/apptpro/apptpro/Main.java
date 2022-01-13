package com.apptpro.apptpro;

import com.apptpro.apptpro.Controllers.LoginController;
import com.apptpro.apptpro.DAO.JDBC;
import com.apptpro.apptpro.Models.Customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
     * Allows the changing of the scene inside the mainStage.
     * @param controller The controller for the passed view
     * @param view The view to use
     * @param title The title of the scene
     * @throws IOException Throws an IOException when loading the scene.
     */
    public static <T> void changeScene(Class<?> controller, Customer params, String view, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
        Object c = null;
        if(params == null) {
            try {
                c = controller.getDeclaredConstructor().newInstance();
            } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
            fxmlLoader.setController(c);
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setTitle(title);
            mainStage.setScene(scene);
            mainStage.show();
            return;
        }
        try {
            c = controller.getConstructor(Customer.class).newInstance(params);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        fxmlLoader.setController(c);
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setTitle(title);
        mainStage.setScene(scene);
        mainStage.show();
    }


    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}