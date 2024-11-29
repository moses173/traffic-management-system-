/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package trafficmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * AI-Based Smart Traffic Management System
 * 
 * This system simulates traffic control with vehicles being detected, logged, 
 * and their images captured.
 * 
 * @author Lil Teen
 */
public class TrafficManagementSystem extends JFrame{

    private TrafficLight trafficLight;
    private AnalogClock analogClock;
    private JPanel imagePanel;
    private JTextArea vehicleRecordArea;
    private Timer vehicleTimer;
    private Random random;
    private JTextField filterField;
    private JButton filterButton;
    private TrafficCameraSystem cameraSystem;
    private List<String> vehicleRecords;
    private VehicleImageManager imageManager; // Instance of VehicleImageManager
    private JPanel cameraPanel; // Camera display panel
    private JLabel cameraLabel; // Camera icon/representation
    private boolean isCameraOn = false;
    private JPanel flashPanel;
    private final Set<String> usedPlates = new HashSet<>();
    private final String[] vehicleTypes = { "Car", "Truck", "Bus", "Motorcycle", "Van", "Government", "Police", "Ambulance" };
    private final String[] carModels = { "Toyota", "Honda", "Ford", "Benz", "BMW", "Prado","Ferrari","Range Roofer","Subaru","Corrola","ProBox" };

    // For Vehicle Animations
    private JPanel vehiclePanel;
    private List<VehicleAnimation> vehicles;
    private Timer animationTimer;

    public TrafficManagementSystem() {
        setTitle("AI-Based Smart Traffic Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        cameraSystem = new TrafficCameraSystem();
        vehicleRecords = new ArrayList<>();
        imageManager = new VehicleImageManager();
        random = new Random();

        DatabaseUtil.createPlateTrackerTable(); // Ensure database table exists

        // UI Setup
        setupUI();
    }

    private void setupUI() {
        BackgroundPanel backgroundPanel = new BackgroundPanel(getClass().getResource("/resources/image1.jpg"));
        setContentPane(backgroundPanel);

        // Analog clock at the top
        analogClock = new AnalogClock();
        analogClock.setPreferredSize(new Dimension(140, 140));
        backgroundPanel.add(analogClock, BorderLayout.NORTH);

        // Camera panel for traffic monitoring
        cameraPanel = new JPanel();
        cameraPanel.setLayout(new BorderLayout());
        cameraLabel = new JLabel("Camera", SwingConstants.CENTER);

        JPanel cameraIcon = new JPanel();
        cameraIcon.setPreferredSize(new Dimension(240, 240));
        cameraIcon.setBackground(Color.GRAY);
        cameraIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JPanel cameraLens = new JPanel();
        cameraLens.setPreferredSize(new Dimension(60, 60));
        cameraLens.setBackground(Color.BLACK);
        cameraLens.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
        cameraIcon.add(cameraLens);

        cameraPanel.add(cameraIcon, BorderLayout.CENTER);
        backgroundPanel.add(cameraPanel, BorderLayout.NORTH);

        flashPanel = new JPanel();
        flashPanel.setBackground(Color.WHITE); // Flash effect
        flashPanel.setOpaque(false);
        cameraPanel.add(flashPanel, BorderLayout.CENTER);

        // Traffic light in the center
        trafficLight = new TrafficLight();
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(trafficLight);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer panel for buttons and filters
        JPanel footerPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton goButton = new JButton("Go");
        goButton.addActionListener(e -> trafficLight.startTrafficControl());
        buttonPanel.add(goButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> new LoginManager().showLogin());
        buttonPanel.add(loginButton);
        footerPanel.add(buttonPanel, BorderLayout.NORTH);

        filterField = new JTextField(15);
        filterButton = new JButton("Filter");
        filterButton.addActionListener(new FilterAction(vehicleRecords));
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Enter Number Plate:"));
        filterPanel.add(filterField);
        filterPanel.add(filterButton);
        footerPanel.add(filterPanel, BorderLayout.SOUTH);

        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        // Vehicle record area on the right
        vehicleRecordArea = new JTextArea(10, 40);
        vehicleRecordArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(vehicleRecordArea);
        backgroundPanel.add(scrollPane, BorderLayout.EAST);

        // Image panel on the left
        imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 2));
        loadVehicleImagesToPanel();
        backgroundPanel.add(imagePanel, BorderLayout.WEST);

        // Vehicle animation panel
        vehiclePanel = new JPanel(null); // Use null layout for precise positioning
        vehiclePanel.setPreferredSize(new Dimension(800, 400));
        vehiclePanel.setOpaque(false);
        backgroundPanel.add(vehiclePanel, BorderLayout.CENTER);

