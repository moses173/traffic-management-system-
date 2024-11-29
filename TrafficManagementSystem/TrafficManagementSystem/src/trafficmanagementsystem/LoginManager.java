/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trafficmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class LoginManager extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JTable resultsTable;
    private JLabel imageLabel;
    private List<ImageIcon> vehicleImages = new ArrayList<>();

    public LoginManager() {
        setTitle("Manager Interface - Vehicle Lookup");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load vehicle images
        loadVehicleImages();

        // Top Panel for Search
        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(new JLabel("Search by Number Plate or Vehicle Type:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Table to Display Results
    resultsTable = new JTable(new DefaultTableModel(
    new String[]{"ID", "Type", "Model", "Number Plate", "Image Path"}, 0));
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);

        // Image Panel to Display Selected Vehicle's Image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imageLabel = new JLabel("Select a vehicle to view its image", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Add Action Listener to the Search Button
        searchButton.addActionListener(e -> searchVehicles());

        // Add Selection Listener to the Table
        resultsTable.getSelectionModel().addListSelectionListener(e -> displaySelectedImage());

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);
        getContentPane().add(imagePanel, BorderLayout.SOUTH);
    }

    public void showLogin() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Login",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                this.setVisible(true); // Show the manager interface
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.");
                showLogin(); // Retry login
            }
        } else {
            System.exit(0); // Exit if user cancels
        }
    }

    private boolean authenticate(String username, String password) {
        // Dummy authentication logic; replace with database verification
        return "manager".equals(username) && "password123".equals(password);
    }

    private void searchVehicles() {
        String searchQuery = searchField.getText().trim();

        // Clear Table
         DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
    model.setRowCount(0);

    String query = "SELECT id, Vehicle_Type, Vehicle_Model, Number_Plate, Image_Path FROM Vehicles WHERE Number_Plate LIKE ? OR Vehicle_Type LIKE ?";

    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(query)) {

        pstmt.setString(1, "%" + searchQuery + "%");
        pstmt.setString(2, "%" + searchQuery + "%");

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
    rs.getInt("id"), 
    rs.getString("Vehicle_Type") != null ? rs.getString("Vehicle_Type") : "N/A",   // Handle null
    rs.getString("Vehicle_Model") != null ? rs.getString("Vehicle_Model") : "N/A",  // Handle null
    rs.getString("Number_Plate"),
    rs.getString("Image_Path")
});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching vehicle data. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void loadVehicleImages() {
    String[] imagePaths = {
        "/resources/front_image1.jpg",
        "/resources/back_image1.jpg",
        "/resources/front_image2.jpg",
        "/resources/back_image2.jpg"
    };

    for (String path : imagePaths) {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            vehicleImages.add(image);
            System.out.println("Image loaded successfully: " + path);
        } else {
            System.err.println("Image not found: " + path);
        }
    }
}

    private void displaySelectedImage() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
            String imagePath = (String) model.getValueAt(selectedRow, 4);

            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                    Image image = imageIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                    imageLabel.setText(""); // Clear text
                } else {
                    imageLabel.setText("Image file not found.");
                    imageLabel.setIcon(null);
                }
            } else {
                imageLabel.setText("No image available for this vehicle.");
                imageLabel.setIcon(null);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginManager managerInterface = new LoginManager();
            managerInterface.showLogin(); // Call showLogin at startup
        });
    }
}
