package com.xunim.librarymanagementsystem.dto.reservation;

import com.xunim.librarymanagementsystem.dto.book.BookDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;
    private BookDTO book;
    private UserDTO user;
    private LocalDate reservationDate;
    private LocalDate expirationDate;
    private Reservation.ReservationStatus status;
    private String observations;
    private Boolean isExpired;
}
