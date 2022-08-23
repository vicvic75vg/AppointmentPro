package com.apptpro.apptpro.Controllers;

import com.apptpro.apptpro.DAO.UserManager;
import com.apptpro.apptpro.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {


    @FXML
    private Button signupButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private void onSignupAction() {
        if (!this.validateFormFields()) {
            return;
        }
        UserManager userManager = new UserManager();
        boolean isSignupSuccess = userManager.signupUser(firstNameTextField.getText(),lastNameTextField.getText(),usernameTextField.getText(), passwordTextField.getText());
        if (isSignupSuccess) {
            Alert successSignupAlert = new Alert(Alert.AlertType.INFORMATION);
            successSignupAlert.setTitle("Success");
            successSignupAlert.setContentText("Your account has been created!");
            successSignupAlert.showAndWait();
            onBackToLogin();
        } else {
            failureAlert();
        }

    }
    private boolean validateFormFields() {
        //Check first and last name not empty
        StringBuilder alertMessage = new StringBuilder();
        if (firstNameTextField.getText().trim().isEmpty() || lastNameTextField.getText().trim().isEmpty()) {
            alertMessage.append("First or last name text fields are empty!\n");
        }
        if (usernameTextField.getText().trim().isEmpty()) {
            alertMessage.append("Username text field is empty!\n");
        }
        if (passwordTextField.getText().trim().isEmpty() || confirmPasswordTextField.getText().trim().isEmpty()) {
            alertMessage.append("Password field(s) is empty!\n");
        }
        if (!passwordTextField.getText().trim().equals(confirmPasswordTextField.getText().trim())) {
           alertMessage.append("Password does not match!\n");
        }
        if (passwordTextField.getText().trim().length() < 8) {
            alertMessage.append("Password must have more than 8 characters");
        }

        if (alertMessage.length() > 0) {
            Alert errorsAlert = new Alert(Alert.AlertType.ERROR);
            errorsAlert.setTitle("Errors signing up");
            errorsAlert.setContentText(alertMessage.toString());
            errorsAlert.showAndWait();
            return false;
        }
        return true;
    }
    @FXML
    private void onBackToLogin() {
        try {
            Main.changeSceneToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            failureAlert();
        }
    }

    private void failureAlert() {
        Alert failureSignupAlert = new Alert(Alert.AlertType.ERROR);
        failureSignupAlert.setTitle("An error occurred!");
        failureSignupAlert.setContentText("An error occurred while attempting to sign up.\n " +
                "This could be because a text field is empty or an SQL error occurred.");
        failureSignupAlert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
