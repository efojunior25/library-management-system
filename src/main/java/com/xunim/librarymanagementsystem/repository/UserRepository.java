package com.xunim.librarymanagementsystem.repository;

import com.xunim.librarymanagementsystem.model.User;
import com.xunim.librarymanagementsystem.model.User.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserCode(String userCode);

    Optional<User> findByEmail(String email);

    boolean existsByUserCode(String userCode);

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);

    Page<User> findByStatus(UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.userCode) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<User> searchByTerm(@Param("term") String term, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.loans l WHERE l.actualReturnDate IS NULL")
    List<User> findUsersWithActiveLoans();

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.actualReturnDate IS NULL")
    Integer countActiveLoansByUserId(@Param("userId") Long userId);
}
