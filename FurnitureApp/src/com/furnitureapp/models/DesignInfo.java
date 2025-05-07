package com.furnitureapp.models;

import java.sql.Timestamp;

public class DesignInfo {
    private int designId;
    private String designName;
    private Timestamp createdAt;

    public DesignInfo(int designId, String designName, Timestamp createdAt) {
        this.designId = designId;
        this.designName = designName;
        this.createdAt = createdAt;
    }

    // Getters
    public int getDesignId() { return designId; }
    public String getDesignName() { return designName; }
    public Timestamp getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return designName + " (Created: " + createdAt + ")";
    }
}