package com.airport.ui;

import com.airport.dao.ScenarioDao;
import com.airport.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private static final String LINE = "=".repeat(52);
    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ScenarioMenu(ScenarioDao dao) {
        this.dao = dao;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addRecord();
                case "2" -> viewRecords();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n" + LINE);
        System.out.println("            AIRPORT LUGGAGE CLAIM REGISTRY");
        System.out.println(LINE);
        System.out.println("[1] Check-In Luggage");
        System.out.println("[2] View Checked Luggage");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
        System.out.print("Enter choice: ");
    }

    private void addRecord() {
        System.out.println("\n--- CHECK-IN LUGGAGE ---");

        String name = readNonEmpty("Enter Passenger Name: ");
        String flight = readNonEmpty("Enter Flight Number: ");
        double weight = readWeight("Enter Luggage Weight (KG): ");
        String status = readStatus("Enter Status (CHECKED / LOADED / CLAIMED): ");

        ScenarioEntity entity = new ScenarioEntity(0, name, flight, weight, status);
        if (dao.insert(entity)) {
            System.out.println("\nSUCCESS: Baggage successfully checked in!");
        } else {
            System.out.println("\nERROR: Could not save the record.");
        }
    }

    private void viewRecords() {
        List<ScenarioEntity> records = dao.findAll();

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(22) + "+"
                + "-".repeat(12) + "+" + "-".repeat(12) + "+" + "-".repeat(12) + "+";
        String rowFormat = "| %-4s | %-20s | %-10s | %-10s | %-10s |%n";

        System.out.println("\n" + "=".repeat(border.length()));
        System.out.printf("%" + ((border.length() + 20) / 2) + "s%n", "CHECKED LUGGAGE LOGS");
        System.out.println("=".repeat(border.length()));
        System.out.println(border);
        System.out.printf(rowFormat, "ID", "PASSENGER NAME", "FLIGHT NO", "WEIGHT_KG", "STATUS");
        System.out.println(border);

        for (ScenarioEntity e : records) {
            System.out.printf(rowFormat, e.id(), e.passengerName(), e.flightNo(),
                    e.weightKg(), e.status());
        }

        System.out.println(border);
        System.out.println("Total Records Displayed: " + records.size());
    }

    // ---------- Input helpers (validation only, no SQL) ----------

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private double readWeight(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double w = Double.parseDouble(scanner.nextLine().trim());
                if (w > 0) {
                    return w;
                }
                System.out.println("Weight must be greater than 0.");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private String readStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim().toUpperCase();
            if (s.equals("CHECKED") || s.equals("LOADED") || s.equals("CLAIMED")) {
                return s;
            }
            System.out.println("Status must be CHECKED, LOADED, or CLAIMED.");
        }
    }
}
