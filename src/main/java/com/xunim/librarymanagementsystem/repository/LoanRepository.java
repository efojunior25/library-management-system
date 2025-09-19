package com.xunim.librarymanagementsystem.repository;

import com.xunim.librarymanagementsystem.model.Loan;
import com.xunim.librarymanagementsystem.model.Loan.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

    List<Loan> findByUserId(Long userId);

    Page<Loan> findByUserId(Long userId, Pageable pageable);

    List<Loan> findByBookId(Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.expectedReturnDate < :date")
    List<Loan> findOverdueLoans(@Param("date") LocalDate date);

    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.expectedReturnDate < CURRENT_DATE")
    Page<Loan> findOverdueLoans(Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByUser(@Param("userId") Long userId);

    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByBook(@Param("bookId") Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.loanDate BETWEEN :startDate AND :endDate")
    List<Loan> findByDateRange(@Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = :status")
    Long countByStatus(@Param("status") LoanStatus status);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.status = 'ACTIVE' AND l.expectedReturnDate < CURRENT_DATE")
    Long countOverdue();

    // Statistics
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.loanDate = CURRENT_DATE")
    Long countLoansToday();

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.actualReturnDate = CURRENT_DATE")
    Long countReturnsToday();
}
