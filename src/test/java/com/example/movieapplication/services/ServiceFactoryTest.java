package com.example.movieapplication.services;

import com.example.movieapplication.exceptions.IllegalCommandException;
import com.example.movieapplication.models.CommandSetup;
import com.example.movieapplication.models.TicketDetails;
import com.example.movieapplication.models.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ServiceFactoryTest {

    @Mock
    private MovieManagementService movieManagementService;

    @InjectMocks
    private ServiceFactory serviceFactory;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void runCommandSetUp() throws Exception {
        String[] instructions = new String[]{CommandSetup.SETUP.toString(),"1", "10", "10", "3"};
        Mockito.when(movieManagementService.setupMovie(any())).thenReturn(true);
        assertEquals(serviceFactory.runCommand(instructions),"Your movie has been successfully created");
    }

    @Test
    void runCommandSetUpInvalidCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.SETUP.toString(), "dasd", "10", "10", "3"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
            assertEquals(exception.getMessage(),"You have entered an Invalid Parameter, Please enter a valid Command");
    }

    @Test
    void runCommandSetUpLessThanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.SETUP.toString(), "dasd", "10", "3"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals(exception.getMessage(),"Booking only accepts a total of 5 arguements");
    }

    @Test
    void runCommandMoreThanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.SETUP.toString(), "dasd", "10", "3","123","123"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals(exception.getMessage(), "Booking only accepts a total of 5 arguements");
    }
    @Test
    void runCommandSetUpNullCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.SETUP.toString(), null, "10", "10", "3"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals(exception.getMessage(), "You have entered an Invalid Parameter, Please enter a valid Command");
    }

    @Test
    void runCommandView() throws Exception {
        String[] instructions = new String[]{CommandSetup.VIEW.toString(),"1"};
        List<TicketDetails> ticketDetails = generateTicketDetails();
        Mockito.when(movieManagementService.view(any())).thenReturn(ticketDetails);
        assertEquals(ticketDetails.toString(), serviceFactory.runCommand(instructions));
    }

    @Test
    void runCommandViewWithInvalidParameters() throws Exception {
        String[] instructions = new String[]{CommandSetup.VIEW.toString(),"dsd"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("You have entered an Invalid Parameter, Please enter a valid Command", exception.getMessage());
    }

    @Test
    void runCommandViewWithMorethanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.VIEW.toString(),"dsd", "123"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("View only accepts a total of 2 arguements", exception.getMessage());
    }

    @Test
    void runCommandAvailability() throws Exception {
        String[] instructions = new String[]{CommandSetup.AVAILABILITY.toString(),"1"};
        List<String> availability = new ArrayList<>(Arrays.asList("A1","A2"));
        Mockito.when(movieManagementService.showAvailability(any())).thenReturn(availability);
        assertEquals(availability.toString(), serviceFactory.runCommand(instructions));
    }

    @Test
    void runCommandAvailabilityInvalidParameters(){
        String[] instructions = new String[]{CommandSetup.AVAILABILITY.toString(),"asd"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals( "You have entered an Invalid Parameter, Please enter a valid Command",exception.getMessage());
    }

    @Test
    void runCommandAvailabilityWithMorethanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.AVAILABILITY.toString(),"123","123"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("Availability only accepts a total of 2 arguements",exception.getMessage());
    }

    @Test
    void runCommandBook() throws Exception {
        String[] instructions = new String[]{CommandSetup.BOOK.toString(),"3","2333","A1,A4"};
        TicketDetails ticketDetail = generateTicketDetail();
        Mockito.when(movieManagementService.bookSeats(any(),any(),any())).thenReturn(ticketDetail);
        assertEquals(ticketDetail.getTicketNumber(), serviceFactory.runCommand(instructions));
    }

    @Test
    void runCommandBookWithMorethanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.BOOK.toString(),"123","123","123","123"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("Book only accepts a total of 4 arguements", exception.getMessage() );
    }

    @Test
    void runCommandBookWithInvalidParameters() throws Exception {
        String[] instructions = new String[]{CommandSetup.BOOK.toString(),"asd","1234","1234"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("You have entered an Invalid Parameter, Please enter a valid Command", exception.getMessage() );
    }

    @Test
    void runCommandBookWithNullParameters() throws Exception {
        String[] instructions = new String[]{CommandSetup.BOOK.toString(),"asd",null,"1234"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("You have entered an Invalid Parameter, Please enter a valid Command", exception.getMessage() );
    }

    @Test
    void runCommandCancellation() throws Exception {
        String[] instructions = new String[]{CommandSetup.CANCEL.toString(),"3","1234"};

        Mockito.when(movieManagementService.cancelSeat(any(), any())).thenReturn(true);
        assertEquals("Your ticket has been successfully cancelled", serviceFactory.runCommand(instructions));
    }

    @Test
    void runCommandCancellationWithNullParameters() throws Exception {
        String[] instructions = new String[]{CommandSetup.CANCEL.toString(),"3",null};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("You have entered an Invalid Parameter, Please enter a valid Command", exception.getMessage() );
    }

    @Test
    void runCommandCancellationWithMorethanRequiredCommand() throws Exception {
        String[] instructions = new String[]{CommandSetup.CANCEL.toString(),"123","123","123","123"};
        Exception exception = assertThrows(IllegalCommandException.class, () -> {
            serviceFactory.runCommand(instructions);
        });
        assertEquals("Cancel only accepts a total of 3 arguements", exception.getMessage() );
    }


    List<TicketDetails> generateTicketDetails(){
        List<TicketDetails> ticketDetails = new ArrayList<>();
        ticketDetails.add(new TicketDetails(LocalDateTime.now(),
                1L,
                "1234",
                Arrays.asList(new String[]{"A1","A2"}), TicketStatus.BOOKED));
        return  ticketDetails;
    }
    TicketDetails generateTicketDetail(){
        return new TicketDetails(LocalDateTime.now(),
                1L,
                "1234",
                Arrays.asList(new String[]{"A1","A2"}), TicketStatus.BOOKED);
    }

}