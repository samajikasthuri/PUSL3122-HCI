package com.furnitureapp.models;

import java.awt.Color;
import java.awt.Graphics;
import javax.vecmath.Color3f;

public class FurnitureItem {
    private String type;
    private int x, y, width, height;
    private Color color;
    
    public FurnitureItem(String type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.RED;
    }
    
    // Drawing method for 2D view
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(type, x + 5, y + 15);
    }
    
    // Hit detection
    public boolean contains(int pointX, int pointY) {
        return pointX >= x && pointX <= x + width &&
               pointY >= y && pointY <= y + height;
    }
    
    // Color management
    public void setColorByName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red": color = Color.RED; break;
            case "blue": color = Color.BLUE; break;
            case "green": color = Color.GREEN; break;
            case "yellow": color = Color.YELLOW; break;
            case "black": color = Color.BLACK; break;
            default: color = Color.GRAY;
        }
    }
    
    public String getColorName() {
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.GREEN)) return "Green";
        if (color.equals(Color.YELLOW)) return "Yellow";
        if (color.equals(Color.BLACK)) return "Black";
        return "Custom";
    }

    // Getters and setters
    public String getType() { return type; }
    public Color getColor() { return color; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    
    // For 3D conversion

    public Color3f getColor3f() {
        Color c = getColor();
        return new Color3f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
    }
    // For JSON serialization
    public String toJson() {
        return String.format(
            "{\"type\":\"%s\",\"x\":%d,\"y\":%d,\"width\":%d,\"height\":%d,\"color\":\"%s\"}",
            type.replace("\"", "\\\""),
            x, y, width, height,
            getColorName().replace("\"", "\\\"")
        );
    }
}