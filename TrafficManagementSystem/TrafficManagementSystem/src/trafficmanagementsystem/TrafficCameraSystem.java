/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;

public class TrafficCameraSystem extends JPanel {

    private Webcam webcam;
    private JLabel cameraFeedLabel;
    private JButton captureButton;
    private Timer timer;

    public TrafficCameraSystem() {
        setLayout(new BorderLayout());

        cameraFeedLabel = new JLabel("Camera Feed", SwingConstants.CENTER);
        cameraFeedLabel.setPreferredSize(new Dimension(400, 300));
        cameraFeedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(cameraFeedLabel, BorderLayout.CENTER);

        captureButton = new JButton("Capture");
        captureButton.addActionListener(e -> captureImage());
        add(captureButton, BorderLayout.SOUTH);

        try {
            initializeWebcam();
        } catch (WebcamException e) {
            System.err.println("Error initializing webcam: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to initialize the webcam. Please check your setup.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Start a timer to capture images automatically every second if webcam is available
        if (webcam != null && webcam.isOpen()) {
            timer = new Timer(1000, e -> captureImage());
            timer.start();
        }
    }

    private void initializeWebcam() {
        try {
            webcam = Webcam.getDefault();
            if (webcam == null) {
                throw new WebcamException("No webcam detected. Please connect a webcam.");
            }

            webcam.setViewSize(WebcamResolution.VGA.getSize());
            if (!webcam.isOpen()) {
                webcam.open();
                System.out.println("Webcam initialized successfully: " + webcam.getName());
            }
        } catch (WebcamException e) {
            System.err.println("Error accessing the webcam: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during webcam initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void captureImage() {
        try {
            if (webcam == null || !webcam.isOpen()) {
                System.err.println("Webcam is not open. Cannot capture image.");
                return;
            }

            new File("images").mkdirs(); // Ensure "images" directory exists
            File imageFile = new File("images/" + System.currentTimeMillis() + ".jpg");
            ImageIO.write(webcam.getImage(), "JPG", imageFile);
            System.out.println("Captured image: " + imageFile.getAbsolutePath());

            // Process and record the detected vehicle
            detectVehicle("Car", "ModelX", "ABC123", imageFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving captured image: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to save the captured image. Check your storage and permissions.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Unexpected error during image capture: " + e.getMessage());
            e.printStackTrace();
        }
    }
 public void detectVehicle(String vehicleType, String vehicleModel, String numberPlate, String imagePath) {
    if (webcam != null && webcam.isOpen()) {
        File imageFile = new File(imagePath);
        try {
            ImageIO.write(webcam.getImage(), "JPG", imageFile);
            System.out.println("Vehicle image captured: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error capturing vehicle image: " + e.getMessage());
        }
    } else {
        System.err.println("Webcam is not initialized or is closed.");
    }
}


    public void close() {
        try {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
                System.out.println("Webcam closed successfully.");
            }
            DatabaseUtil.closeConnection();
        } catch (Exception e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