        vehicles = new ArrayList<>();
        int roadHeight = 200;
        for (int i = 0; i < 5; i++) {
        VehicleAnimation vehicle = new VehicleAnimation(50, 50 + (i * 30), 
            new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 
            true
        );
        vehicles.add(vehicle);
        vehiclePanel.add(vehicle);
    }
        for (int i = 0; i < 5; i++) {
        VehicleAnimation vehicle = new VehicleAnimation(
            vehiclePanel.getWidth() - 50, roadHeight + 50 + (i * 30),
            new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)),
            false
        );
        vehicles.add(vehicle);
        vehiclePanel.add(vehicle);
    }

        // Timer for vehicle animations
        animationTimer = new Timer(50, e -> updateVehiclePositions());
        animationTimer.start();

        // Timer for simulating vehicle passage
        vehicleTimer = new Timer(2000, e -> simulateVehiclePassing());
        vehicleTimer.start();
    }

    private void loadVehicleImagesToPanel() {
        String[] imagePaths = {
            "/resources/front_image1.jpg",
            "/resources/back_image1.jpg",
            "/resources/front_image2.jpg",
            "/resources/back_image2.jpg"
        };

        for (String path : imagePaths) {
            ImageIcon image = imageManager.loadImage(path);
            if (image != null) {
                JLabel label = new JLabel(image);
                imagePanel.add(label);
            }
        }
    }

    private void updateVehiclePositions() {
    boolean greenLight = trafficLight.isGreen();
    boolean yellowLight = trafficLight.isYellow();

    for (VehicleAnimation vehicle : vehicles) {
        if (greenLight) {
            vehicle.start();
        } else if (yellowLight) {
            vehicle.stop(); // Optional: Slow down logic
        } else {
            vehicle.stop(); // Stop on red
        }

        vehicle.move();
    }
}

    private String generateRandomUniqueNumberPlate() {
        String prefix = "ABC";
        String numberPlate;

        do {
            int randomNumber = 1000 + random.nextInt(9000);
            numberPlate = String.format("%s-%04d", prefix, randomNumber);
        } while (usedPlates.contains(numberPlate));

        usedPlates.add(numberPlate);
        return numberPlate;
    }

    private void simulateVehiclePassing() {
        if (trafficLight.isGreen()) {
            turnOnCamera();
            for (VehicleAnimation vehicle : vehicles) {
                if (vehicle.isMoving()) {
                    String vehicleType = vehicleTypes[random.nextInt(vehicleTypes.length)];
                    String vehicleModel = carModels[random.nextInt(carModels.length)];
                    String numberPlate = generateRandomUniqueNumberPlate();
                    recordVehicle(vehicleType, vehicleModel, numberPlate);
                }
            }
        }
    }

    private void turnOnCamera() {
        if (!isCameraOn) {
            isCameraOn = true;
            cameraPanel.setBackground(Color.GREEN);
            cameraLabel.setText("Camera ON");
        }
    }

    private void triggerFlash() {
        flashPanel.setOpaque(true);
        cameraPanel.repaint();
        Timer flashTimer = new Timer(100, e -> {
            flashPanel.setOpaque(false);
            cameraPanel.repaint();
        });
        flashTimer.setRepeats(false);
        flashTimer.start();
    }

    private void recordVehicle(String vehicleType, String vehicleModel, String numberPlate) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        String record = String.format("%s: %s (%s) at %s", vehicleType, vehicleModel, numberPlate, formattedTime);
        vehicleRecordArea.append(record + "\n");
        vehicleRecords.add(record);

        imageManager.captureAndSaveVehicleImage(numberPlate);
        triggerFlash();
        cameraSystem.detectVehicle(vehicleType, vehicleModel, numberPlate, "image_path_here");
    }

    private class FilterAction implements ActionListener {
        private final List<String> vehicleRecords;

        public FilterAction(List<String> vehicleRecords) {
            this.vehicleRecords = vehicleRecords;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String filterText = filterField.getText().trim();
            StringBuilder result = new StringBuilder();
            int count = 0;

            for (String record : vehicleRecords) {
                if (record.contains(filterText)) {
                    result.append(record).append("\n");
                    count++;
                }
            }

            if (count > 0) {
                JOptionPane.showMessageDialog(null, result.toString(), "Filter Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No match found!", "Filter Results", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrafficManagementSystem().setVisible(true));
    }
}
