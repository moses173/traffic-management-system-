/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
    import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Lil Teen
 */
public class PlateNumberGenerator {
public static synchronized String generateNumberPlate() {
        String prefix = "ABC"; // Default prefix
        int number = 0; // Default starting number

        try (Connection connection = DatabaseUtil.getConnection()) {
            if (connection == null) {
                System.err.println("Database connection failed.");
                return null;
            }

            // Ensure the PlateTracker table is initialized and contains at least one row
            initializePlateTracker(connection);

            // Fetch the last used prefix and number
            String query = "SELECT last_prefix, last_number FROM PlateTracker ORDER BY id DESC LIMIT 1";
            try (PreparedStatement pstmt = connection.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    prefix = rs.getString("last_prefix");
                    number = rs.getInt("last_number");
                    System.out.println("Fetched last plate: " + prefix + "-" + number);
                }
            }

            // Increment the number
            number++;
            if (number > 9999) {
                number = 1;
                prefix = incrementPrefix(prefix); // Move to the next prefix
            }

            // Update the database with the new prefix and number
            String updateQuery = "UPDATE PlateTracker SET last_prefix = ?, last_number = ? WHERE id = 1";
            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
                pstmt.setString(1, prefix);
                pstmt.setInt(2, number);
                pstmt.executeUpdate();
                System.out.println("Updated PlateTracker: " + prefix + "-" + number);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error while generating number plate: " + e.getMessage());
        }

        // Return the formatted number plate
        return String.format("%s-%04d", prefix, number);
    }

    // Method to increment the prefix (e.g., from ABC to ABD)
    private static String incrementPrefix(String prefix) {
        char[] chars = prefix.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] < 'Z') {
                chars[i]++;
                break;
            } else {
                chars[i] = 'A';
            }
        }
        return new String(chars);
    }

    // Ensure the PlateTracker table exists and is initialized
    private static void initializePlateTracker(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS PlateTracker ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "last_prefix VARCHAR(3) NOT NULL, "
                + "last_number INT NOT NULL DEFAULT 0);";

        String insertDefaultRowSQL = "INSERT INTO PlateTracker (last_prefix, last_number) "
                + "SELECT 'ABC', 0 WHERE NOT EXISTS (SELECT * FROM PlateTracker);";

        try (PreparedStatement createStmt = connection.prepareStatement(createTableSQL);
             PreparedStatement insertStmt = connection.prepareStatement(insertDefaultRowSQL)) {
            createStmt.executeUpdate(); // Ensure table exists
            insertStmt.executeUpdate(); // Ensure default row exists if empty
        }
    }
}