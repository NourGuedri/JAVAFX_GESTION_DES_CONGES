package com.leavemng.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.leavemng.models.User;
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;


public class ProfileController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label departementLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label annualBalanceLabel;

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        usernameLabel.setText(user.getUsername());
        phoneLabel.setText(user.getPhone());
        departementLabel.setText(user.getDepartement());
        birthDateLabel.setText(user.getBirth_date());
        annualBalanceLabel.setText(String.valueOf(user.getAnnual_balance()));
    }
    @FXML
    private void handleGoBackButtonAction(ActionEvent event) {
      Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
    }

}