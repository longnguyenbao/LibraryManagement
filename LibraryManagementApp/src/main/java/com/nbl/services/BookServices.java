/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.Book;
import com.nbl.utils.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 *
 * @author admin
 */
public class BookServices {
    public static boolean checkBookExistenceById(String id) throws SQLException {
        if (id.isBlank() == true) {
            return false;
        }

        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm = conn.prepareStatement("SELECT id FROM book WHERE id = ?");

            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.isBeforeFirst()) {
                return true;
            }

            return false;
        }
    }
    
    public Integer deleteBook(String id) throws SQLException {
        if (id == null) {
            return 10;
        }
        else if (!checkBookExistenceById(id)) {
            return 11;
        }
        
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement("DELETE FROM `book` WHERE id = ?");

            stm.setString(1, id);

            stm.executeUpdate();
            conn.commit();

            return 1;
        }
    }
    
    public Integer addBook(Book book) throws SQLException {
        if(book.getId() == null || book.getId().isBlank()) {
            return 10;
        }
        
        else if(book.getName() == null || book.getName().isBlank()) {
            return 11;
        }
        
        else if(checkBookExistenceById(book.getId())) {
            return 12;
        }
        
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement("INSERT INTO `book` VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

            stm.setString(1, book.getId());
            stm.setString(2, book.getName());
            stm.setString(3, book.getDescription());
            stm.setInt(4, book.getPublicationYear());
            stm.setString(5, book.getPublicationPlace());
            stm.setDate(6, book.getEntryDate());
            stm.setString(7, book.getLocation());
            stm.setInt(8, book.getCategoryId());

            stm.executeUpdate();
            conn.commit();

            return 1;
        }
    }
}
