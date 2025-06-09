package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static com.pluralsight.DataManager.sqlConnection;

public class App {

    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        DataManager.initConnection();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nCiao!");
                scanner.close();
                DataManager.close();
        }));

        while (true){
            System.out.print("""
                    1) Search Actors
                    2) Search Movies By Actor ID
                    0) Exit
                    Enter Selection:\s""");
            switch (scanner.nextLine()){
                case "1" -> searchActor();
                case "2" -> searchMovie();
                case "0" -> System.exit(0);
            }
        }
    }
    private static void searchActor(){
        System.out.print("Enter First Name (Empty to Skip): ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name (Empty to Skip): ");
        String lastName = scanner.nextLine().trim();
        List<Actor> actors = DataManager.getActors(firstName, lastName);

        if (actors != null) actors.forEach(System.out::println); else System.out.println("No Matches Found!");
    }
    private static void searchMovie(){
        while(true){
            System.out.print("Enter Actor ID: ");
            try{
                List<Film> films = DataManager.getFilmsByActorID(scanner.nextInt());
                if(films != null) films.forEach(System.out::println); else System.out.println("No Matches Found!");
                scanner.nextLine();
                return;
            }catch (Exception ignored){
                scanner.nextLine();
            }
        }
    }
}
