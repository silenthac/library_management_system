package com.prashant.library.library_management.service;

import com.prashant.library.library_management.dto.*;
import com.prashant.library.library_management.entity.Seat;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.exceptions.ResourceNotFoundException;
import com.prashant.library.library_management.exceptions.SeatAlreadyBookedException;
import com.prashant.library.library_management.exceptions.UserAlreadyBookedSeatException;
import com.prashant.library.library_management.repository.SeatRepo;
import com.prashant.library.library_management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SeatService {




    private final SeatRepo seatRepo;
    private final UserRepository userRepository;

    public SeatService(SeatRepo seatRepo , UserRepository userRepository)
    {
        this.seatRepo = seatRepo;
        this.userRepository = userRepository;
    }


    /**
     * Fetch all seats from the system.
     * @return list of all seats present in our system
     */
    public List<SeatBookingResponseDTO> getAllSeats()
    {
        List<Seat> response  = seatRepo.findAll();
        return  MapSeatResponseTOSeatResponseDTO(response);


    }


    /**
     * Fetch all available seats from the system
     *
     * @return all the availble (unbooked) seats
     */
    public List<SeatBookingResponseDTO> getAvailableSeats()
    {
        List<Seat> response = seatRepo.findByIsBookedFalse();
        return MapSeatResponseTOSeatResponseDTO(response);


    }


    /**
     *Fetch all the booked seats from the system
     * @return all the booked seats
     */
    public List<SeatBookingResponseDTO> getBookedSeats()
    {
        List<Seat> response = seatRepo.findByIsBookedTrue();
        return  MapSeatResponseTOSeatResponseDTO(response);


    }

    /**
     *
     * @param id
     * @return Get Specific Seat info
     */

    public SeatBookingResponseDTO  getSeatinfo(String id)
    {
        Seat response = seatRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Seat Id is not Present in the system"));

        return convertToDTO(response);

    }


    /**
     *  Get user booked seat
     */

    public SeatBookingResponseDTO getUserBookedSeat(Long id) {
//        User user = Optional.ofNullable(userRepository.findByEmail(dto.getEmail()))
//                .orElseThrow(() -> new ResourceNotFoundException("This user is not present in our system"));
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("This User is not present in our system"));

        Seat seat = Optional.ofNullable(seatRepo.findByBookedBy_Id(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("No seat booked by this user"));

        return convertToDTO(seat);
    }


    public SeatBookingResponseDTO bookSeat(SeatBookingRequestDTO dto )
    {
        // Step 1: Fetch the user

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 2: Fetch the seat

        Seat seat = seatRepo.findBySeatNumber(dto.getSeatNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        // Step 3: Check if seat is already booked

        if (seat.isBooked()) {
            throw new SeatAlreadyBookedException("Seat is already booked");
        }

        Seat alreadyBookedSeat = seatRepo.findByBookedBy_Id(dto.getUserId());
        if (alreadyBookedSeat != null) {
            throw new UserAlreadyBookedSeatException("User already has a booked seat: " + alreadyBookedSeat.getSeatNumber());
        }


        // Step 4: Assign the user to the seat and mark it booked
        seat.setBooked(true);
        seat.setBookedBy(user);
        seat.setBookedAt(LocalDate.now());

        // Step 5: Save updated seat
        Seat updatedSeat = seatRepo.save(seat);

        // Step 6: Return response DTO
        return convertToDTO(updatedSeat);
    }

    public void leaveSeat(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Seat seat = seatRepo.findByBookedBy_Id(id);
        if (seat == null) {
            throw new ResourceNotFoundException("No seat booked by this user");
        }

        // Unassign seat from user
        user.setSeat(null);
        userRepository.save(user);

        // Unbook seat
        seat.setBooked(false);
        seat.setBookedBy(null);  // Also clear who booked it
        seat.setBookedAt(null);  // Clear booking time if needed
        seatRepo.save(seat);     // ✅ save the updated seat
    }





    /**
     *
     * @param response
     * @return mapping the response which we get to the resposne we need to send to the client
     */


    private List<SeatBookingResponseDTO> MapSeatResponseTOSeatResponseDTO(List<Seat> response)
    {
        return response.stream().map(this::convertToDTO).toList();
    }






    private SeatBookingResponseDTO convertToDTO(Seat seat)
    {
        SeatBookingResponseDTO responseDTO = new SeatBookingResponseDTO();




        responseDTO.setSeatNumber(seat.getSeatNumber());
        responseDTO.setBooked(seat.isBooked());
        responseDTO.setBookedAt(seat.getBookedAt());
        User data = seat.getBookedBy();
        UserSummaryDTO result = (data!=null)? convertToUserSummaryDTO(data):null;
        responseDTO.setBookedBy(result);
        return responseDTO;



    }

    private UserSummaryDTO convertToUserSummaryDTO(User data)
    {
        UserSummaryDTO user = new UserSummaryDTO();
        user.setUserId(data.getId());
        user.setEmail(data.getEmail());
        user.setName(data.getFullName());
        return user;



    }



}
