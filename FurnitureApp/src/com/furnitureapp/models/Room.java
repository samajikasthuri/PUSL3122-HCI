package com.furnitureapp.models;

import java.awt.Color;

public class Room {
    private int width;
    private int height;
    private int depth;
    private String shape; // "Rectangle", "L-Shape", etc.
    private Color wallColor;
    private Color floorColor;
    
    public Room(int width, int height, int depth, String shape, Color wallColor, Color floorColor) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.shape = shape;
        this.wallColor = wallColor;
        this.floorColor = floorColor;
    }
    
    // Getters and setters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return depth; }
    public String getShape() { return shape; }
    public Color getWallColor() { return wallColor; }
    public Color getFloorColor() { return floorColor; }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setDepth(int depth) { this.depth = depth; }
    public void setShape(String shape) { this.shape = shape; }
    public void setWallColor(Color wallColor) { this.wallColor = wallColor; }
    public void setFloorColor(Color floorColor) { this.floorColor = floorColor; }
    
    // For JSON serialization
    public String toJson() {
        return String.format(
            "{\"width\":%d,\"height\":%d,\"depth\":%d," +
            "\"shape\":\"%s\"," +
            "\"wallColor\":%d,\"floorColor\":%d}",
            width, height, depth,
            shape,
            wallColor.getRGB(),
            floorColor.getRGB()
        );
    }
    
    // Create from JSON
    public static Room fromJson(String json) {
        // Simple JSON parsing (you might want to use a proper JSON library)
        try {
            json = json.replaceAll("[{}\"]", "");
            String[] parts = json.split(",");
            
            int width = 400, height = 250, depth = 500;
            String shape = "Rectangle";
            Color wallColor = Color.WHITE;
            Color floorColor = new Color(210, 180, 140); // Tan color
            
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    
                    switch (key) {
                        case "width": width = Integer.parseInt(value); break;
                        case "height": height = Integer.parseInt(value); break;
                        case "depth": depth = Integer.parseInt(value); break;
                        case "shape": shape = value; break;
                        case "wallColor": wallColor = new Color(Integer.parseInt(value)); break;
                        case "floorColor": floorColor = new Color(Integer.parseInt(value)); break;
                    }
                }
            }
            
            return new Room(width, height, depth, shape, wallColor, floorColor);
        } catch (Exception e) {
            System.err.println("Error parsing room JSON: " + e.getMessage());
            return new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
        }
    }
}