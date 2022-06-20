/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.utils.JdbcUtils;
import com.nbl.pojo.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class RoleServices {
    public List<Role> getRoleList() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `role` ");
            
            ResultSet rs = stm.executeQuery();
            List<Role> roles = new ArrayList<>();
            
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                roles.add(new Role(id, name));
            }
            
            return roles;
        }
    }
    
    public static Role getRoleById(Integer roleId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `role` WHERE `id` = ?");
            stm.setInt(1, roleId);
            ResultSet rs = stm.executeQuery();
            
            Role role = null;
            while (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                break;
            }
            
            return role;
        }
    }
}
