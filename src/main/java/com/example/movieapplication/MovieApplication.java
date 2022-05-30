package com.example.movieapplication;

import com.example.movieapplication.services.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class MovieApplication implements CommandLineRunner {

    @Autowired
    ServiceFactory serviceFactory;

    public static void main(String[] args) {

        SpringApplication.run(MovieApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        helperFunction();
        while(scanner.hasNextLine()){
            String input = scanner.nextLine();
            String[] command = input.split(" ");
            if(input.equals("exit")) {break;}
            try{
                System.out.println(serviceFactory.runCommand(command));
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    public void helperFunction(){
        System.out.println("Movie Management System");
        System.out.println("Please follow the following commands available to select your actions:");
        System.out.println("1.Setup  <Show Number> <Number of Rows> <Number of seats per row>  <Cancellation window in minutes>  ");
        System.out.println("Example: setup 3 5 5 3");

        System.out.println("2.View <Show Number>");
        System.out.println("Example: view 3");

        System.out.println("3.Availability  <Show Number>   ");
        System.out.println("Example: availability 3");

        System.out.println("4.Book  <Show Number> <Phone#> <Comma separated list of seats> ");
        System.out.println("Example: book 3 1234 A1,A4");

        System.out.println("4.Cancel  <Ticket#>  <Phone#>");
        System.out.println("Example:Cancel ABCD-ASDA-1232-asdas 9123456778");
    }
}
