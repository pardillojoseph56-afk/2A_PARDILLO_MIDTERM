package com.airport.dao;

import com.airport.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    private static final String INSERT_SQL =
            "INSERT INTO luggage (passenger_name, flight_no, weight_kg, status) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, passenger_name, flight_no, weight_kg, status FROM luggage ORDER BY id";

    public boolean insert(ScenarioEntity entity) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, entity.passengerName());
            ps.setString(2, entity.flightNo());
            ps.setDouble(3, entity.weightKg());
            ps.setString(4, entity.status());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> findAll() {
        List<ScenarioEntity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("passenger_name"),
                        rs.getString("flight_no"),
                        rs.getDouble("weight_kg"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("Fetch failed: " + e.getMessage());
        }
        return list;
    }
}
