package com.leavemng.controllers;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;
import com.leavemng.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();  
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.getUser(username);
            if (user != null && user.getPassword().equals(password)) {
                // Store the logged-in user in the session manager
                SessionManager.getInstance().setCurrentUser(user);

                // Navigate to Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/leavemng/views/Dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred. Please try again.");
        }
    }

}
