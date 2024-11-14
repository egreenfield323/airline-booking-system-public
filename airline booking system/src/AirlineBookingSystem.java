import java.sql.*;
import java.util.Scanner;

class AirlineBookingSystem {

    private static final String DB_URL = "jdbc:mysql://<hostname>:3306/<databasename (FalconAirlineDB)>";
    private static final String USER = "<username>";
    private static final String PASSWORD = "<password>";

    public static void main(String[] args) {
        Connection conn = create_connection();
        if (conn == null) {
            System.out.println("Connection failed.");
            return;
        }

        /*
        Uncomment to create database tables
        create_table();
         */
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n--- Falcon Airlines Booking System ---");
            System.out.println("1. Book a Flight");
            System.out.println("2. View Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. See all available bookings, passengers, and flights");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> insert_booking(conn, scanner);
                case 2 -> view_booking(conn, scanner);
                case 3 -> delete_booking(conn, scanner);
                case 4 -> see_all_data(conn);
                case 5 -> {
                    System.out.println("Exiting the system...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Connection create_connection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    private static void insert_booking(Connection conn, Scanner scanner) {
        System.out.print("Enter Passenger ID: ");
        int passengerID = scanner.nextInt();
        System.out.print("Enter Flight ID: ");
        int flightID = scanner.nextInt();

        String insertSQL = "INSERT INTO Booking (PassengerID, FlightID) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, passengerID);
            pstmt.setInt(2, flightID);
            pstmt.executeUpdate();
            System.out.println("Booking created successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting booking: " + e.getMessage());
        }
    }

    private static void view_booking(Connection conn, Scanner scanner) {
        System.out.print("Enter Booking ID to view: ");
        int bookingID = scanner.nextInt();

        String query = "SELECT * FROM Booking WHERE BookingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Booking found: Passenger ID = " + rs.getInt("PassengerID") + ", Flight ID = " + rs.getInt("FlightID"));
            } else {
                System.out.println("No booking found with the provided ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error viewing reservation: " + e.getMessage());
        }
    }

    private static void delete_booking(Connection conn, Scanner scanner) {
        System.out.print("Enter Booking ID to cancel: ");
        int bookingID = scanner.nextInt();

        String deleteSQL = "DELETE FROM Booking WHERE BookingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, bookingID);
            if (pstmt.executeUpdate() > 0) {
                System.out.println("Booking canceled successfully!");
            } else {
                System.out.println("Booking ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error canceling booking: " + e.getMessage());
        }
    }

    private static void see_all_data(Connection conn) {
        System.out.print("\n--- All Available Flights ---");
        System.out.print(getFlightData(conn) + "\n");

        System.out.print("\n--- Current Bookings ---");
        System.out.print(getBookingData(conn) + "\n");

        System.out.print("\n--- All Passengers ---");
        System.out.print(getPassengerData(conn) + "\n");
    }

    private static String getFlightData(Connection conn) {
        StringBuilder data = new StringBuilder("All Available Flights:\n");
        String query = "SELECT * FROM FlightSchedule";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                data.append(String.format("ID: %d | Departure: %s | Arrival: %s | Destination: %s\n",
                        rs.getInt("FlightID"), rs.getString("DepartureTime"), rs.getString("ArrivalTime"), rs.getString("Destination")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching flight data: " + e.getMessage());
        }
        return data.toString();
    }

    private static String getBookingData(Connection conn) {
        StringBuilder data = new StringBuilder("Current Bookings:\n");
        String query = "SELECT * FROM Booking";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                data.append(String.format("Booking ID: %d | Passenger ID: %d | Flight ID: %d\n",
                        rs.getInt("BookingID"), rs.getInt("PassengerID"), rs.getInt("FlightID")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching booking data: " + e.getMessage());
        }
        return data.toString();
    }

    private static String getPassengerData(Connection conn) {
        StringBuilder data = new StringBuilder("All Passengers:\n");
        String query = "SELECT * FROM Passenger";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                data.append(String.format("ID: %d | Name: %s | Email: %s\n",
                        rs.getInt("PassengerID"), rs.getString("Name"), rs.getString("Email")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching passenger data: " + e.getMessage());
        }
        return data.toString();
    }

    public static void create_table(Connection conn) {
        if (conn == null) {
            System.out.println("Could not connect with database");
            return;
        }

        String[] createStatements = {
                "CREATE TABLE Airport ("
                        + "AirportID INT AUTO_INCREMENT PRIMARY KEY,"
                        + "Name VARCHAR(100) NOT NULL,"
                        + "Location VARCHAR(100) NOT NULL);",
                "CREATE TABLE FlightSchedule ("
                        + "FlightID INT AUTO_INCREMENT PRIMARY KEY,"
                        + "AirportID INT,"
                        + "DepartureTime DATETIME NOT NULL,"
                        + "ArrivalTime DATETIME NOT NULL,"
                        + "Destination VARCHAR(100) NOT NULL,"
                        + "FOREIGN KEY (AirportID) REFERENCES Airport(AirportID));",
                "CREATE TABLE Passenger ("
                        + "PassengerID INT AUTO_INCREMENT PRIMARY KEY,"
                        + "Name VARCHAR(100) NOT NULL,"
                        + "Email VARCHAR(100) NOT NULL UNIQUE);",
                "CREATE TABLE Booking ("
                        + "BookingID INT AUTO_INCREMENT PRIMARY KEY,"
                        + "PassengerID INT,"
                        + "FlightID INT,"
                        + "BookingDate DATETIME DEFAULT CURRENT_TIMESTAMP,"
                        + "FOREIGN KEY (PassengerID) REFERENCES Passenger(PassengerID),"
                        + "FOREIGN KEY (FlightID) REFERENCES FlightSchedule(FlightID));"
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : createStatements) {
                stmt.executeUpdate(sql);
            }
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Could not create tables: " + e.getMessage());
        }
    }

}
