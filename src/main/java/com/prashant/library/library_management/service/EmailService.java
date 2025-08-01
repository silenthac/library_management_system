package com.prashant.library.library_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendToUser(String toEmail,String userName, String seatNumber,String amount)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Library Seat Booking Confirmation");
        message.setTo(toEmail);

        String body = String.format("Dear %s,\n\nYour seat booking was successful. \n\n"+
                "Seat Number : %s\nAmount Paid: %s\n\nThank you,\nLibrary Team",userName,seatNumber,amount);

        message.setText(body);
        try {
            mailSender.send(message);
            log.info("Confirmation email sent to user: {}", toEmail);
        } catch (MailException e) {
            log.error("Failed to send confirmation email to user {}: {}", toEmail, e.getMessage(), e);
            // Optionally alert or notify here
        }

    }

    @Async
    public void sendToAdmins(List<String> adminEmails, String userName, String seatNumber, String amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmails.toArray(new String[0]));
        message.setSubject("New Seat Booking Alert");

        String body = String.format(
                "Hello Admin,\n\nA new seat booking was made.\n\n" +
                        "User: %s\nSeat Number: %s\nAmount Paid: ₹%s\n\nLibrary System",
                userName, seatNumber, amount);

        message.setText(body);
        mailSender.send(message);
    }
}
