package com.prashant.library.library_management.controller;
import com.prashant.library.library_management.dto.PaymentRequestDTO;
import com.prashant.library.library_management.dto.SeatBookingRequestDTO;
import com.prashant.library.library_management.entity.Payment;
import com.prashant.library.library_management.entity.User;
import com.prashant.library.library_management.exceptions.ResourceNotFoundException;
import com.prashant.library.library_management.exceptions.SeatAlreadyBookedException;
import com.prashant.library.library_management.exceptions.UserAlreadyBookedException;
import com.prashant.library.library_management.exceptions.UserAlreadyBookedSeatException;
import com.prashant.library.library_management.repository.PaymentRepository;
import com.prashant.library.library_management.repository.UserRepository;
import com.prashant.library.library_management.service.EmailService;
import com.prashant.library.library_management.service.PaymentService;
import com.prashant.library.library_management.service.SeatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @PostMapping
    @Transactional
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestDTO request)
    {
        try{
            CompletableFuture<Boolean> result = paymentService
                    .processPayment(request.getUserId(), request.getAmount());

            System.out.println(result);

            boolean success = result.get(7, TimeUnit.SECONDS);




            if(success)
            {
                try {


                    SeatBookingRequestDTO bookingRequest = new SeatBookingRequestDTO();
                    bookingRequest.setUserId(request.getUserId());
                    bookingRequest.setSeatNumber(request.getSeatNumber());
                    seatService.bookSeat(bookingRequest);
                    Payment payment = new Payment();
                    payment.setPaymentDate(LocalDate.now());
                    payment.setAmount(request.getAmount());
                    payment.setStatus("SUCCESS");
                    User user = userRepository.findById(request.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    payment.setUser(user);
                    paymentRepository.save(payment);
                    emailService.sendToUser(user.getEmail(), user.getFullName(), request.getSeatNumber(), String.valueOf(payment.getAmount()));

                    List<String> adminEmails = userRepository.findAllAdmins()
                            .stream()
                            .peek(System.out::println)
                            .map(User::getEmail)
                            .collect(Collectors.toList());

                    emailService.sendToAdmins(adminEmails, user.getFullName(), request.getSeatNumber(), String.valueOf(payment.getAmount()));


                    return ResponseEntity.ok("Payment Successful and seat booked!");
                }
                catch(ResourceNotFoundException e)
                {
//                    log.warn("❗ Seat {} already booked by another user {}. Initiating refund.", request.getSeatNumber(), request.getUserId());
                    paymentService.refundPayment(request.getUserId(), request.getAmount());

                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(e.getMessage());

                }
                catch (UserAlreadyBookedSeatException e)
                {
//                    log.warn("❗ Seat {} already booked by another user {}. Initiating refund.", request.getSeatNumber(), request.getUserId());
                    paymentService.refundPayment(request.getUserId(),  request.getAmount());

                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("❗ user already booked another seat. Refund initiated.");

                }
                catch (Exception e)
                {
//                    log.warn("❗ Seat {} already booked by another user {}. Initiating refund.", request.getSeatNumber(), request.getUserId());
                    paymentService.refundPayment(request.getUserId(), request.getAmount());

                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(" Unknown error");

                }
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Payment failed.");
            }

        }
        catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                    .body("⏱️ Payment timed out. Please try again.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Payment processing error.");
        }

    }
}
