package com.furnitureapp.views;

import com.furnitureapp.models.Room;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class RoomConfigPanel extends JPanel {
    private JSpinner widthSpinner, heightSpinner, depthSpinner;
    private JComboBox<String> shapeCombo;
    private JButton wallColorBtn, floorColorBtn;
    private Color currentWallColor = Color.WHITE;
    private Color currentFloorColor = new Color(210, 180, 140); // Tan color
    
    private Room currentRoom;
    private Consumer<Room> roomChangeListener;
    
    public RoomConfigPanel() {
        setLayout(new GridLayout(0, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Room Configuration"));
        initializeComponents();
        currentRoom = createRoomFromInputs();
    }
    
    public void setInitialRoom(Room room) {
        this.currentRoom = room;
        widthSpinner.setValue(room.getWidth());
        heightSpinner.setValue(room.getHeight());
        depthSpinner.setValue(room.getDepth());
        shapeCombo.setSelectedItem(room.getShape());
        currentWallColor = room.getWallColor();
        currentFloorColor = room.getFloorColor();
        wallColorBtn.setBackground(currentWallColor);
        floorColorBtn.setBackground(currentFloorColor);
    }
    
    public void addRoomChangeListener(Consumer<Room> listener) {
        this.roomChangeListener = listener;
    }
    
    public void setRoom(Room room) {
        this.currentRoom = room;
        if (roomChangeListener != null) {
            roomChangeListener.accept(room);
        }
    }
    
    private void initializeComponents() {
        // Room dimensions
        add(new JLabel("Width (cm):"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(400, 200, 1000, 10));
        add(widthSpinner);
        
        add(new JLabel("Height (cm):"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(250, 200, 400, 10));
        add(heightSpinner);
        
        add(new JLabel("Depth (cm):"));
        depthSpinner = new JSpinner(new SpinnerNumberModel(500, 200, 1000, 10));
        add(depthSpinner);
        
        // Room shape
        add(new JLabel("Shape:"));
        shapeCombo = new JComboBox<>(new String[]{"Rectangle", "Square", "L-Shaped"});
        add(shapeCombo);
        
        // Color selection
        add(new JLabel("Wall Color:"));
        wallColorBtn = createColorButton(currentWallColor, e -> {
            currentWallColor = JColorChooser.showDialog(this, "Choose Wall Color", currentWallColor);
            ((JButton)e.getSource()).setBackground(currentWallColor);
            notifyRoomChanged();
        });
        add(wallColorBtn);
        
        add(new JLabel("Floor Color:"));
        floorColorBtn = createColorButton(currentFloorColor, e -> {
            currentFloorColor = JColorChooser.showDialog(this, "Choose Floor Color", currentFloorColor);
            ((JButton)e.getSource()).setBackground(currentFloorColor);
            notifyRoomChanged();
        });
        add(floorColorBtn);
        
        // Add listeners to dimension spinners
        addChangeListener(widthSpinner);
        addChangeListener(heightSpinner);
        addChangeListener(depthSpinner);
        
        // Shape combo listener
        shapeCombo.addActionListener(e -> notifyRoomChanged());
    }
    
    private JButton createColorButton(Color color, ActionListener listener) {
        JButton button = new JButton();
        button.setBackground(color);
        button.setPreferredSize(new Dimension(30, 30));
        button.addActionListener(listener);
        return button;
    }
    
    private void addChangeListener(JSpinner spinner) {
        spinner.addChangeListener(e -> notifyRoomChanged());
    }
    
    private void notifyRoomChanged() {
        currentRoom = createRoomFromInputs();
        if (roomChangeListener != null) {
            roomChangeListener.accept(currentRoom);
        }
    }
    
    private Room createRoomFromInputs() {
        return new Room(
            (Integer)widthSpinner.getValue(),
            (Integer)heightSpinner.getValue(),
            (Integer)depthSpinner.getValue(),
            (String)shapeCombo.getSelectedItem(),
            currentWallColor,
            currentFloorColor
        );
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
}