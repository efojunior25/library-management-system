package com.xunim.librarymanagementsystem.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "ISBN is required")
    @Size(max = 20, message = "ISBN must have at most 20 characters")
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must have at most 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author must have at most 100 characters")
    private String author;

    @Size(max = 50, message = "Category must have at most 50 characters")
    private String category;

    @NotNull(message = "Availability status is required")
    private Boolean available;

    private Integer timesBorrowed;
    private LocalDate publicationDate;

    @Size(max = 1000, message = "Description must have at most 1000 characters")
    private String description;

    private Integer pageCount;

    @Size(max = 100, message = "Publisher must have at most 100 characters")
    private String publisher;
}
