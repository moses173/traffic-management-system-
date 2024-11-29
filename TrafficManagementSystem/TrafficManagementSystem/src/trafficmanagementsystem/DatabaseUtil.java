/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import java.sql.*;
/**
 *
 * @author Lil Teen
 */
public class DatabaseUtil {


    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/smartlifetech";
    private static final String USER = "LIL_TEEN";
    private static final String PASSWORD = "Admin123";

    // Singleton method to get a persistent connection
    public static Connection getConnection() {
    try {
        if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL driver
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established.");
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.err.println("MySQL JDBC Driver not found.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error establishing database connection.");
    }
    return connection;
}


    // Create the PlateTracker table if it does not exist
    public static void createPlateTrackerTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS PlateTracker ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "last_prefix VARCHAR(3) NOT NULL, "
                + "last_number INT NOT NULL DEFAULT 0);";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(createTableSQL)) {
            pstmt.executeUpdate();
            System.out.println("PlateTracker table checked/created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the connection when the application terminates
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error closing database connection.");
        }
    }

    public static void recordVehicleMovement(String vehicleType, String vehicleModel, String numberPlate, String imagePath) {
        String sql = "INSERT INTO Vehicles (Vehicle_Type, Vehicle_Model, Number_Plate, Image_Path) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vehicleType);
            pstmt.setString(2, vehicleModel);
            pstmt.setString(3, numberPlate);
            pstmt.setString(4, imagePath);
            pstmt.executeUpdate();
            System.out.println("Vehicle movement recorded successfully.");
        } catch (SQLException e) {
            System.err.println("Error recording vehicle movement: " + e.getMessage());
        }
    }
}
