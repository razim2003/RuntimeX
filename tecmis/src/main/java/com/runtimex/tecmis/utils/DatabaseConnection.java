package com.runtimex.tecmis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/tecmis_java";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Connection connection;
    private DatabaseConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database successfully");
            }catch (SQLException e){
                System.err.println("failed to connect to database "+e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed successfully");
                } catch (SQLException e) {
                    System.err.println("failed to close connection to database "+e.getMessage());
                }
            }
    }

}
