package com.prashant.library.library_management.service;


import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingResponseDTO;
import com.prashant.library.library_management.entity.SeatBooking;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.exceptions.AllSeatsBookException;
import com.prashant.library.library_management.exceptions.ResourceNotFoundException;
import com.prashant.library.library_management.exceptions.UserAlreadyBookedException;
import com.prashant.library.library_management.repository.SeatBookingRepo;
import com.prashant.library.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public  class SeatBookingServiceImpl  {


    private final  SeatBookingRepo bookingRepo;

    private final  UserRepository userRepo;

    private static final  int TOTAL_SEATS = 120;
    @Autowired
    public SeatBookingServiceImpl(SeatBookingRepo bookingrepo , UserRepository userRepo)
    {
        this.bookingRepo = bookingrepo;
        this.userRepo = userRepo;
    }


    public SeatBookingResponseDTO bookSeat(SeatBookingRequestDTO dto) {
        if(bookingRepo.existsByUserId(dto.getUserId()))
        {
            throw new UserAlreadyBookedException("User has already booked a seat");
        }

        if(bookingRepo.count()>=TOTAL_SEATS)
        {
            throw new AllSeatsBookException("All seats are Booked");
        }

        User user = userRepo.findById(dto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));

//        String seatNumber = dto.getSeatNumber()!=null?dto.getSeatNumber():generateAvailableSeat();

        String requestedSeat = dto.getSeatNumber();
        String finalSeatNumber;

        if (requestedSeat != null && !bookingRepo.existsBySeatNumber(requestedSeat)) {
            finalSeatNumber = requestedSeat;
        } else {
            // Seat was either not provided or already taken, assign an available one
            finalSeatNumber = generateAvailableSeat();
            if (finalSeatNumber == null) {
                throw new RuntimeException("No available seat found");
            }
        }



        SeatBooking booking = new SeatBooking();
        booking.setSeatNumber(finalSeatNumber);
        booking.setUser(user);
        booking.setBookedAt(LocalDate.now());
        bookingRepo.save(booking);
        return mapToDTO(booking);



    }


    private String generateAvailableSeat()
    {
        String result = "";
        Set<String> taken = bookingRepo.findAll().stream().map(SeatBooking::getSeatNumber).collect(Collectors.toSet());

        for(int i = 1;i<=TOTAL_SEATS;i++)
        {
            String seat = "S"+i;
            if(!taken.contains(seat))
            {
                result =  seat;
                break;
            }
        }

        if(result.isEmpty())
        {
            throw new RuntimeException("No Available Seat found");
        }

        return result;

    }


    private SeatBookingResponseDTO mapToDTO(SeatBooking booking)
    {
        SeatBookingResponseDTO responseDTO = new SeatBookingResponseDTO();
        responseDTO.setSeatNumber(booking.getSeatNumber());
        responseDTO.setUserId(booking.getUser().getId());
        responseDTO.setBookedAt(booking.getBookedAt());
        responseDTO.setUserName(booking.getUser().getFullName());
        return responseDTO;

    }
}
