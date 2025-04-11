import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n==== BUS RESERVATION SYSTEM ====");
            System.out.println("1. Admin Login");
            System.out.println("2. Passenger Login");
            System.out.println("3. Register");
            System.out.println("4. Exit");
            System.out.print("Select option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> UserController.adminLogin(sc);
                case 2 -> UserController.passengerLogin(sc);
                case 3 -> UserController.register(sc);
                case 4 -> {
                    System.out.println("Thank you!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
