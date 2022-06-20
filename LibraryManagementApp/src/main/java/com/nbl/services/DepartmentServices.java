/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.Department;
import com.nbl.utils.JdbcUtils;
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
public class DepartmentServices {
    public List<Department> getDepartmentList() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `department`");
            
            ResultSet rs = stm.executeQuery();
            List<Department> departments = new ArrayList<>();
            
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                departments.add(new Department(id, name));
            }
            
            return departments;
        }
    }
    
    public static Department getDepartmentById(Integer departmentId) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `department` WHERE `id` = ?");
            stm.setInt(1, departmentId);
            ResultSet rs = stm.executeQuery();
            
            Department department = null;
            while (rs.next()) {
                department = new Department();
                department.setId(rs.getInt("id"));
                department.setName(rs.getString("name"));
                break;
            }
            
            return department;
        }
    }
}
