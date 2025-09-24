package com.rajanthari.springbootwithoutsecurity.books.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer publishedYear;
    private Double price;

    public Book() {}

    public Book(String title, String author, Integer publishedYear, Double price) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.price = price;
    }
}
