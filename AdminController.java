import java.sql.*;
import java.util.Scanner;

public class AdminController {
    public static void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add Bus");
            System.out.println("2. View Buses");
            System.out.println("3. View All Bookings");
            System.out.println("4. Logout");
            System.out.print("Select option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addBus(sc);
                case 2 -> viewBuses();
                case 3 -> viewBookings();
                case 4 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addBus(Scanner sc) {
        System.out.print("Bus Name: ");
        String name = sc.next();
        System.out.print("Source: ");
        String src = sc.next();
        System.out.print("Destination: ");
        String dest = sc.next();
        System.out.print("Total Seats: ");
        int seats = sc.nextInt();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO buses(bus_name, source, destination, total_seats, available_seats) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, src);
            ps.setString(3, dest);
            ps.setInt(4, seats);
            ps.setInt(5, seats);
            ps.executeUpdate();
            System.out.println("Bus Added Successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void viewBuses() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM buses");
            System.out.println("\nAvailable Buses:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("bus_id") +
                        ", Name: " + rs.getString("bus_name") +
                        ", From: " + rs.getString("source") +
                        ", To: " + rs.getString("destination") +
                        ", Available Seats: " + rs.getInt("available_seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void viewBookings() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT b.booking_id, u.username, bs.bus_name, b.seats_booked, b.booking_time FROM bookings b JOIN users u ON b.user_id=u.id JOIN buses bs ON b.bus_id=bs.bus_id");
            System.out.println("\nAll Bookings:");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id") +
                        ", User: " + rs.getString("username") +
                        ", Bus: " + rs.getString("bus_name") +
                        ", Seats: " + rs.getInt("seats_booked") +
                        ", Time: " + rs.getTimestamp("booking_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
