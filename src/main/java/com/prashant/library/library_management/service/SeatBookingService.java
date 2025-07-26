package com.prashant.library.library_management.service;

import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingResponseDTO;

import java.util.List;
import java.util.Optional;

public interface SeatBookingService {
    SeatBookingResponseDTO bookSeat(SeatBookingRequestDTO dto);
    void leaveSeat(Long userId);
    List<String> getAvailableSeats();
    Optional<SeatBookingResponseDTO> gerUserBooking(Long userId);
    List<SeatBookingResponseDTO> getAllBooking();

}
