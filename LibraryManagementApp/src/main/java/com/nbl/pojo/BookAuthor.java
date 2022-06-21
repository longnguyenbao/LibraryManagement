/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nbl.pojo;

/**
 *
 * @author admin
 */
public class BookAuthor {
    private String bookId;
    private Integer authorId;

    public BookAuthor(String bookId, Integer authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public BookAuthor() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}
