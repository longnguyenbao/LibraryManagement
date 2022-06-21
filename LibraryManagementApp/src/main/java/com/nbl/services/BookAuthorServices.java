/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.utils.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * @author admin
 */
public class BookAuthorServices {
    public boolean addBookAuthor(String bookId, ArrayList<Integer> authorIdList) throws SQLException {
        if (authorIdList.isEmpty()) {
            return false;
        }
        
        else if (!BookServices.checkBookExistenceById(bookId)) {
            return false;
        }
        
        try (Connection conn = JdbcUtils.getConn()) {
            LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(authorIdList);
            ArrayList<Integer> newAuthorIdList = new ArrayList<>(hashSet);

            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("INSERT INTO `book_author` VALUES (?, ?)");

            for (Integer authorId: newAuthorIdList) {
                stm.setString(1, bookId);
                stm.setInt(2, authorId);

                stm.executeUpdate();
            }
            conn.commit();

            return true;
        }
    }
    
    public boolean deleteBookAuthor(String bookId) throws SQLException {
        if (!BookServices.checkBookExistenceById(bookId)) {
            return false;
        }

        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);

            PreparedStatement stm = conn.prepareStatement("DELETE FROM `book_author` WHERE book_id = ?");

            stm.setString(1, bookId);

            stm.executeUpdate();
            conn.commit();

            return true;
        }
    }
}
