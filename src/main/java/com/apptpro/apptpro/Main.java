package com.apptpro.apptpro;

import com.apptpro.apptpro.Controllers.MainScreenController;
import com.apptpro.apptpro.DBO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    /**
     * Opens the stage in JavaFX
     * @param stage The stage to use.
     * @throws IOException Failure on fxmlloader.load() throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        MainScreenController controller = new MainScreenController();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ApptPro");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
    }
}