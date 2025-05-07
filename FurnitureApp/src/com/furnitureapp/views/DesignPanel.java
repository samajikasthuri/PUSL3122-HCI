package com.furnitureapp.views;

import com.furnitureapp.models.FurnitureItem;
import com.furnitureapp.utils.DBUtil;
import com.mysql.cj.xdevapi.Statement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.furnitureapp.models.Room; 


public class DesignPanel extends JPanel {
    private List<FurnitureItem> furnitureItems = new ArrayList<>();
    private JPanel drawingArea;
    private FurnitureItem selectedItem;
    private JComboBox<String> colorCombo;
    private JSpinner widthSpinner, heightSpinner;
    private int currentUserId;
    private Room currentRoom;
    private JButton configureRoomBtn;


    
    public DesignPanel(int userId) {
        this.currentUserId = userId;
        setLayout(new BorderLayout());

        this.currentRoom = new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
        setupToolbar();
        setupDrawingArea();
        setupPropertiesPanel();
        setupSaveButton();
   
      
    }

    private void setupRoomConfiguration() {
        configureRoomBtn = new JButton("Configure Room");
        configureRoomBtn.addActionListener(e -> showRoomConfigurationDialog());
        add(configureRoomBtn, BorderLayout.NORTH);
        
        // Initialize with default room
        currentRoom = new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
    }

