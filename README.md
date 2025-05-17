# 🚌 Bus Reservation System - Console Application (Java + MySQL)

This is a simple console-based **Bus Reservation System** developed using **Java** and **MySQL**. It allows users to register, log in, view available buses, book tickets, and view booking history. Admins can add buses, view bookings, and manage the bus database.

---

## 💡 Features

### 👤 User
- Register and Login
- View Available Buses
- Book Bus Tickets
- View My Bookings

### 🔐 Admin
- Login as Admin
- Add, Update, and Delete Buses
- View All Bookings

---

## 🛠️ Technologies Used
- **Java** (Core Java, JDBC)
- **MySQL** (Database)
- **IntelliJ IDEA ** (IDE)
- **Command Line Interface** (Console UI)

---

## 🗂️ Database Schema (MySQL)

```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(100),
    role ENUM('admin', 'user')
);

CREATE TABLE buses (
    bus_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    source VARCHAR(50),
    destination VARCHAR(50),
    date DATE,
    seats_available INT
);

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    bus_id INT,
    seats_booked INT,
    booking_date DATE,
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(bus_id) REFERENCES buses(bus_id)
);
