/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
/**
 *
 * @author Lil Teen
 */
public class AnalogClock extends JPanel{

    private Image backgroundImage;
    private Image cityClockImage;

    public AnalogClock() {
        // Load the background image
        backgroundImage = new ImageIcon("src/images/background.jpg").getImage();
        // Load the city clock image
        cityClockImage = new ImageIcon("src/images/city_clock.png").getImage();

        // Set preferred size
        setPreferredSize(new Dimension(300, 300));

        // Update clock every second
        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawClock(g);
    }

    private void drawClock(Graphics g) {
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the city clock image on top of the background
        g.drawImage(cityClockImage, 0, 0, getWidth(), getHeight(), this);

        // Get current time
        Calendar now = Calendar.getInstance();
        int seconds = now.get(Calendar.SECOND);
        int minutes = now.get(Calendar.MINUTE);
        int hours = now.get(Calendar.HOUR_OF_DAY);

        // Clock center
        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;
        int radius = Math.min(xCenter, yCenter) - 10;

        // Draw clock face (optional if you want to outline the clock)
        g.setColor(Color.BLACK);
        g.drawOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);

        // Draw Roman numerals
        String[] romanNumerals = {"XII", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"};
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(30 * i - 60); // Adjust angle to align with clock positions
            int x = (int) (xCenter + radius * 0.8 * Math.cos(angle) - 10); // Offset for centering text
            int y = (int) (yCenter + radius * 0.8 * Math.sin(angle) + 10); // Offset for centering text
            g.drawString(romanNumerals[i], x, y);
        }

        // Draw clock hands
        drawHand(g, 360 - (hours % 12) * 30 - minutes / 2, radius * 0.5, 5, Color.BLACK); // Hour hand
        drawHand(g, 360 - minutes * 6, radius * 0.8, 3, Color.BLUE); // Minute hand
        drawHand(g, 360 - seconds * 6, radius * 0.9, 1, Color.RED); // Second hand
    }

    private void drawHand(Graphics g, int angle, double length, int width, Color color) {
        g.setColor(color);
        int xCenter = getWidth() / 2;
        int yCenter = getHeight() / 2;

        double radian = Math.toRadians(angle);
        int xEnd = (int) (xCenter + length * Math.cos(radian));
        int yEnd = (int) (yCenter - length * Math.sin(radian));

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(xCenter, yCenter, xEnd, yEnd);
    }

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Analog Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of your AnalogClock panel
        AnalogClock clock = new AnalogClock();

        // Add the panel to the frame
        frame.add(clock);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}
