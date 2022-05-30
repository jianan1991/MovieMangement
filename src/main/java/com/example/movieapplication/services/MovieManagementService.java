package com.example.movieapplication.services;

import com.example.movieapplication.exceptions.MovieHasNotBeenInitializedException;
import com.example.movieapplication.exceptions.UnableToBookSeatsException;
import com.example.movieapplication.exceptions.UnableToCancelBooking;
import com.example.movieapplication.exceptions.UnableToViewMovieException;
import com.example.movieapplication.models.Movie;
import com.example.movieapplication.models.TicketDetails;

import java.util.List;

public interface MovieManagementService {
    Boolean setupMovie(Movie movie) throws MovieHasNotBeenInitializedException;
    List<TicketDetails> view(Long id) throws UnableToViewMovieException;
    List<String> showAvailability(Long id) throws MovieHasNotBeenInitializedException;
    TicketDetails bookSeats(Long id, String phoneNumber, String[] seatNumbers) throws UnableToBookSeatsException, MovieHasNotBeenInitializedException;
    Boolean cancelSeat(String ticketNo, String phoneNumber) throws UnableToCancelBooking;
}
