package com.example.movieapplication.services;

import com.example.movieapplication.exceptions.MovieHasNotBeenInitializedException;
import com.example.movieapplication.exceptions.UnableToBookSeatsException;
import com.example.movieapplication.exceptions.UnableToCancelBooking;
import com.example.movieapplication.exceptions.UnableToViewMovieException;
import com.example.movieapplication.models.Movie;
import com.example.movieapplication.models.TicketDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class MovieManagementServiceTest {

    @InjectMocks
    private MovieManagementServiceImpl movieManagementService;

    private Movie regularMovie;
    private Movie cancellationZeroMovie;
    private TicketDetails ticketDetailsNoCancellation = null;
    private TicketDetails ticketDetails = null;
    private String phoneNumber;

    @BeforeEach
    void setUp() throws MovieHasNotBeenInitializedException, UnableToBookSeatsException {
        MockitoAnnotations.initMocks(this);
        movieManagementService = new MovieManagementServiceImpl();
        this.movieManagementService.init();

        regularMovie = new Movie(3L, 3, 3, 5);
        movieManagementService.setupMovie(regularMovie);
        phoneNumber = "1234";
        cancellationZeroMovie = new Movie(4L, 3, 3, 0);
        movieManagementService.setupMovie(cancellationZeroMovie);
        ticketDetailsNoCancellation = movieManagementService.bookSeats(cancellationZeroMovie.getId(), phoneNumber, new String[]{"A1","A2"});


        ticketDetails = movieManagementService.bookSeats(regularMovie.getId(), phoneNumber, new String[]{"A1","A2"});
    }

    @Test
    void setupMovieWithRepeatingId() throws MovieHasNotBeenInitializedException {
        Movie movie = new Movie(3L,10,10,5);
        Exception exception = assertThrows(MovieHasNotBeenInitializedException.class, () -> {
            movieManagementService.setupMovie(movie);
        });
        assertEquals(exception.getMessage(), "Movie ID has been taken, movie is not setup");
    }

    @Test
    void setupMovie() throws MovieHasNotBeenInitializedException {
        Movie movie = new Movie(1L,10,10,5);
        Assert.assertTrue(movieManagementService.setupMovie(movie));
    }
    @Test
    void view() throws UnableToViewMovieException {
        List<TicketDetails> ticketDetails = movieManagementService.view(regularMovie.getId());
        assertEquals(ticketDetails.size(), 1);
    }

    @Test
    void viewThrowsUnableToViewMovie()  {
        Exception exception = assertThrows(UnableToViewMovieException.class, () -> {
            movieManagementService.view(10L);
        });
        assertEquals(exception.getMessage(), "Movie has not been book, unable to view Movie");
    }



    @Test
    void showAvailability() throws MovieHasNotBeenInitializedException {
        List<String> availability = movieManagementService.showAvailability(3l);
        Assert.assertEquals(7, availability.size());
    }

    @Test
    void showAvailabilityThrowsMovieHasNotBeenInitialized()  {
        Exception exception = assertThrows(MovieHasNotBeenInitializedException.class, () -> {
            movieManagementService.showAvailability(10l);
        });
        assertEquals(exception.getMessage(), "Movie has not yet been setup, Please setup before checking availability");
    }

    @Test
    void bookSeats() throws MovieHasNotBeenInitializedException, UnableToBookSeatsException {
        String[] seatNumber = new String[]{"A3","B1"};
        TicketDetails ticketDetails = movieManagementService.bookSeats(regularMovie.getId(), "12345", seatNumber);
        assertEquals(ticketDetails.getMovieId(), regularMovie.getId());
        assertEquals(ticketDetails.getSeatNumber(), Arrays.asList(seatNumber));
        assertEquals(ticketDetails.getPhoneNumber(),"12345");
    }

    @Test
    void bookSeatswithrepeatedphoneNumber() throws MovieHasNotBeenInitializedException, UnableToBookSeatsException {
        String[] seatNumber = new String[]{"A3","B1"};
        Exception exception = assertThrows(UnableToBookSeatsException.class, () -> {
            movieManagementService.bookSeats(regularMovie.getId(), "1234", seatNumber);
        });
        assertEquals(exception.getMessage(), "Your phone number has booked for the mobile/A Booking that you have booked might not be available anymore");
    }

    @Test
    void bookSeatsMovieHasNotBeenInitializedException() {
        String[] seatNumber = new String[]{"A3","B1"};
        Exception exception = assertThrows(MovieHasNotBeenInitializedException.class, () -> {
            movieManagementService.bookSeats(10L, "12345", seatNumber);
        });
        assertEquals(exception.getMessage(), "Movie has not yet been setup, Please setup before booking");
    }

    @Test
    void bookSeatsMovieUnableToBookSeatsException()  {
        String[] seatNumber = new String[]{"AAA","ABB"};
        Exception exception = assertThrows(UnableToBookSeatsException.class, () -> {
            movieManagementService.bookSeats(regularMovie.getId(), "1234", seatNumber);
        });
        assertEquals(exception.getMessage(), "Your phone number has booked for the mobile/A Booking that you have booked might not be available anymore");
    }

    @Test
    void cancelSeat() throws UnableToCancelBooking {
        Boolean cancelSeat = movieManagementService.cancelSeat(ticketDetails.getTicketNumber(),phoneNumber);
        assertEquals(cancelSeat, true);
    }
    @Test
    void cancelSeatUnableToCancelBookingDueToWindow() {
        Exception exception = assertThrows(UnableToCancelBooking.class, () -> {
                    movieManagementService.cancelSeat(ticketDetailsNoCancellation.getTicketNumber(),phoneNumber);
        });
        assertEquals(exception.getMessage(), "Unable to cancel booking as 0 mins cancellation window has past!");

    }

    @Test
    void cancelSeatUnableToCancelBookingDueToInvalidTicket() {
        Exception exception = assertThrows(UnableToCancelBooking.class, () -> {
            movieManagementService.cancelSeat("123123",phoneNumber);
        });
        assertEquals(exception.getMessage(), "Unable to find booking for cancellation");

    }
}