package com.leavemng.dao;

import com.leavemng.models.LeaveRequest;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {
    public void saveLeaveRequest(LeaveRequest leaveRequest) throws SQLException {
        String sql = "INSERT INTO leaverequest (uid, startdate, enddate, reason, id_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaveRequest.getUid());
            stmt.setDate(2, java.sql.Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(leaveRequest.getEndDate()));
            stmt.setString(4, leaveRequest.getReason());
            stmt.setInt(5, leaveRequest.getId_type()); // Add id_type
            stmt.executeUpdate();
        }
    }

    public List<LeaveRequest> getLeaveRequestsByUid(int uid) throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leaverequest WHERE uid = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setUid(rs.getInt("uid"));
                leaveRequest.setStartDate(rs.getDate("startdate").toLocalDate());
                leaveRequest.setEndDate(rs.getDate("enddate").toLocalDate());
                leaveRequest.setReason(rs.getString("reason"));
                leaveRequest.setId_type(rs.getInt("id_type")); // Retrieve id_type
                leaveRequest.setStatus(rs.getString("status"));
                leaveRequests.add(leaveRequest);
            }
        }
        return leaveRequests;
    }
    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) throws SQLException {
        String sql = "UPDATE leaverequest SET startdate = ?, enddate = ?, reason = ?, id_type = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(leaveRequest.getStartDate()));
            stmt.setDate(2, java.sql.Date.valueOf(leaveRequest.getEndDate()));
            stmt.setString(3, leaveRequest.getReason());
            stmt.setInt(4, leaveRequest.getId_type()); // Add id_type
            stmt.setInt(5, leaveRequest.getId());
            stmt.executeUpdate();
        }
        return leaveRequest;
    }       

    public void approveLeaveRequest(int id) throws SQLException {
        String sql = "UPDATE leaverequest SET status = 'approved' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    public void rejectLeaveRequest(int id) throws SQLException {
        String sql = "UPDATE leaverequest SET status = 'rejected' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public List<LeaveRequest> getPendingLeaveRequests() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String sql = "SELECT * FROM leaverequest WHERE status = 'pending'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(rs.getInt("id"));
                leaveRequest.setUid(rs.getInt("uid"));
                leaveRequest.setStartDate(rs.getDate("startdate").toLocalDate());
                leaveRequest.setEndDate(rs.getDate("enddate").toLocalDate());
                leaveRequest.setReason(rs.getString("reason"));
                leaveRequest.setId_type(rs.getInt("id_type")); // Retrieve id_type
                leaveRequest.setStatus(rs.getString("status"));
                leaveRequests.add(leaveRequest);
            }
        }
        return leaveRequests;
    }
}