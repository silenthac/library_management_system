package com.prashant.library.library_management.repository;

import com.prashant.library.library_management.entity.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatBookingRepo extends JpaRepository<SeatBooking,Long> {

    boolean existsByUserId(Long userId);
    SeatBooking findByUserId(Long userId);
    boolean existsBySeatNumber(String seatNumber);
    long count();


}
