/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
/**
 *
 * @author Lil Teen
 */
public class ImageRetrieval {

public void showImages() {
        JFrame imageFrame = new JFrame("SMART LIFE TECHNOLOGY MANAGINGROOM");
        imageFrame.setSize(600, 400);
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(1, 2));

        // Load front and back number plate images with proper error handling
        URL frontImageUrl = getClass().getResource("/resources/front_number_plate.jpg");
        URL backImageUrl = getClass().getResource("/resources/back_number_plate.jpg");

        // Check if front image exists
        JLabel frontImageLabel = new JLabel();
        if (frontImageUrl != null) {
            frontImageLabel.setIcon(new ImageIcon(frontImageUrl));
        } else {
            System.out.println("Front number plate image not found at /resources/front_number_plate.jpg");
            frontImageLabel.setText("Front Image Not Found");
        }

        // Check if back image exists
        JLabel backImageLabel = new JLabel();
        if (backImageUrl != null) {
            backImageLabel.setIcon(new ImageIcon(backImageUrl));
        } else {
            System.out.println("Back number plate image not found at /resources/back_number_plate.jpg");
            backImageLabel.setText("Back Image Not Found");
        }

        // Add the labels to the panel
        imagePanel.add(frontImageLabel);
        imagePanel.add(backImageLabel);

        imageFrame.add(imagePanel);
        imageFrame.setVisible(true);
    }
}