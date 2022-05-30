package com.example.movieapplication.services;

import com.example.movieapplication.exceptions.IllegalCommandException;
import com.example.movieapplication.models.CommandSetup;
import com.example.movieapplication.models.Movie;
import com.example.movieapplication.models.TicketDetails;
import org.springframework.stereotype.Component;

@Component
public class ServiceFactory {

    private MovieManagementService movieManagementService;

    public ServiceFactory(MovieManagementService movieManagementService) {
        this.movieManagementService = movieManagementService;
    }

    public String runCommand(String[] arg) throws Exception{
        String result = null;
        CommandSetup setup = null;
        try {
            setup = CommandSetup.valueOf(arg[0].toUpperCase());
        } catch (IllegalArgumentException ex){
            throw new IllegalCommandException("You have entered an Invalid Command, Please enter a valid Command");
        }
        try {

            if(setup != null && arg.length>1){
                    switch (setup) {
                        case SETUP:
                            if(arg.length!= 5){ throw  new IllegalCommandException("Booking only accepts a total of 5 arguements");}
                            Integer row = Integer.parseInt(arg[2]);
                            Integer col = Integer.parseInt(arg[3]);

                            if(row>10 || col>26){ throw new IllegalCommandException("Max seats per row is 10 and max rows are 26 ");}
                            Movie movie = new Movie(Long.parseLong(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]), Integer.parseInt(arg[4]));
                            if (movieManagementService.setupMovie(movie)) {
                                result = "Your movie has been successfully created";
                            } else {
                                result = "Your movie has not been setup";
                            }
                            break;
                        case VIEW:
                            if(arg.length!= 2){ throw  new IllegalCommandException("View only accepts a total of 2 arguements");}
                            result = movieManagementService.view(Long.parseLong(arg[1])).toString();
                            break;
                        case AVAILABILITY:
                            if(arg.length!= 2){ throw  new IllegalCommandException("Availability only accepts a total of 2 arguements");}
                            result = movieManagementService.showAvailability(Long.parseLong(arg[1])).toString();
                            break;
                        case BOOK:
                            if(arg.length!= 4){ throw  new IllegalCommandException("Book only accepts a total of 4 arguements");}
                            if(arg[2]==null ||
                                    arg[2].trim().equals("") ||
                                    arg[3]==null ||
                                    arg[3].trim().equals("")) {throw new IllegalCommandException("You have entered an Invalid Parameter, Please enter a valid Command");}
                            TicketDetails ticketDetails = movieManagementService.bookSeats(Long.parseLong(arg[1]), arg[2], arg[3].split(","));
                            result = ticketDetails.getTicketNumber();
                            break;
                        case CANCEL:
                            if(arg.length!= 3){ throw  new IllegalCommandException("Cancel only accepts a total of 3 arguements");}
                            if(arg[1]==null ||
                                    arg[1].trim().equals("") ||
                                    arg[2]==null ||
                                    arg[2].trim().equals("")) {throw new IllegalCommandException("You have entered an Invalid Parameter, Please enter a valid Command");}
                            if (movieManagementService.cancelSeat(arg[1], arg[2])) {
                                result = "Your ticket has been successfully cancelled";
                            } else {
                                result = "Your ticket has not been cancelled";
                            }
                            break;
                        default:
                            throw new IllegalCommandException("You have entered an Invalid Command, Please enter a valid Command");
                    }
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalCommandException("You have entered an Invalid Parameter, Please enter a valid Command");
        }
        return result;
    }
}
