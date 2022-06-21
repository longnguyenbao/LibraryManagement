/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.services;

import com.nbl.pojo.BookDetail;
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
public class BookDetailServices {
    public List<BookDetail> getBookDetailList(String kw, Integer searchCondition) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            PreparedStatement stm;
            String sql = "SELECT * FROM `book` ";
            if (kw == null)
                kw = "";

            if(searchCondition != null) {
                switch(searchCondition) {
                    case 0:
                        sql += "WHERE name like concat('%',?,'%') ";
                        stm = conn.prepareStatement(sql);
                        stm.setString(1, kw);
                        return addBookDetailList(stm);
                    case 1:
                        sql += "WHERE id IN "
                                + "(SELECT book_id FROM book_author WHERE author_id IN "
                                + "(SELECT id FROM author WHERE name LIKE concat('%',?,'%')))";
                        stm = conn.prepareStatement(sql);
                        stm.setString(1, kw);
                        return addBookDetailList(stm);
                    case 2:
                        sql += "WHERE publication_year like concat('%',?,'%')";
                        stm = conn.prepareStatement(sql);
                        stm.setString(1, kw);
                        return addBookDetailList(stm);
                    case 3:
                        sql += "WHERE category_id like concat('%',?,'%')";
                        stm = conn.prepareStatement(sql);
                        stm.setString(1, kw);
                        return addBookDetailList(stm);
                    default:
                        break;
                }
            }

            stm = conn.prepareStatement(sql);
            
            return addBookDetailList(stm);
        }
    }
    
    private List<BookDetail> addBookDetailList(PreparedStatement stm) throws SQLException {
        List<BookDetail> bookDetails = new ArrayList<>();
        ResultSet rs = stm.executeQuery();
        
        Book book;
        
        while (rs.next()) {                
            String id = rs.getString("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            Integer publicationYear = rs.getInt("publication_year");
            String publicationPlace = rs.getString("publication_place");
            Date entryDate = rs.getDate("entry_date");
            String location = rs.getString("location");
            Integer categoryId = rs.getInt("category_id");

            book = new Book(id, name, description, publicationYear, publicationPlace, entryDate, location, categoryId);
            
            bookDetails.add(new BookDetail(book, CategoryServices.getCategoryById(categoryId)));
        }
        
        return bookDetails;
    }
}
