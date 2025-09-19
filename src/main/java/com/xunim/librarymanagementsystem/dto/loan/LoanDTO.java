package com.xunim.librarymanagementsystem.dto.loan;

import com.xunim.librarymanagementsystem.dto.book.BookDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.model.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {

    private Long id;
    private BookDTO book;
    private UserDTO user;
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private Loan.LoanStatus status;
    private BigDecimal fineAmount;
    private String observations;
    private Boolean isOverdue;
    private Long overdueDays;
}
