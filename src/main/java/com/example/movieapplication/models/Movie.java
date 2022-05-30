package com.example.movieapplication.models;

import com.example.movieapplication.exceptions.UnableToBookSeatsException;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
public class Movie {
    private Long id;
    private Integer numOfRow;
    private Integer numOfColumn;
    private Integer cancellationWindow;

    @Getter(value=AccessLevel.PRIVATE)
    @Setter(value= AccessLevel.PRIVATE)
    private List<String> seatsAvailable = new ArrayList<>();

    @Getter(value=AccessLevel.PRIVATE)
    @Setter(value= AccessLevel.PRIVATE)
    private List<String> soldSeats = new ArrayList<>();

    public Movie(Long id, Integer numOfRow, Integer numOfColumn, Integer cancellationWindow) {
        this.id = id;
        this.numOfRow = numOfRow;
        this.numOfColumn = numOfColumn;
        this.cancellationWindow = cancellationWindow;


        Stream<String> rowStream = IntStream.rangeClosed(1,numOfRow)
                .mapToObj(x-> convertAlpha(x));

        List<String> availability = rowStream.flatMap(row->
                IntStream.rangeClosed(1, numOfColumn)
                        .mapToObj(x-> String.valueOf(x))
                        .map(col-> row+col))
                .collect(Collectors.toList());

        this.seatsAvailable = availability;
    }

    public Boolean checkAvailability(String[] seatNumbers){
        for (String seatNumber:seatNumbers) {
            if(!seatsAvailable.contains(seatNumber)){
                break;
            }
            return true;
        }
        return false;
    }

    public List<String> showAvailability(){
        return seatsAvailable;
    }

    public Boolean bookSeats(String[] seatNumbers) throws UnableToBookSeatsException {
        for (String seatNumber:seatNumbers) {

            Optional<String> foundAvailable = seatsAvailable.stream().filter(x->x.equals(seatNumber)).findAny();
            if(foundAvailable.isPresent()){
                seatsAvailable.removeIf(x -> x.equals(seatNumber));
                soldSeats.add(seatNumber);
            }else{
                throw new UnableToBookSeatsException("A Booking that you have booked might not be available anymore, Please check!");
            }
        }
        return true;
    }

    public Boolean cancelSeat(List<String> seatNumbers){
        for (String seatNumber: seatNumbers) {
            soldSeats.removeIf(x->x.equals(seatNumber));
            seatsAvailable.add(seatNumber);
        }
        seatsAvailable = seatsAvailable.stream().sorted().collect(Collectors.toList());

        return true;
    }




    private String convertAlpha(Integer num){
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        if (num > 25) {
            return null;
        }
        return Character.toString(alphabet[num-1]);
    }
}
