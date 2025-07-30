package com.prashant.library.library_management.initializer;

import com.prashant.library.library_management.entity.Seat;
import com.prashant.library.library_management.repository.SeatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class SeatInitializer implements CommandLineRunner {

    @Autowired
    private SeatRepo seatRepo;

    private static final int TOTAL_SEATS = 120;



    @Override
    public void run(String ...args)
    {
        if(seatRepo.count()==0)
        {
            List<Seat> seats = new ArrayList<>();
            for(int i = 1;i<=TOTAL_SEATS;i++)
            {
                String seatNumber = "S"+i;
                Seat seat = new Seat();
                seat.setSeatNumber(seatNumber);
                seat.setBooked(false);
                seats.add(seat);

            }
            seatRepo.saveAll(seats);
            System.out.println("initialized 120 Seats");
        }

    }

}
