package com.prashant.library.library_management.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    @Async
    public CompletableFuture<Boolean> processPayment(Long  userId, BigDecimal amount)
    {
        try{
            Thread.sleep(6000); //6seconds
        }catch(InterruptedException ex)
        {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(true);

    }

    public void refundPayment(Long userId,  BigDecimal amount) {
        // Simulate refund logic
//        log.info("Initiating refund for User: {}, Seat: {}, Amount: {}", userId, seatNumber, amount);

        // Here you'd call a real payment gateway to process the refund.
        // Simulate delay
        try {
            Thread.sleep(2000); // simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

//       log.info("Refund successful for User: {}, Amount: {}", userId, amount);
    }
}
