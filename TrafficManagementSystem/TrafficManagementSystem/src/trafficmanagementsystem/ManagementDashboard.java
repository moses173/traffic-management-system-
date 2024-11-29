/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
/**
 *
 * @author Lil Teen
 */
public class ManagementDashboard extends JFrame {

    public ManagementDashboard() {
        setTitle("Management Dashboard");
        setSize(800, 600); // Set the size of the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window on exit
        setLocationRelativeTo(null); // Center the window on the screen

        // Create a panel to hold the images
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 3)); // 3 images per row

        // Load and display images
        displayImages(imagePanel);

        // Add the image panel to the frame
        add(imagePanel, BorderLayout.CENTER);
    }

    private void displayImages(JPanel imagePanel) {
        // Example file paths for vehicle images (update these paths as needed)
        String[] imagePaths = {
            "C:\\path\\to\\vehicle1.jpg", // Replace with actual paths
            "C:\\path\\to\\vehicle2.jpg",
            "C:\\path\\to\\vehicle3.jpg"
            // Add more paths as needed
        };

        for (String path : imagePaths) {
            try {
                // Load the image
                BufferedImage img = ImageIO.read(new File(path));
                // Create an ImageIcon from the BufferedImage
                ImageIcon icon = new ImageIcon(img.getScaledInstance(200, 150, Image.SCALE_SMOOTH)); // Resize image
                JLabel label = new JLabel(icon);
                imagePanel.add(label); // Add label to the panel
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions (e.g., file not found)
                JOptionPane.showMessageDialog(this, "Error loading image: " + path, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}