package com.prashant.library.library_management.controller;


import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingResponseDTO;
import com.prashant.library.library_management.service.SeatBookingService;
import com.prashant.library.library_management.service.SeatBookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
