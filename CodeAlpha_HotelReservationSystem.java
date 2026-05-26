import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Room {

    int roomNumber;
    String category;
    double price;
    boolean isBooked;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }
}

class Booking {

    String customerName;
    int roomNumber;
    String category;
    double amountPaid;

    Booking(String customerName, int roomNumber, String category, double amountPaid) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amountPaid = amountPaid;
    }
}

public class CodeAlpha_HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<Room>();
    static ArrayList<Booking> bookings = new ArrayList<Booking>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Add Rooms
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 4000));
        rooms.add(new Room(202, "Deluxe", 4000));
        rooms.add(new Room(301, "Suite", 7000));

        loadBookings();

        int choice = 0;

        while (choice != 5) {

            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    viewAvailableRooms();
                    break;

                case 2:
                    bookRoom();
                    break;

                case 3:
                    cancelReservation();
                    break;

                case 4:
                    viewBookings();
                    break;

                case 5:
                    saveBookings();
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // View Available Rooms
    public static void viewAvailableRooms() {

        System.out.println("\nAvailable Rooms:");

        boolean available = false;

        for (Room room : rooms) {

            if (!room.isBooked) {

                System.out.println(
                        "Room No: " + room.roomNumber +
                        " | Category: " + room.category +
                        " | Price: Rs." + room.price
                );

                available = true;
            }
        }

        if (!available) {
            System.out.println("No Rooms Available!");
        }
    }

    // Book Room
    public static void bookRoom() {

        sc.nextLine();

        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Room Category (Standard/Deluxe/Suite): ");
        String category = sc.nextLine();

        boolean found = false;

        for (Room room : rooms) {

            if (!room.isBooked &&
                room.category.equalsIgnoreCase(category)) {

                System.out.println("\nRoom Available!");
                System.out.println("Room Number: " + room.roomNumber);
                System.out.println("Price: Rs." + room.price);

                System.out.print("Proceed Payment? (yes/no): ");
                String payment = sc.nextLine();

                if (payment.equalsIgnoreCase("yes")) {

                    room.isBooked = true;

                    Booking booking = new Booking(
                            name,
                            room.roomNumber,
                            room.category,
                            room.price
                    );

                    bookings.add(booking);

                    saveBookings();

                    System.out.println("\nPayment Successful!");
                    System.out.println("Room Booked Successfully!");

                } else {

                    System.out.println("Payment Cancelled!");
                }

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No Rooms Available in this Category!");
        }
    }

    // Cancel Reservation
    public static void cancelReservation() {

        System.out.print("Enter Room Number to Cancel: ");
        int roomNo = sc.nextInt();

        boolean found = false;

        for (int i = 0; i < bookings.size(); i++) {

            Booking booking = bookings.get(i);

            if (booking.roomNumber == roomNo) {

                bookings.remove(i);

                for (Room room : rooms) {

                    if (room.roomNumber == roomNo) {
                        room.isBooked = false;
                    }
                }

                saveBookings();

                System.out.println("Reservation Cancelled Successfully!");

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Booking Not Found!");
        }
    }

    // View Bookings
    public static void viewBookings() {

        if (bookings.size() == 0) {

            System.out.println("No Bookings Found!");
            return;
        }

        System.out.println("\n===== BOOKING DETAILS =====");

        for (Booking booking : bookings) {

            System.out.println("----------------------------");
            System.out.println("Customer Name : " + booking.customerName);
            System.out.println("Room Number   : " + booking.roomNumber);
            System.out.println("Category      : " + booking.category);
            System.out.println("Amount Paid   : Rs." + booking.amountPaid);
        }
    }

    // Save Bookings to File
    public static void saveBookings() {

        try {

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter("bookings.txt")
                    );

            for (Booking booking : bookings) {

                writer.write(
                        booking.customerName + "," +
                        booking.roomNumber + "," +
                        booking.category + "," +
                        booking.amountPaid
                );

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("Error Saving File!");
        }
    }

    // Load Bookings from File
    public static void loadBookings() {

        try {

            File file = new File("bookings.txt");

            if (!file.exists()) {
                return;
            }

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                String name = data[0];
                int roomNo = Integer.parseInt(data[1]);
                String category = data[2];
                double amount = Double.parseDouble(data[3]);

                bookings.add(
                        new Booking(name, roomNo, category, amount)
                );

                for (Room room : rooms) {

                    if (room.roomNumber == roomNo) {
                        room.isBooked = true;
                    }
                }
            }

            reader.close();

        } catch (Exception e) {

            System.out.println("No Previous Booking Data Found.");
        }
    }
}