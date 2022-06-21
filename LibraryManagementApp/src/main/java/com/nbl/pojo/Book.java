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
public class Book {
    private String id;
    private String name;
    private String description;
    private Integer publicationYear;
    private String publicationPlace;
    private Date entryDate;
    private String location;
    private Integer categoryId;

    public Book(String id, String name, String description, Integer publicationYear, String publicationPlace, Date entryDate, String location, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publicationYear = publicationYear;
        this.publicationPlace = publicationPlace;
        this.entryDate = entryDate;
        this.location = location;
        this.categoryId = categoryId;
    }

    public Book() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(String publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    
}
