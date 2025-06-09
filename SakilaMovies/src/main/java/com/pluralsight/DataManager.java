package com.pluralsight;


import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    static Connection sqlConnection;

    public static void initConnection(){
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
    }
    public static void close(){
        try {
            sqlConnection.close();
        } catch (SQLException ignored) {
        }
    }

    public static List<Actor> getActors(String firstName, String lastName){
        boolean firstExists = !firstName.isBlank();
        boolean lastExists = !lastName.isBlank();
        String query = String.format(
                "SELECT * FROM actor%s%s",
                firstExists ? String.format(" WHERE first_name = '%s'", firstName) : "",
                lastExists ? String.format(" %s last_name = '%s'", firstExists ? "AND" : "WHERE", lastName) : ""
        );
        System.out.println(query);
        try(ResultSet res = sqlConnection.prepareStatement(query).executeQuery()){
            List<Actor> actors = new ArrayList<>();
            if(!res.next()) return null; else do{
                actors.add(new Actor(res.getString("first_name"), res.getString("last_name"), res.getInt("actor_id")));
            }while(res.next());
            return actors;
        } catch (SQLException ignored) {
            System.out.println("Actors Query Failed! Shutting Down!");
        }

        System.exit(1);
        return null;
    }
    public static List<Film> getFilmsByActorID(int actorID){
        try(ResultSet res = sqlConnection.prepareStatement(String.format("SELECT * FROM film WHERE film_id IN(SELECT film_id FROM film_actor WHERE actor_id = '%d')", actorID)).executeQuery()){
            List<Film> films = new ArrayList<>();
            if(!res.next()) return null; else do{
                films.add(new Film(res.getInt("film_id"), res.getInt("release_year"), res.getInt("length"), res.getString("title"), res.getString("description")));
            }while(res.next());
            return films;
        } catch (Exception e) {
            System.out.println("Film Query Failed! Shutting Down!");
        }
        System.exit(1);
        return null;
    }
}
