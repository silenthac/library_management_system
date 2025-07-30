package com.prashant.library.library_management.repository;

import com.prashant.library.library_management.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepo extends JpaRepository<Seat,String> {
//        boolean existsByUserId(Long userId);
//        Seat findByUserId(Long userId);
//        boolean existsBySeatNumber(String seatNumber);
        long count();
        List<Seat> findByIsBookedFalse();
        List<Seat> findByIsBookedTrue();
        Seat findByBookedBy_Id(Long userId);
//        boolean existsByBookedBy_Id(Long userId);
//        long countByIsBookedTrue();

        Optional<Seat> findBySeatNumber(String seatNumber);
}
