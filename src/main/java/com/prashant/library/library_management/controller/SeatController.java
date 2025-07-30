package com.prashant.library.library_management.controller;


import com.prashant.library.library_management.dto.CancelSeatRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingResponseDTO;
import com.prashant.library.library_management.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService)
    {
        this.seatService = seatService;

    }


    //Get All Seats

    @GetMapping()
    public ResponseEntity<List<SeatBookingResponseDTO>> getAllSeats()
    {
        List<SeatBookingResponseDTO> list =  seatService.getAllSeats();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<SeatBookingResponseDTO>> getAvailableSeats()
    {
        List<SeatBookingResponseDTO> list = seatService.getAvailableSeats();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/booked")
    public ResponseEntity<List<SeatBookingResponseDTO>> getBookedSeats()
    {
        List<SeatBookingResponseDTO> list = seatService.getBookedSeats();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatBookingResponseDTO> getSeatInfo(@PathVariable String id)
    {
        SeatBookingResponseDTO response = seatService.getSeatinfo(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{userId}/seat")
    public ResponseEntity<SeatBookingResponseDTO> getBookedSeatByUser(@PathVariable Long userId)
    {
        SeatBookingResponseDTO response = seatService.getUserBookedSeat(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/booked")
    public ResponseEntity<SeatBookingResponseDTO> bookSeat(@RequestBody SeatBookingRequestDTO dto)
    {
        SeatBookingResponseDTO result = seatService.bookSeat(dto);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> leaveSeat(@RequestBody CancelSeatRequestDTO request)
    {
        Long userId = request.getUserId();
        seatService.leaveSeat(userId);
        return  ResponseEntity.ok("Seat is Cancelled Successfully");
    }











}
