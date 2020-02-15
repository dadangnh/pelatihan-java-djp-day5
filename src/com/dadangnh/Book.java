package com.dadangnh;

import java.time.LocalDate;

public class Book extends ObjectEntity{
    private String author;
    private String description;
    private String keyword;
    private LocalDate createddate;
    private LocalDate availablefrom;
    private int stock;
    private int borrowed;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LocalDate getCreateddate() {
        return createddate;
    }

    public void setCreateddate(LocalDate createddate) {
        this.createddate = createddate;
    }

    public LocalDate getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(LocalDate availablefrom) {
        this.availablefrom = availablefrom;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }

    public int getStockAvailable() {
        int stockAvailable = this.getStock() - this.getBorrowed();

        // return the value of stock available, if stock is lower than or equal 0, return 0
        return Math.max(stockAvailable, 0);
    }


}