    private void showRoomConfigurationDialog() {
    	if (currentRoom == null) {
            currentRoom = new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
        }
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Room Configuration", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 300);
        
        // Room dimensions
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(currentRoom.getWidth(), 100, 1000, 10));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(currentRoom.getHeight(), 100, 1000, 10));
        JSpinner depthSpinner = new JSpinner(new SpinnerNumberModel(currentRoom.getDepth(), 100, 1000, 10));
        
        // Room shape
        JComboBox<String> shapeCombo = new JComboBox<>(new String[]{"Rectangle", "L-Shape", "Square"});
        shapeCombo.setSelectedItem(currentRoom.getShape());
        
        // Color selectors
        JButton wallColorBtn = new JButton("Wall Color");
        wallColorBtn.setBackground(currentRoom.getWallColor());
        wallColorBtn.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(dialog, "Choose Wall Color", currentRoom.getWallColor());
            if (newColor != null) {
                currentRoom.setWallColor(newColor);
                wallColorBtn.setBackground(newColor);
            }
        });
        
        JButton floorColorBtn = new JButton("Floor Color");
        floorColorBtn.setBackground(currentRoom.getFloorColor());
        floorColorBtn.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(dialog, "Choose Floor Color", currentRoom.getFloorColor());
            if (newColor != null) {
                currentRoom.setFloorColor(newColor);
                floorColorBtn.setBackground(newColor);
            }
        });
        
        // Add components to dialog
        dialog.add(new JLabel("Width:"));
        dialog.add(widthSpinner);
        dialog.add(new JLabel("Height:"));
        dialog.add(heightSpinner);
        dialog.add(new JLabel("Depth:"));
        dialog.add(depthSpinner);
        dialog.add(new JLabel("Shape:"));
        dialog.add(shapeCombo);
        dialog.add(new JLabel("Wall Color:"));
        dialog.add(wallColorBtn);
        dialog.add(new JLabel("Floor Color:"));
        dialog.add(floorColorBtn);
        
        JButton saveBtn = new JButton("Save Configuration");
        saveBtn.addActionListener(e -> {
            currentRoom.setWidth((Integer)widthSpinner.getValue());
            currentRoom.setHeight((Integer)heightSpinner.getValue());
            currentRoom.setDepth((Integer)depthSpinner.getValue());
            currentRoom.setShape((String)shapeCombo.getSelectedItem());
            drawingArea.repaint();
            dialog.dispose();
        });
        
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }
    private void setupToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Furniture buttons on the left
        addFurnitureButton(toolBar, "Chair", 50, 50);
        addFurnitureButton(toolBar, "Table", 100, 60);
        addFurnitureButton(toolBar, "Sofa", 120, 40);
        
        // Add glue to push Configure Room button to the right
        toolBar.add(Box.createHorizontalGlue());
        
        // Configure Room button on the right
        JButton configureRoomBtn = new JButton("Configure Room");
        configureRoomBtn.addActionListener(e -> showRoomConfigurationDialog());
        toolBar.add(configureRoomBtn);
        
        add(toolBar, BorderLayout.NORTH);
    }

  
    private void addFurnitureButton(JToolBar toolBar, String type, int width, int height) {
        JButton button = new JButton(type);
        button.addActionListener(e -> {
            furnitureItems.add(new FurnitureItem(type, 100, 100, width, height));
            drawingArea.repaint();
        });
        toolBar.add(button);
    }

    private void setupDrawingArea() {
        drawingArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Draw room with current configuration - add null check
                if (currentRoom != null) {
                    g.setColor(currentRoom.getWallColor());
                    g.fillRect(50, 50, currentRoom.getWidth(), currentRoom.getHeight());
                    g.setColor(Color.BLACK);
                    g.drawRect(50, 50, currentRoom.getWidth(), currentRoom.getHeight());
                    
                    // Draw floor
                    g.setColor(currentRoom.getFloorColor());
                    g.fillRect(50, 50 + currentRoom.getHeight() - 20, currentRoom.getWidth(), 20);
                } else {
                    // Fallback drawing if room is null (shouldn't happen after our fix)
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(50, 50, 400, 250);
                    g.setColor(Color.BLACK);
                    g.drawRect(50, 50, 400, 250);
                }
                
                // Draw furniture
                for (FurnitureItem item : furnitureItems) {
                    item.draw(g);
                    if (item == selectedItem) {
                        g.setColor(Color.BLUE);
                        g.drawRect(item.getX()-2, item.getY()-2, 
                                  item.getWidth()+4, item.getHeight()+4);
                    }
                }
            }
        };
        drawingArea.setPreferredSize(new Dimension(900, 550));
        
        // Mouse interactions
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                selectedItem = furnitureItems.stream()
                    .filter(item -> item.contains(e.getX(), e.getY()))
                    .findFirst()
                    .orElse(null);
                drawingArea.repaint();
            }
        });
        
        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedItem != null) {
                    selectedItem.setX(e.getX() - selectedItem.getWidth()/2);
                    selectedItem.setY(e.getY() - selectedItem.getHeight()/2);
                    drawingArea.repaint();
                }
            }
        });
        drawingArea.setPreferredSize(new Dimension(900, 550));
        add(drawingArea, BorderLayout.CENTER);
    }
    public List<FurnitureItem> getFurnitureItems() {
        return new ArrayList<>(furnitureItems);
    }

    public void clearDesign() {
        furnitureItems.clear();
        drawingArea.repaint();
    }
    private void setupPropertiesPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Properties"));
        
        // Color selector
        panel.add(new JLabel("Color:"));
        colorCombo = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Yellow", "Black"});
        colorCombo.addActionListener(e -> updateItemColor());
        panel.add(colorCombo);
        
        // Size controls
        panel.add(new JLabel("Width:"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 200, 5));
        panel.add(widthSpinner);
        
        panel.add(new JLabel("Height:"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 200, 5));
        panel.add(heightSpinner);
        
        // Action buttons
        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> applyProperties());
        panel.add(applyBtn);
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteSelected());
        panel.add(deleteBtn);
        
        add(panel, BorderLayout.EAST);
    }

    private void setupSaveButton() {
        JButton saveBtn = new JButton("Save Design");
        saveBtn.addActionListener(e -> saveDesign());
        add(saveBtn, BorderLayout.SOUTH);
    }

    private void saveDesign() {
        if (furnitureItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No furniture items to save!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String designName = JOptionPane.showInputDialog(this, 
            "Enter design name:", 
            "Save Design", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (designName == null || designName.trim().isEmpty()) {
            return; // User cancelled or entered empty name
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            String json = furnitureItems.stream()
                .map(FurnitureItem::toJson)
                .collect(Collectors.joining(",", "[", "]"));
            
            String sql = "INSERT INTO designs (designer_id, design_name, design_data, room_config) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, currentUserId);
            stmt.setString(2, designName);
            stmt.setString(3, json);
            stmt.setString(4, currentRoom.toJson());
            
            // 4. Execute and check results
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating design failed, no rows affected.");
            }
            
            // 5. Get generated ID
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int designId = generatedKeys.getInt(1);
                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "Design saved successfully! ID: " + designId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new SQLException("Creating design failed, no ID obtained.");
            }
        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                ex.addSuppressed(e);
            }
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            // 6. Clean up resources
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.closeConnection(conn);
        }
    }
    public void loadDesign(String furnitureJson, Room room) {
        clearDesign();
        
        // Ensure we always have a valid room
        this.currentRoom = (room != null) ? room : 
            new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
        
        try {
            // Load furniture items
            if (furnitureJson != null && !furnitureJson.trim().isEmpty()) {
                String content = furnitureJson.substring(1, furnitureJson.length() - 1).trim();
                String[] items = content.split("\\s*},\\s*\\{");
                
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    if (i != 0) item = "{" + item;
                    if (i != items.length - 1) item = item + "}";
                    
                    FurnitureItem furniture = parseJsonToFurnitureItem(item);
                    if (furniture != null) {
                        furnitureItems.add(furniture);
                    }
                }
            }
            
            drawingArea.repaint();
        } catch (Exception ex) {
            System.err.println("Error loading design:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading design: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private FurnitureItem parseJsonToFurnitureItem(String json) {
        try {
            // Normalize the JSON string first
            json = json.trim();
            
            // Extract values with more robust pattern matching
            String type = extractJsonValue(json, "type");
            int x = Integer.parseInt(extractJsonValue(json, "x"));
            int y = Integer.parseInt(extractJsonValue(json, "y"));
            int width = Integer.parseInt(extractJsonValue(json, "width"));
            int height = Integer.parseInt(extractJsonValue(json, "height"));
            String color = extractJsonValue(json, "color");
            
            System.out.println("Parsed values - Type: " + type + ", X: " + x + ", Y: " + y + 
                             ", Width: " + width + ", Height: " + height + ", Color: " + color); // Debug
            
            FurnitureItem item = new FurnitureItem(type, x, y, width, height);
            item.setColorByName(color);
            return item;
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + json);
            e.printStackTrace();
            return null;
        }
    }

    private String extractJsonValue(String json, String key) {
        // Look for either "key":"value" or "key":value pattern
        String pattern = "\"" + key + "\"\\s*:\\s*(\"[^\"]*\"|\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        
        if (m.find()) {
            String value = m.group(1);
            // Remove surrounding quotes if present
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            return value;
        }
        throw new IllegalArgumentException("Could not find key: " + key);
    }
    private void updateItemColor() {
        if (selectedItem != null) {
            selectedItem.setColorByName((String)colorCombo.getSelectedItem());
            drawingArea.repaint();
        }
    }

    private void applyProperties() {
        if (selectedItem != null) {
            selectedItem.setWidth((int)widthSpinner.getValue());
            selectedItem.setHeight((int)heightSpinner.getValue());
            drawingArea.repaint();
        }
    }

    private void deleteSelected() {
        if (selectedItem != null) {
            furnitureItems.remove(selectedItem);
            selectedItem = null;
            drawingArea.repaint();
        }
    }
} 