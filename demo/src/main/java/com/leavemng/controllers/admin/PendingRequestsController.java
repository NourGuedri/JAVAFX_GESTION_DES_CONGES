package com.leavemng.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.Navigation;
import com.leavemng.dao.LeaveRequestDAO;

import java.sql.SQLException;
import java.util.List;

public class PendingRequestsController {
    @FXML
    private TableView<LeaveRequest> requestsTable;
    @FXML
    private TableColumn<LeaveRequest, Integer> idColumn;
    @FXML
    private TableColumn<LeaveRequest, Integer> uidColumn;
    @FXML
    private TableColumn<LeaveRequest, String> startDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> endDateColumn;
    @FXML
    private TableColumn<LeaveRequest, String> reasonColumn;
    @FXML
    private TableColumn<LeaveRequest, String> statusColumn;


    @FXML
    public void handleApproveButtonAction() {
        LeaveRequest leaveRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (leaveRequest != null) {
            LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
            try {
                leaveRequestDAO.approveLeaveRequest(leaveRequest.getId());
                ObservableList<LeaveRequest> observableList = requestsTable.getItems();
                observableList.remove(leaveRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleRejectButtonAction() {
        LeaveRequest leaveRequest = requestsTable.getSelectionModel().getSelectedItem();
        if (leaveRequest != null) {
            LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
            try {
                leaveRequestDAO.rejectLeaveRequest(leaveRequest.getId());
                ObservableList<LeaveRequest> observableList = requestsTable.getItems();
                observableList.remove(leaveRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        Navigation.navigateToPage("/com/leavemng/views/admin/Dashboard.fxml", event);
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        uidColumn.setCellValueFactory(new PropertyValueFactory<>("uid"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        LeaveRequestDAO leaveRequestDAO = new LeaveRequestDAO();
        try {
            List<LeaveRequest> leaveRequests = leaveRequestDAO.getPendingLeaveRequests();
            ObservableList<LeaveRequest> observableList = FXCollections.observableArrayList(leaveRequests);
            requestsTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}