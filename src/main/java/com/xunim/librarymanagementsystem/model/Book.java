package com.xunim.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(length = 50)
    private String category;

    @Column(name = "available", nullable = false)
    @Builder.Default
    private Boolean available = true;

    @Column(name = "times_borrowed")
    @Builder.Default
    private Integer timesBorrowed = 0;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(length = 1000)
    private String description;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(length = 100)
    private String publisher;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Utility methods
    public void borrowBook() {
        this.available = false;
        this.timesBorrowed = (this.timesBorrowed == null ? 0 : this.timesBorrowed) + 1;
    }

    public void returnBook() {
        this.available = true;
    }
}
