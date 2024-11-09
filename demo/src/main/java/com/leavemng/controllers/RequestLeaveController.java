package com.leavemng.controllers;

import com.leavemng.dao.LeaveRequestDAO;
import com.leavemng.dao.LeaveTypeDAO; // Import LeaveTypeDAO
import com.leavemng.models.LeaveRequest;
import com.leavemng.models.LeaveType; // Import LeaveType
import com.leavemng.utils.Navigation;
import com.leavemng.utils.SessionManager;
import java.time.LocalDate;
import java.util.List; // Import List
import javafx.collections.FXCollections; // Import FXCollections
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox; // Import ComboBox
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.leavemng.models.User; // Import User
import com.leavemng.dao.LeaveBalanceDAO; // Import LeaveBalanceDAO
import com.leavemng.dao.UserDAO; // Import UserDAO
public class RequestLeaveController {

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TextArea reasonTextArea;

  @FXML
  private Label errorLabel;

  @FXML
  private ComboBox<LeaveType> idTypeComboBox; // Change TextField to ComboBox


  @FXML
  public void handleDashboardButtonAction(ActionEvent event) {
    Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
  }

  @FXML
  public void initialize() {
    LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes(); // Get all leave types
    System.out.println("--------------------------");
    System.out.println(leaveTypes);
    idTypeComboBox.setItems(FXCollections.observableArrayList(leaveTypes)); // Set items in ComboBox
  }



  @FXML
  private void handleSubmitButtonAction(ActionEvent event) {
    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    String reason = reasonTextArea.getText();
    LeaveType selectedLeaveType = idTypeComboBox
      .getSelectionModel()
      .getSelectedItem(); // Get selected leave type

    if (
      startDate == null ||
      endDate == null ||
      reason.isEmpty() ||
      selectedLeaveType == null
    ) {
      errorLabel.setText("Please fill all fields.");
      return;
    }

    int idType = selectedLeaveType.getId(); // Get id from selected leave type

    LeaveRequest leaveRequest = new LeaveRequest();
    leaveRequest.setUid(SessionManager.getInstance().getCurrentUser().getId());
    leaveRequest.setStartDate(startDate);
    leaveRequest.setEndDate(endDate);
    leaveRequest.setReason(reason);
    leaveRequest.setId_type(idType); // Set id_type
    

    // diffrence between start date and end date
    final int days = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    final User currentUser = SessionManager.getInstance().getCurrentUser();

    if (days<0) {
        errorLabel.setText("End date should be greater than start date.");
        return;
    }
    if (days>currentUser.getAnnual_balance()) {
        errorLabel.setText("Insufficient leave balance ("+currentUser.getAnnual_balance()+" days) .");
        return;
    }
    
    // checkLeaveBalance
    LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
    try {
      leaveBalanceDAO.checkLeaveBalance(currentUser.getId(), idType, days);
      currentUser.setAnnual_balance(currentUser.getAnnual_balance()-days);

        UserDAO userDAO = new UserDAO();
        userDAO.updateUser(currentUser);
    } catch (Exception e) { 
    //   show error message
      errorLabel.setText(e.getMessage());
      return;
    }


    LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
    try {
      leaveRequestDAO.saveLeaveRequest(leaveRequest);
      errorLabel.setText("Leave request submitted successfully.");
      Navigation.navigateToPage("/com/leavemng/views/Dashboard.fxml", event);
    } catch (Exception e) {
      e.printStackTrace();
      errorLabel.setText("An error occurred. Please try again.");
    }
  }
}
