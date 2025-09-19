package com.xunim.librarymanagementsystem.repository;

import com.xunim.librarymanagementsystem.model.Reservation;
import com.xunim.librarymanagementsystem.model.Reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatus(ReservationStatus status);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByBookId(Long bookId);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'ACTIVE' AND r.expirationDate < :date")
    List<Reservation> findExpiredReservations(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'ACTIVE' AND r.expirationDate < CURRENT_DATE")
    Page<Reservation> findExpiredReservations(Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.book.id = :bookId AND r.status = 'ACTIVE' ORDER BY r.reservationDate ASC")
    List<Reservation> findActiveReservationsForBook(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status")
    Long countByStatus(@Param("status") ReservationStatus status);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = CURRENT_DATE")
    Long countReservationsToday();

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.status = 'ACTIVE'")
    List<Reservation> findActiveReservationsByUser(@Param("userId") Long userId);

    boolean existsByBookIdAndUserIdAndStatus(Long bookId, Long userId, ReservationStatus status);
}
