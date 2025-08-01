package com.prashant.library.library_management.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {

    private Long userId;
//    private String SeatNumber;
    private BigDecimal amount;

    private String seatNumber;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
