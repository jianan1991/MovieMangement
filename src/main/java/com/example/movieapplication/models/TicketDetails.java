package com.example.movieapplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class TicketDetails {
    private String ticketNumber ;
    private LocalDateTime bookingTime;
    private Long movieId;
    private String phoneNumber;
    private List<String> seatNumber;
    private TicketStatus ticketStatus;

    public TicketDetails(LocalDateTime bookingTime, Long movieId, String phoneNumber, List<String> seatNumber, TicketStatus ticketStatus) {
        this.bookingTime = bookingTime;
        this.movieId = movieId;
        this.phoneNumber = phoneNumber;
        this.seatNumber = seatNumber;
        this.ticketStatus = ticketStatus;
        this.ticketNumber = UUID.randomUUID().toString();
    }

    @Override
    public String toString(){
        return String.format(
                "Show Number: %d\n" +
                "Ticket Number: %s\n" +
                "Buyer Phone: %s\n" +
                "Seat Number: %s\n\n",movieId,ticketNumber,phoneNumber,seatNumber);
    }

}
