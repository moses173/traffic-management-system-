/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Lil Teen
 */
public class TrafficLight extends JPanel{
    private Color redLight = Color.GRAY;
    private Color yellowLight = Color.GRAY;
    private Color greenLight = Color.GRAY;
    private Image backgroundImage;
    private Timer timer;

    public TrafficLight() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrafficManagementSystem\\src\\resources\\panel.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(150, 360));  // Adjust the size as needed
    }

    public void startTrafficControl() {
        if (timer == null) { // Prevent multiple timers from being created
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    changeLights();  // Change lights every 2 seconds
                }
            }, 0, 10000); // Change lights every 2 seconds
        }
    }

    private void changeLights() {
        if (redLight == Color.GRAY && yellowLight == Color.GRAY && greenLight == Color.GRAY) {
            redLight = Color.RED;
            yellowLight = Color.GRAY;
            greenLight = Color.GRAY;
        } else if (redLight == Color.RED) {
            redLight = Color.GRAY;
            yellowLight = Color.YELLOW;
        } else if (yellowLight == Color.YELLOW) {
            yellowLight = Color.GRAY;
            greenLight = Color.GREEN;
        } else if (greenLight == Color.GREEN) {
            greenLight = Color.GRAY;
            redLight = Color.RED;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(redLight);
        g.fillOval(50, 55, 45, 67);  // Red light
        g.setColor(greenLight);
        g.fillOval(50, 155, 45, 67);  // Green light
        g.setColor(yellowLight);
        g.fillOval(50, 255, 45, 67);  // Yellow light
    }

    public void stopTrafficControl() {
        if (timer != null) {
            timer.cancel();
            timer = null; // Reset the timer to allow restarting
        }
    }

    public boolean isRed() {
        return redLight == Color.RED;
    }

    public boolean isYellow() {
        return yellowLight == Color.YELLOW;
    }

    public boolean isGreen() {
        return greenLight == Color.GREEN;
    }
} 