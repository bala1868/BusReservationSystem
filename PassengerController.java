import java.sql.*;
import java.util.Scanner;

public class PassengerController {
    public static void passengerMenu(Scanner sc, int userId) {
        while (true) {
            System.out.println("\n=== PASSENGER MENU ===");
            System.out.println("1. View Buses");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Booking");
            System.out.println("4. My Bookings");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> AdminController.viewBuses();
                case 2 -> bookTicket(sc, userId);
                case 3 -> cancelBooking(sc, userId);
                case 4 -> viewMyBookings(userId);
                case 5 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void bookTicket(Scanner sc, int userId) {
        System.out.print("Enter Bus ID: ");
        int busId = sc.nextInt();
        System.out.print("Seats to book: ");
        int seats = sc.nextInt();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT available_seats FROM buses WHERE bus_id=?");
            check.setInt(1, busId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int available = rs.getInt("available_seats");
                if (seats <= available) {
                    PreparedStatement book = con.prepareStatement("INSERT INTO bookings(user_id, bus_id, seats_booked) VALUES (?, ?, ?)");
                    book.setInt(1, userId);
                    book.setInt(2, busId);
                    book.setInt(3, seats);
                    book.executeUpdate();

                    PreparedStatement update = con.prepareStatement("UPDATE buses SET available_seats=? WHERE bus_id=?");
                    update.setInt(1, available - seats);
                    update.setInt(2, busId);
                    update.executeUpdate();

                    System.out.println("Booking successful!");
                } else {
                    System.out.println("Not enough seats available.");
                }
            } else {
                System.out.println("Bus not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cancelBooking(Scanner sc, int userId) {
        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = sc.nextInt();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT bus_id, seats_booked FROM bookings WHERE booking_id=? AND user_id=?");
            check.setInt(1, bookingId);
            check.setInt(2, userId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int busId = rs.getInt("bus_id");
                int seats = rs.getInt("seats_booked");

                PreparedStatement updateSeats = con.prepareStatement("UPDATE buses SET available_seats = available_seats + ? WHERE bus_id=?");
                updateSeats.setInt(1, seats);
                updateSeats.setInt(2, busId);
                updateSeats.executeUpdate();

                PreparedStatement delete = con.prepareStatement("DELETE FROM bookings WHERE booking_id=?");
                delete.setInt(1, bookingId);
                delete.executeUpdate();

                System.out.println("Booking cancelled.");
            } else {
                System.out.println("Booking not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewMyBookings(int userId) {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT b.booking_id, bs.bus_name, b.seats_booked, b.booking_time FROM bookings b JOIN buses bs ON b.bus_id=bs.bus_id WHERE b.user_id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("Your Bookings:");
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id") +
                        ", Bus: " + rs.getString("bus_name") +
                        ", Seats: " + rs.getInt("seats_booked") +
                        ", Time: " + rs.getTimestamp("booking_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
