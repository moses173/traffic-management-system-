/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author Lil Teen
 */
public class CameraPanel extends JPanel {

    private JLabel cameraLabel;
    private boolean flashOn;

    public CameraPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 150));
        setBorder(BorderFactory.createTitledBorder("Camera"));

        cameraLabel = new JLabel("Camera: Ready", SwingConstants.CENTER);
        cameraLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(cameraLabel, BorderLayout.CENTER);
    }

    public void captureFlash() {
        flashOn = true;
        setBackground(Color.YELLOW);
        cameraLabel.setText("Capturing...");
        repaint();

        // Simulate flash duration
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                flashOn = false;
                setBackground(Color.LIGHT_GRAY);
                cameraLabel.setText("Camera: Ready");
                repaint();
            }
        }, 500); // Flash lasts 500ms
    }

    public boolean isFlashOn() {
        return flashOn;
    }
}

