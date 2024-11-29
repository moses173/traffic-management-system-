/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Lil Teen
 */
public class VehicleAnimation extends JPanel {

    private int x, y;
    private int speed;
    private boolean moving;
    private boolean movingRight; // New field to track direction

    public VehicleAnimation(int x, int y, Color color, boolean movingRight) {
        this.x = x;
        this.y = y;
        this.speed = 5; // Default speed
        this.moving = false;
        this.movingRight = movingRight; // Set the direction
        setBounds(x, y, 50, 20);
        setBackground(color);
    }

    public void start() {
        this.moving = true;
    }

    public void stop() {
        this.moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public void move() {
        if (moving) {
            if (movingRight) {
                x += speed; // Move to the right (top lane)
                if (x > getParent().getWidth()) { // Reset if off screen
                    x = -50;
                }
            } else {
                x -= speed; // Move to the left (bottom lane)
                if (x < -50) { // Reset if off screen
                    x = getParent().getWidth();
                }
            }
            setLocation(x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
