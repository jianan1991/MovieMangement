package com.example.movieapplication.services;

import com.example.movieapplication.exceptions.MovieHasNotBeenInitializedException;
import com.example.movieapplication.exceptions.UnableToBookSeatsException;
import com.example.movieapplication.exceptions.UnableToCancelBooking;
import com.example.movieapplication.exceptions.UnableToViewMovieException;
import com.example.movieapplication.models.Movie;
import com.example.movieapplication.models.TicketDetails;
import com.example.movieapplication.models.TicketStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MovieManagementServiceImpl implements MovieManagementService{

    private HashMap<Long, Movie> movieHashMap;
    private HashMap<Long, List<TicketDetails>> ticketDetailsHashMap;

    @PostConstruct
    void init(){
        movieHashMap = new HashMap<>();
        ticketDetailsHashMap = new HashMap<>();
    }

    public Boolean setupMovie(Movie movie) throws MovieHasNotBeenInitializedException {
        if(movieHashMap.get(movie.getId())!=null){
            throw new MovieHasNotBeenInitializedException("Movie ID has been taken, movie is not setup");
        }
        movieHashMap.put(movie.getId(), movie);
        return true;
    }

    public List<TicketDetails> view(Long id) throws UnableToViewMovieException {
        if(ticketDetailsHashMap.get(id)==null){
            throw new UnableToViewMovieException("Movie has not been book, unable to view Movie");
        }else{
            return ticketDetailsHashMap.get(id).stream()
                    .filter(ticket->ticket.getTicketStatus().equals(TicketStatus.BOOKED))
                    .collect(Collectors.toList());
        }
    }

    public List<String> showAvailability(Long id) throws MovieHasNotBeenInitializedException {

        Movie movie = movieHashMap.get(id);
        if(movie==null){
            throw new MovieHasNotBeenInitializedException("Movie has not yet been setup, Please setup before checking availability");
        }

        return movie.showAvailability();

    }

    public TicketDetails bookSeats(Long id, String phoneNumber,String[] seatNumbers) throws UnableToBookSeatsException, MovieHasNotBeenInitializedException {
        Movie movie = movieHashMap.get(id);
        List<TicketDetails> ticketDetails = ticketDetailsHashMap.get(id);
        Optional<TicketDetails> ticketDetailsList = null;

        if(ticketDetails != null){
            ticketDetailsList = ticketDetails.stream()
                    .filter(x->{
                        return x.getPhoneNumber().equals(phoneNumber) && x.getTicketStatus().equals(TicketStatus.BOOKED);
                    }).findAny();
        }

        if (movie == null) {throw new MovieHasNotBeenInitializedException("Movie has not yet been setup, Please setup before booking");}
            List<String> ticketNumbers = new ArrayList<>();

            if(movie.checkAvailability(seatNumbers) && (ticketDetailsList == null || !ticketDetailsList.isPresent())){

                movie.bookSeats(seatNumbers);
                return generateTickets(id, phoneNumber, seatNumbers);
            }else{
                throw new UnableToBookSeatsException("Your phone number has booked for the mobile/A Booking that you have booked might not be available anymore");
            }

    }

    public Boolean cancelSeat(String ticketNo, String phoneNumber) throws UnableToCancelBooking {
        Collection<List<TicketDetails>> collections = ticketDetailsHashMap.values();
        Optional <TicketDetails> ticketFound = collections.stream().flatMap(x->x.stream()
                .filter(ticket-> {
                    return (ticket.getTicketNumber().equals(ticketNo) &&
                            ticket.getPhoneNumber().equals(phoneNumber) &&
                        ticket.getTicketStatus().equals(TicketStatus.BOOKED));
                }))
                .findAny();
        if(ticketFound.isPresent()){
            TicketDetails ticketDetails = ticketFound.get();
            Movie movie = movieHashMap.get(ticketDetails.getMovieId());
            if(checkCancellationWindow(ticketDetails, movieHashMap.get(ticketDetails.getMovieId()).getCancellationWindow())){
                ticketDetails.setTicketStatus(TicketStatus.CANCELLED);
                return movieHashMap.get(ticketFound.get().getMovieId()).cancelSeat(ticketDetails.getSeatNumber());
            }else{
                throw new UnableToCancelBooking(String.format("Unable to cancel booking as %S mins cancellation window has past!",movie.getCancellationWindow()));
            }
        }else{
            throw new UnableToCancelBooking("Unable to find booking for cancellation");
        }

    }

    private Boolean checkCancellationWindow(TicketDetails ticketDetails, Integer CancellationWindow){
        return ticketDetails.getBookingTime().plusMinutes(CancellationWindow).isAfter(LocalDateTime.now());
    }


    private TicketDetails generateTickets(Long id, String phoneNumber,String[] seatNumber){
        TicketDetails ticketDetails1 = new TicketDetails(LocalDateTime.now(), id, phoneNumber, Arrays.asList(seatNumber), TicketStatus.BOOKED);
        if(ticketDetailsHashMap.get(id) == null) {
            List<TicketDetails> ticketDetails = new ArrayList<>();
            ticketDetailsHashMap.put(id, ticketDetails);
        }
        ticketDetailsHashMap.get(id).add(ticketDetails1);

        return ticketDetails1;
    }



    private String convertAlpha(Integer num){
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        if (num > 25) {
            return null;
        }
        return Character.toString(alphabet[num-1]);
    }



}
