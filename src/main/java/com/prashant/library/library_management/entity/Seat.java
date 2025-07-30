package com.prashant.library.library_management.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    private String seatNumber;

    private boolean isBooked= false;

    @OneToOne
    @JoinColumn(name="user_id",unique = true)
    private User bookedBy;

    private LocalDate bookedAt;

    public LocalDate getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDate bookedAt) {
        this.bookedAt = bookedAt;
    }



    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    protected void OnCreate()
    {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDate.now();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }
}
