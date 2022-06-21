/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.Author;
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
public class AuthorServices {
    public List<Author> getAuthorList() throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM `author`");
            
            ResultSet rs = stm.executeQuery();
            List<Author> authors = new ArrayList<>();
            
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                authors.add(new Author(id, name));
            }
            
            return authors;
        }
    }
    
    public List<Author> getAuthorListByBookId(String kw) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM author WHERE id IN "
                            + "(SELECT author_id FROM book_author WHERE book_id = ?)"
            );
            if (kw == null)
                kw = "";
            
            stm.setString(1, kw);
            ResultSet rs = stm.executeQuery();
            
            List<Author> authors = new ArrayList<>();
            while (rs.next()) {                
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                
                authors.add(new Author(id, name));
            }
            
            return authors;
        }
    }
}
