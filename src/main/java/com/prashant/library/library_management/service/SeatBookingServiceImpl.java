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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    public List<SeatBookingResponseDTO>getAllBooking()
    {
        List<SeatBooking>  seatBookings = bookingRepo.findAll();

      List<SeatBookingResponseDTO> result =   seatBookings.stream().map(this::mapToDTO).toList();

      return result;

    }

    public SeatBookingResponseDTO getUserBooking(@PathVariable Long id)
    {
        SeatBooking booking = bookingRepo.findByUserId(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with this User " + id);
        }
        SeatBookingResponseDTO response = mapToDTO(booking);
        return response;

    }

    public void leaveSeat(@PathVariable Long id)
    {
        User user = userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        SeatBooking booking = bookingRepo.findByUserId(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with this User " + id);
        }

        user.setSeatBooking(null);
        userRepo.save(user);
        bookingRepo.delete(booking);


    }

    public long CountSeatBooked()
    {
         return bookingRepo.count();
    }








    private  String generateAvailableSeat()
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

    public  List<String> generateAllAvailableSeat()
    {
        List<String> result = new ArrayList<>();
        Set<String> taken = bookingRepo.findAll().stream().map(SeatBooking::getSeatNumber).collect(Collectors.toSet());

        for(int i = 1;i<=TOTAL_SEATS;i++)
        {
            String seat = "S"+i;
            if(!taken.contains(seat))
            {
                result.add(seat);

            }
        }



        return result;

    }
}
