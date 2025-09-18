package com.xunim.librarymanagementsystem.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateDTO {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String observations;
}
