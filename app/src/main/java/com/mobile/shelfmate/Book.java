package com.mobile.shelfmate;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private int totalPages;
    private int currentPage;
    private String status;
    private String genre; // Add genre field

    public Book(String title, String author, int totalPages, int currentPage, String status, String genre) {
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.status = status;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }
    public String getStatus() { return status; }
    public String getGenre() { return genre; } // Getter for genre

    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public void setStatus(String status) { this.status = status; }
    public void setGenre(String genre) { this.genre = genre; } // Setter for genre
}
