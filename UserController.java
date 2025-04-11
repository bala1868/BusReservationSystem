import java.sql.*;
import java.util.Scanner;

public class UserController {
    public static void adminLogin(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role='admin'");
            ps.setString(1, uname);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AdminController.adminMenu(sc);
            } else {
                System.out.println("Invalid Admin credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void passengerLogin(Scanner sc) {
        System.out.print("Username: ");
        String uname = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role='passenger'");
            ps.setString(1, uname);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                PassengerController.passengerMenu(sc, userId);
            } else {
                System.out.println("Invalid Passenger credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void register(Scanner sc) {
        System.out.print("Choose Username: ");
        String uname = sc.next();
        System.out.print("Choose Password: ");
        String pass = sc.next();
        String role="passenger";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(username, password, role) VALUES (?, ?, ?)");
            ps.setString(1, uname);
            ps.setString(2, pass);
            ps.setString(3, role);
            ps.executeUpdate();
            System.out.println("Registered Successfully! Please login.");
        } catch (SQLException e) {
            System.out.println("Username already exists.");
        }
    }
}
