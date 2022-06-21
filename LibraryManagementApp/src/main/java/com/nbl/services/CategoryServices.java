/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.Category;
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
public class CategoryServices {
    public List<Category> getCategoryList() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM category");
            
            ResultSet rs = stm.executeQuery();
            List<Category> categories = new ArrayList<>();
            
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                categories.add(new Category(id, name));
            }
            
            return categories;
        }
    }
    
    public static Category getCategoryById(Integer id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `category` WHERE `id` = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            
            Category category = null;
            while (rs.next()) {
                category = new Category();
                
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                
                break;
            }
            
            return category;
        }
    }
}
