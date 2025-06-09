package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static Connection sqlConnection;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        try(BasicDataSource source = new BasicDataSource()){
            Class.forName("com.mysql.cj.jdbc.Driver");
            source.setUrl("jdbc:mysql://localhost:3306/sakila");
            source.setUsername("root");
            source.setPassword("mysql");
            sqlConnection = source.getConnection();
        }catch (SQLException e) {
            System.out.println("Connection to MySQL Could Not Be Established!");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Failed!");
            System.exit(1);
        } catch (Exception ignored){

        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("\nCiao!");
                scanner.close();
                sqlConnection.close();
            } catch (SQLException ignore) {
            }
        }));
        System.out.print("Enter Last Name: ");
        String last_name = scanner.nextLine().trim();

        try(ResultSet results = sqlConnection.prepareStatement(String.format("SELECT * FROM actor WHERE last_name = '%s'", last_name)).executeQuery()){
            if(!results.next()) {System.out.println("No Matches Found!"); System.exit(0);} else while(results.next()){
                System.out.printf("""
                                Actor ID: %d
                                First Name: %s
                                Last Name: %s
                                -------------------------
                                """,
                        results.getInt("actor_id"),
                        results.getString("first_name"),
                        results.getString("last_name"));
            }
        }catch (Exception ignores){}

        System.out.print("Enter First Name: ");
        String first_name = scanner.nextLine().trim();
        System.out.print("\n".repeat(3));
        search(first_name, last_name);
    }
    private static void search(String first_name, String last_name){
        System.out.printf("Querying All Movies By %s, %s\n", first_name, last_name);
        try(ResultSet results = sqlConnection.prepareStatement(String.format("SELECT * FROM film WHERE film_id IN (SELECT film_id FROM film_actor WHERE actor_id = (SELECT actor_id FROM actor WHERE first_name = '%s' AND last_name = '%s'))",first_name, last_name)).executeQuery()){
            if(!results.next()) {System.out.println("No Matches Found!"); System.exit(0);} else while(results.next()){
                System.out.printf("""
                        Title: %s
                        Year: %d
                        -------------------------
                        """, results.getString("title"), results.getInt("release_year"));
            }
        } catch (Exception ignored) {

        }
    }
}
