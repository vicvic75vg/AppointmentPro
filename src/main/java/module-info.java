module com.apptpro.apptpro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.apptpro.apptpro to javafx.fxml;
    exports com.apptpro.apptpro;
    exports com.apptpro.apptpro.Controllers;
    exports com.apptpro.apptpro.Models;
    opens com.apptpro.apptpro.Controllers to javafx.fxml;
}