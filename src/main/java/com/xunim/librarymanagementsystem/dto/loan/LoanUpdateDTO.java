package com.xunim.librarymanagementsystem.dto.loan;

import com.xunim.librarymanagementsystem.model.Loan.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanUpdateDTO {

    private LocalDate actualReturnDate;
    private LoanStatus status;
    private String observations;
}
