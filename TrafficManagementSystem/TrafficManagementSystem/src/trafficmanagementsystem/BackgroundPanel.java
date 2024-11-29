/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
/**
 *
 * @author Lil Teen
 */
public class BackgroundPanel extends JPanel {
   private Image backgroundImage;

    // Updated constructor to accept URL type
    public BackgroundPanel(URL imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
