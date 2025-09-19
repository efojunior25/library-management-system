package com.xunim.librarymanagementsystem.repository;

import com.xunim.librarymanagementsystem.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByActive(Boolean active);

    Page<Category> findByActive(Boolean active, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Category> searchByName(@Param("term") String term, Pageable pageable);

    @Query("SELECT c FROM Category c ORDER BY SIZE(c.books) DESC")
    List<Category> findCategoriesWithMostBooks();
}
