/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.User;
import com.nbl.utils.Utils;
import com.nbl.utils.JdbcUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class UserServices {
    public User signIn(User user) throws SQLException, IOException{
        try (Connection conn = JdbcUtils.getConn()) {
                PreparedStatement stm = conn.prepareStatement("SELECT * FROM `user` "
                        + "WHERE `username` = ? and `password` = ? and `role_id` = ? ");

                stm.setString(1, user.getUsername());
                stm.setString(2, Utils.getMd5(user.getPassword()));
                stm.setInt(3, user.getRoleId());
                ResultSet rs = stm.executeQuery();

                User newUser = null;
                while (rs.next()) {
                    newUser = new User();
                     
                    newUser.setId(rs.getInt("id"));
                    newUser.setUsername(rs.getString("username"));
                    newUser.setName(rs.getString("name"));
                    newUser.setGender(rs.getString("gender"));
                    newUser.setDateOfBirth(rs.getDate("date_of_birth"));
                    newUser.setRoleId(rs.getInt("role_id"));
                    newUser.setDepartmentId(rs.getInt("department_id"));
                    newUser.setRegistrationDate(rs.getDate("registration_date"));
                    newUser.setExpirationDate(rs.getDate("expiration_date"));
                    newUser.setEmail(rs.getString("email"));
                    newUser.setAddress(rs.getString("address"));
                    newUser.setPhone(rs.getString("phone"));
                    break;
                }
                
                return newUser;
        }
    }
    
    public boolean signUp(User user) throws SQLException {
        if(!checkUserExistence(user.getUsername())) {
            try (Connection conn = JdbcUtils.getConn()) {
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `user` "
                        + "(username, name, email, date_of_birth, gender, role_id, department_id, registration_date, expiration_date, address, phone, password)"
                          + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setDate(4, user.getDateOfBirth());
                preparedStatement.setString(5, user.getGender());
                preparedStatement.setInt(6, user.getRoleId());
                preparedStatement.setInt(7, user.getDepartmentId());
                preparedStatement.setDate(8, user.getRegistrationDate());
                preparedStatement.setDate(9, user.getExpirationDate());
                preparedStatement.setString(10, user.getAddress());
                preparedStatement.setString(11, user.getPhone());
                preparedStatement.setString(12, Utils.getMd5(user.getPassword()));

                preparedStatement.executeUpdate();

                return true;
            }
        }
        return false;
    }
    
    public static boolean checkUserExistence(String username) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT `id` FROM `user` WHERE `username` = ?");
            
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            
            if (rs.isBeforeFirst()) {
                return true;
            }

            return false;
        }
    }
    
    public static boolean checkUserExistenceById(Integer userId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT `id` FROM `user` WHERE `id` = ?");
            
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.isBeforeFirst()) {
                return true;
            }

            return false;
        }
    }
    
    public List<User> getUserList(String kw) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `user` "
                    + "WHERE (username like concat('%',?,'%')) OR (name like concat('%',?,'%'))");
            if (kw == null)
                kw = "";
            
            stm.setString(1, kw);
            stm.setString(2, kw);
            ResultSet rs = stm.executeQuery();
            
            List<User> users = new ArrayList<>();
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password") ;
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                Date dateOfBirth = rs.getDate("date_of_birth");
                Integer roleId = rs.getInt("role_id");
                Integer departmentId = rs.getInt("department_id") ;
                Date registrationDate = rs.getDate("registration_date");
                Date expirationDate = rs.getDate("expiration_date");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                users.add(new User(id, username, password, name, gender, dateOfBirth, roleId, departmentId, registrationDate, expirationDate, email, address, phone));
            }
            
            return users;
        }
    }
    
    public boolean deleteUser(Integer userId) throws SQLException {
        if(userId != null && checkUserExistenceById(userId) == true) {
            try (Connection conn = JdbcUtils.getConn()) {
                conn.setAutoCommit(false);
                
                PreparedStatement stm = conn.prepareStatement("DELETE FROM `user` WHERE id = ?");

                stm.setInt(1, userId);
                
                stm.executeUpdate();
                conn.commit();

                return true;
            }
        }
        
        return false;
    }
    
    public boolean addUser(User user) throws SQLException {
        if(user.getUsername() == null) {
            return false;
        }
        
        if(user.getPassword() == null) {
            return false;
        }
        
        if(user.getRoleId() == null) {
            return false;
        }
        
        if(user.getDepartmentId() == null) {
            return false;
        }
        
        else {
            try (Connection conn = JdbcUtils.getConn()) {
                conn.setAutoCommit(false);

                PreparedStatement stm = conn.prepareStatement("INSERT INTO `user`"
                        + "(username, name, email, date_of_birth, gender, role_id, department_id, registration_date, expiration_date, address, phone, password)"
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                stm.setString(1, user.getUsername());
                stm.setString(2, user.getName());
                stm.setString(3, user.getEmail());
                stm.setDate(4, user.getDateOfBirth());
                stm.setString(5, user.getGender());
                stm.setInt(6, user.getRoleId());
                stm.setInt(7, user.getDepartmentId());
                stm.setDate(8, user.getRegistrationDate());
                stm.setDate(9, user.getExpirationDate());
                stm.setString(10, user.getAddress());
                stm.setString(11, user.getPhone());
                stm.setString(12, Utils.getMd5(user.getPassword()));

                stm.executeUpdate();
                conn.commit();

                return true;
            }
        }
    }
    
    public boolean editUser(User user) throws SQLException {
        if(user.getUsername() == null) {
            return false;
        }
        
        if(user.getPassword() == null) {
            return false;
        }
        
        if(user.getRoleId() == null) {
            return false;
        }
        
        if(user.getDepartmentId() == null) {
            return false;
        }
        
        else {
            try (Connection conn = JdbcUtils.getConn()) {
                conn.setAutoCommit(false);
                
                PreparedStatement stm = conn.prepareStatement("UPDATE `user` "
                        + "SET `username` = ?, "
                        + "`name` = ?, "
                        + "`email` = ?, "
                        + "`date_of_birth` = ?, "
                        + "`gender` = ?, "
                        + "`role_id` = ?, "
                        + "`department_id` = ?, "
                        + "`registration_date` = ?, "
                        + "`expiration_date` = ?, "
                        + "`address` = ?, "
                        + "`phone` = ?, "
                        + "`password` = ? "
                        + "WHERE `id` = ?");

                stm.setString(1, user.getUsername());
                stm.setString(2, user.getName());
                stm.setString(3, user.getEmail());
                stm.setDate(4, user.getDateOfBirth());
                stm.setString(5, user.getGender());
                stm.setInt(6, user.getRoleId());
                stm.setInt(7, user.getDepartmentId());
                stm.setDate(8, user.getRegistrationDate());
                stm.setDate(9, user.getExpirationDate());
                stm.setString(10, user.getAddress());
                stm.setString(11, user.getPhone());
                stm.setString(12, Utils.getMd5(user.getPassword()));
                stm.setInt(13, user.getId());
                
                stm.executeUpdate();
                conn.commit();

                return true;
             }
        }
    }
}
