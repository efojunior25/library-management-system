package com.xunim.librarymanagementsystem.repository;

import com.xunim.librarymanagementsystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByAvailable(Boolean available);

    Page<Book> findByAvailable(Boolean available, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.category) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Book> searchByTerm(@Param("term") String term, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.category) = LOWER(:category)")
    Page<Book> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    Page<Book> findByAuthorContainingIgnoreCase(@Param("author") String author, Pageable pageable);

    @Query("SELECT b FROM Book b ORDER BY b.timesBorrowed DESC")
    Page<Book> findMostPopular(Pageable pageable);

    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.category IS NOT NULL ORDER BY b.category")
    List<String> findAllCategories();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.available = true")
    Long countAvailable();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.available = false")
    Long countLoaned();
}
