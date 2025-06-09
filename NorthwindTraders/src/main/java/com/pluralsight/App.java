package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    static Connection sqlConnection;
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("\nCiao!");
                sqlConnection.close();
            } catch (SQLException ignore) {
            }
        }));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            sqlConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    "root",
                    "mysql");
        } catch (SQLException e) {
            System.out.println("Connection to MySQL Could Not Be Established!");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Failed!");
            System.exit(1);
        }
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("""
                    
                    
                    
                    What do you want to do?
                        1) Display all products
                        2) Display all customers
                        0) Exit
                    Select an option:\s""");
            switch(scanner.nextLine()){
                case "1" -> products();
                case "2" -> customers();
                case "0" -> System.exit(0);
                default -> System.out.println("Invalid Selection!!!");
            }
        }
    }
    private static void products(){
        System.out.println("\nQuerying All Products");
        try {
            ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Products").executeQuery();
            while (results.next()) {
                String name = results.getString("ProductName");
                int id = results.getInt("ProductId");
                float price = results.getFloat("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("""
                        Id: %d
                        Name: %s
                        Price: %.2f
                        Stock: %d
                        ------------------------------
                        """, id, name, price, stock);
            }
        } catch (SQLException e) {
            System.out.println("Database Error Occurred!");
            System.exit(1);
        }
        System.out.println("\nDONE!");
    }
    private static void customers(){
        System.out.println("\nQuerying All Customers");
        try {
            ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Customers ORDER BY Country").executeQuery();
            while (results.next()) {
                String contactName = results.getString("ContactName");
                String companyName = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phoneNumber = results.getString("Phone");
                System.out.printf("""
                        Name: %s
                        Company: %s
                        City: %s
                        Country: %s
                        Phone: %s
                        ------------------------------
                        """, contactName, companyName, city, country, phoneNumber);
            }
        } catch (SQLException e) {
            System.out.println("Database Error Occurred!");
            System.exit(1);
        }
        System.out.println("\nDONE!");
    }
}
