package com.leavemng.controllers;

import com.leavemng.dao.UserDAO;
import com.leavemng.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.leavemng.utils.Navigation;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField departementField;
    @FXML
    private TextField birthDateField;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // Handle user registration
        String username = usernameField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String departement = departementField.getText();
        String birthDate = birthDateField.getText();

        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || departement.isEmpty() || birthDate.isEmpty() ) {
            errorLabel.setText("All fields are required.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        try {
            User existingUser = userDAO.getUser(username);
            if (existingUser != null) {
                errorLabel.setText("Username already exists.");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setDepartement(departement);
            user.setBirth_date(birthDate);
            userDAO.saveUser(user);

            errorLabel.setText("Registration successful!");
            // navigate to login.fxml
            Navigation.navigateToPage("/com/leavemng/views/login.fxml", event);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred. Please try again.");
        }
    }   
}