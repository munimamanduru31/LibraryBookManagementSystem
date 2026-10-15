
package com.library.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Book {

    @Positive(message = "Book ID must be positive")
    private int bookId;

    @NotBlank(message = "Book name cannot be empty")
    @Size(min = 2, max = 50,
          message = "Book name must be between 2 and 50 characters")
    private String bookName;

    @NotBlank(message = "Author name cannot be empty")
    @Size(min = 2, max = 40,
          message = "Author name must be between 2 and 40 characters")
    private String author;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Availability must be specified")
    private Boolean available;

    public Book() {
    }

    public Book(int bookId, String bookName, String author,
                double price, Boolean available) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.available = available;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{bookId=" + bookId
                + ", bookName='" + bookName + '\''
                + ", author='" + author + '\''
                + ", price=" + price
                + ", available=" + available + '}';
    }
}
