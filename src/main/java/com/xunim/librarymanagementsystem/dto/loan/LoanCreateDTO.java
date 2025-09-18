package com.xunim.librarymanagementsystem.dto.loan;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanCreateDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String observations;
}
