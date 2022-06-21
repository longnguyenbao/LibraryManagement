/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.pojo;

import java.sql.Date;

/**
 *
 * @author admin
 */
public class BookDetail {
    private Book book;
    private Category category;

    public BookDetail(Book book, Category category) {
        this.book = book;
        this.category = category;
    }

    public BookDetail() {
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String getBookId() {
        return this.book.getId();
    }
    
    public String getBookName() {
        return this.book.getName();
    }
    
    public String getDescription() {
        return this.book.getDescription();
    }
    
    public Integer getPublicationYear() {
        return this.book.getPublicationYear();
    }
    
    public String getPublicationPlace() {
        return this.book.getPublicationPlace();
    }
    
    public Date getEntryDate() {
        return this.book.getEntryDate();
    }
    
    public String getLocation() {
        return this.book.getLocation();
    }
    
    public Integer getCategoryId() {
        return this.book.getCategoryId();
    }
    
    public String getCategoryName() {
        return this.category.getName();
    }
}
