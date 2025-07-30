package com.prashant.library.library_management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserSeatResponseDTO {
    private Long userId;
    private String seatNumber;
    private LocalDate bookedAt;

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDate bookedAt) {
        this.bookedAt = bookedAt;
    }


}
