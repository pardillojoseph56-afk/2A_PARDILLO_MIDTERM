package com.airport;

import com.airport.dao.DatabaseConnection;
import com.airport.dao.ScenarioDao;
import com.airport.ui.ScenarioMenu;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        ScenarioMenu menu = new ScenarioMenu(new ScenarioDao());
        menu.start();
    }
}
