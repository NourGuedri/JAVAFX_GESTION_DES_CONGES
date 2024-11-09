package com.leavemng.dao;

import com.leavemng.models.User;
import com.leavemng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password, phone, departement, birth_date) VALUES ( ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getDepartement());
            stmt.setString(5, user.getBirth_date());
            stmt.executeUpdate();
        }
    }

    public User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setIs_admin(rs.getBoolean("is_admin"));
                user.setDepartement (rs.getString("departement"));
                user.setBirth_date(rs.getString("birth_date"));
                user.setAnnual_balance(rs.getInt("annual_balance"));
                
                return user;
            }
        }
        return null;
    }

    public User updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET username = ?, password = ?, phone = ?, departement = ?, birth_date = ?, annual_balance = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getDepartement());
            stmt.setString(5, user.getBirth_date());
            stmt.setInt(6, user.getAnnual_balance());
            stmt.setInt(7, user.getId());
            stmt.executeUpdate();
        }
        return user;
    }
}
