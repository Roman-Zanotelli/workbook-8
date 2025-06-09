package com.pluralsight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        Connection sqlConnection = null;
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
            throw new RuntimeException(e);
        }
        try {
            ResultSet results = sqlConnection.prepareStatement("SELECT * FROM Products").executeQuery();
            while (results.next()) {
                String name = results.getString("ProductName");
                System.out.println(name);
            }
            sqlConnection.close();
        } catch (SQLException e) {
            System.out.println("Database Error Occurred!");
            System.exit(1);
        }


    }
}
