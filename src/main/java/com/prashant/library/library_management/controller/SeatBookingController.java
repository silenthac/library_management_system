package com.prashant.library.library_management.controller;
import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingResponseDTO;
import com.prashant.library.library_management.service.SeatBookingService;
import com.prashant.library.library_management.service.SeatBookingServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/seats")
public class SeatBookingController {

    private final SeatBookingServiceImpl seatBookingService;

    @Autowired
    public SeatBookingController(SeatBookingServiceImpl seatBookingService) {
        this.seatBookingService = seatBookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<SeatBookingResponseDTO> bookSeat(@RequestBody SeatBookingRequestDTO requestDTO)
    {
        SeatBookingResponseDTO dto = seatBookingService.bookSeat(requestDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<List<SeatBookingResponseDTO>> getAllBookings()
    {
        List<SeatBookingResponseDTO> bookings = seatBookingService.getAllBooking();
        return new ResponseEntity<>( bookings, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<SeatBookingResponseDTO> getUserBooking(@PathVariable Long id)
    {
        SeatBookingResponseDTO result = seatBookingService.getUserBooking(id);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> CancelSeat(@PathVariable Long id)
    {
        seatBookingService.leaveSeat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> CountSeatBooked()
    {
        long result = seatBookingService.CountSeatBooked();
        return new  ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<String>> getAllAvailableSeat()
    {
        List<String> result = seatBookingService.generateAllAvailableSeat();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }










}
