package com.example.pilaskow.bookrentallocator.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    String title;
    ArrayList author;
    String year;
    String publishingHouse;
    String condition;

    public String getiSBN() {
        return iSBN;
    }

    public void setiSBN(String iSBN) {
        this.iSBN = iSBN;
    }

    String iSBN;

    public Book(String iSBN, String title, ArrayList author, String year, String publishingHouse, String condition){
        this.iSBN = iSBN;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publishingHouse = publishingHouse;
        this.condition = condition;
    }

    public Book(){

    }
}
