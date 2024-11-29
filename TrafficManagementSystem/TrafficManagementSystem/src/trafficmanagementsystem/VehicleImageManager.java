/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Lil Teen
 */
public class VehicleImageManager {

    private static final String IMAGE_DIRECTORY = "images";

    /**
     * Captures and saves a vehicle image for a given number plate.
     *
     * @param numberPlate the vehicle's number plate
     */
    public void captureAndSaveVehicleImage(String numberPlate) {
        try {
            // Simulating image capture (replace with actual capture logic)
            BufferedImage dummyImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

            // Generate a unique file name
            String fileName = numberPlate + "_" + UUID.randomUUID() + ".jpg";
            File imageFile = new File(IMAGE_DIRECTORY, fileName);

            // Ensure the directory exists
            File directory = new File(IMAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Save the dummy image
            ImageIO.write(dummyImage, "jpg", imageFile);

            System.out.println("Captured and saved image for " + numberPlate + " at " + imageFile.getAbsolutePath());

            // Save image path to the database
            saveImagePathToDatabase(numberPlate, imageFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving vehicle image: " + e.getMessage());
        }
    }

    /**
     * Saves the image path to the database associated with a number plate.
     *
     * @param numberPlate the vehicle's number plate
     * @param filePath the path of the image file
     */
    private void saveImagePathToDatabase(String numberPlate, String filePath) {
        String query = "INSERT INTO Vehicles (Number_Plate, Image_Path) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, numberPlate);
            pstmt.setString(2, filePath);
            pstmt.executeUpdate();
            System.out.println("Image path saved for " + numberPlate);
        } catch (SQLException e) {
            System.err.println("Database error while saving image path: " + e.getMessage());
        }
    }

    /**
     * Saves the captured image to disk and stores the path in the database.
     *
     * @param numberPlate the vehicle's number plate
     * @param image the image to save
     * @throws IOException if an error occurs while saving the image
     */
    public void saveCapturedImage(String numberPlate, java.awt.image.BufferedImage image) throws IOException {
        // Create the images directory if it does not exist
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Save image file
        String filePath = IMAGE_DIRECTORY + "/" + numberPlate + "_" + System.currentTimeMillis() + ".jpg";
        File outputFile = new File(filePath);
        ImageIO.write(image, "JPG", outputFile);

        System.out.println("Image saved: " + outputFile.getAbsolutePath());

        // Save image path to database
        saveImagePathToDatabase(numberPlate, filePath);
    }

    /**
     * Retrieves the image paths associated with a given number plate.
     *
     * @param numberPlate the vehicle's number plate
     * @return an array of file paths
     */
    public String[] getImagePathsByNumberPlate(String numberPlate) {
        String query = "SELECT Image_Path FROM Vehicles WHERE Number_Plate = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, numberPlate);
            ResultSet rs = pstmt.executeQuery();

            // Collect all image paths
            java.util.List<String> imagePaths = new java.util.ArrayList<>();
            while (rs.next()) {
                imagePaths.add(rs.getString("Image_Path"));
            }
            return imagePaths.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving images for plate: " + numberPlate);
        }
        return new String[0];
    }

    /**
     * Loads an image as an ImageIcon given its file path.
     *
     * @param filePath the path to the image file
     * @return an ImageIcon, or null if the image cannot be loaded
     */
    public ImageIcon loadImage(String filePath) {
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            return new ImageIcon(filePath);
        } else {
            System.err.println("Image file not found: " + filePath);
            return null;
        }
    }

    void captureVehicleImage(String vehicleType, String vehicleModel, String numberPlate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
