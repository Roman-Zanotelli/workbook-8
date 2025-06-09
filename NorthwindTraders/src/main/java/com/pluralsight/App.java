package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class App {
    static Connection sqlConnection;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        try(BasicDataSource source = new BasicDataSource()){
            Class.forName("com.mysql.cj.jdbc.Driver");
            source.setUrl("jdbc:mysql://localhost:3306/northwind");
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


        while(true){
            System.out.print("""
                    
                    
                    
                    What do you want to do?
                        1) Display all products
                        2) Display all customers
                        3) Display all categories
                        0) Exit
                    Select an option:\s""");
            switch(scanner.nextLine()){
                case "1" -> products();
                case "2" -> customers();
                case "3" -> categories();
                case "0" -> System.exit(0);
                default -> System.out.println("Invalid Selection!!!");
            }
        }
    }
    private static void products(){
        System.out.println("\nQuerying All Products");
        try (ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Products").executeQuery()){

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
        try(ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Customers ORDER BY Country").executeQuery()) {

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

    private static void categories(){
        System.out.println("\nQuerying All Categories");
        try(ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Categories ORDER BY CategoryID").executeQuery()) {

            while (results.next()) {
                int id = results.getInt("CategoryID");
                String name = results.getString("CategoryName");
                System.out.printf("""
                        ID: %d
                        Name: %s
                        ------------------------------
                        """, id, name);
            }

        } catch (SQLException e) {
            System.out.println("Database Error Occurred!");
            System.exit(1);
        }
        productsByCategory();
    }

    private static void productsByCategory(){
        System.out.println("\nQuerying All Products By Category");
        int selectedID;
        while (true){
            System.out.print("Select Category ID: ");
            try{
                selectedID = scanner.nextInt();
                scanner.nextLine();
                break;
            }catch (Exception ignore){
                scanner.nextLine();
            }
        }
        System.out.print("\n".repeat(3));
        PreparedStatement query;
        try {
            query = sqlConnection.prepareStatement("SELECT * FROM Products WHERE CategoryID = ?");
            query.setInt(1, selectedID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (ResultSet results = query.executeQuery()){

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
}
