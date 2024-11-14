# Falcon Airlines Booking System

This project is a simple airline booking system implemented in Java, using a MySQL database for data management. It allows users to book flights, view reservations, cancel bookings, and display all available data.

## Table of Contents
- [Installation](#installation)
- [Dependencies](#dependencies)
- [Setup](#setup)
- [Usage](#usage)
- [Database Tables](#database-tables)
- [Notes](#notes)

---

## Installation

### 1. Clone the Repository
Download or clone the project repository to your local machine:
```bash
git clone <repository-url>
```

### 2. Configure Your IDE
Ensure your Java IDE (such as IntelliJ, Eclipse, or VSCode) is configured to handle Java projects and can connect to a MySQL database.

---

## Dependencies

Ensure you have the following installed on your system:
- **Java Development Kit (JDK)** (Version 8 or higher)
- **MySQL Database**
- **JDBC Connector** for MySQL

### Required Libraries
Download the JDBC Connector for MySQL and add it to your project's classpath:
- `mysql-connector-java-<version>.jar`

---

## Setup

1. **Configure the Database:**
   - Use MySQL to create a database named `FalconAirlineDB`.
   - Ensure your database credentials and URL are configured correctly in the code:
     ```java
     private static final String DB_URL = "jdbc:mysql://<your-database-url>:<port>/<database-name>";
     private static final String USER = "<username>";
     private static final String PASSWORD = "<password>";
     ```

2. **Create Tables (Optional):**
   - Uncomment the `create_table()` call in the `main` method if you need to create tables.
   - Run the program once to create the necessary database tables.

---

## Usage

1. **Run the Program:**
   - Execute the `AirlineBookingSystem` class to start the program.
   - Use the menu to interact with the system:
     - `1`: Book a flight
     - `2`: View a reservation
     - `3`: Cancel a reservation
     - `4`: Display all data
     - `5`: Exit

2. **Database Interactions:**
   - The program will connect to your MySQL database and perform CRUD operations for flight bookings and passenger information.

---

## Database Tables

### 1. `Airport`
- **AirportID**: INT (Primary Key)
- **Name**: VARCHAR(100)
- **Location**: VARCHAR(100)

### 2. `FlightSchedule`
- **FlightID**: INT (Primary Key)
- **AirportID**: INT (Foreign Key to `Airport`)
- **DepartureTime**: DATETIME
- **ArrivalTime**: DATETIME
- **Destination**: VARCHAR(100)

### 3. `Passenger`
- **PassengerID**: INT (Primary Key)
- **Name**: VARCHAR(100)
- **Email**: VARCHAR(100) (Unique)

### 4. `Booking`
- **BookingID**: INT (Primary Key)
- **PassengerID**: INT (Foreign Key to `Passenger`)
- **FlightID**: INT (Foreign Key to `FlightSchedule`)
- **BookingDate**: DATETIME (Default to current timestamp)

---

## Notes
- Ensure your MySQL server is running and accessible.
- You may need to adjust the database URL and port to match your setup.
- Handle database credentials securely in a real-world application.