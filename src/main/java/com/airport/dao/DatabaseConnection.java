package com.airport.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /** Creates the luggage table on first run. */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS luggage ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "passenger_name TEXT NOT NULL, "
                + "flight_no TEXT NOT NULL, "
                + "weight_kg REAL NOT NULL, "
                + "status TEXT NOT NULL CHECK (status IN ('CHECKED','LOADED','CLAIMED'))"
                + ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }
}
