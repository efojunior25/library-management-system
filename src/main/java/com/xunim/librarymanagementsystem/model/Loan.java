package com.xunim.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LoanStatus status = LoanStatus.ACTIVE;

    @Column(name = "fine_amount", precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @Column(length = 500)
    private String observations;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enum for loan status
    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE, RENEWED
    }

    // Utility methods
    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE &&
                LocalDate.now().isAfter(expectedReturnDate);
    }

    public long getOverdueDays() {
        if (actualReturnDate != null) {
            return Math.max(0, ChronoUnit.DAYS.between(expectedReturnDate, actualReturnDate));
        } else if (isOverdue()) {
            return ChronoUnit.DAYS.between(expectedReturnDate, LocalDate.now());
        }
        return 0;
    }

    public BigDecimal calculateFine() {
        long overdueDays = getOverdueDays();
        if (overdueDays > 0) {
            // $1.00 per day overdue
            return BigDecimal.valueOf(overdueDays);
        }
        return BigDecimal.ZERO;
    }
}
