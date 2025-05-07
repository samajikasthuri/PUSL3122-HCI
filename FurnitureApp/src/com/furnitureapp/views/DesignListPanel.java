package com.furnitureapp.views;

import com.furnitureapp.models.DesignInfo;
import com.furnitureapp.utils.DBUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import com.furnitureapp.models.Room; 
public class DesignListPanel extends JPanel {
    private int currentUserId;
    private JList<DesignInfo> designList;
    private DefaultListModel<DesignInfo> listModel;
    private DesignPanel designPanel;

    public DesignListPanel(int userId, DesignPanel designPanel) {
        this.currentUserId = userId;
        this.designPanel = designPanel;
        
        // Set up the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        loadDesigns();
    }

    private void initializeComponents() {
        // Create list model and JList
        listModel = new DefaultListModel<>();
        designList = new JList<>(listModel);
        designList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        designList.setCellRenderer(new DesignListRenderer());
        
        // Add mouse listener for double-click
        designList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadSelectedDesign();
                }
            }
        });

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(designList);
        add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton loadButton = new JButton("Load Design");
        loadButton.addActionListener(e -> loadSelectedDesign());
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedDesign());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadDesigns());
        
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDesigns() {
        listModel.clear();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT design_id, design_name, created_at FROM designs WHERE designer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, currentUserId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listModel.addElement(new DesignInfo(
                    rs.getInt("design_id"),
                    rs.getString("design_name"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading designs: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedDesign() {
        DesignInfo selected = designList.getSelectedValue();
        if (selected != null) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "SELECT design_data, room_config FROM designs WHERE design_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, selected.getDesignId());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String furnitureJson = rs.getString("design_data");
                    String roomJson = rs.getString("room_config");
                    
                    // If room configuration exists, use it; otherwise use default
                    Room room = (roomJson != null && !roomJson.isEmpty()) ? 
                        Room.fromJson(roomJson) : 
                        new Room(400, 250, 500, "Rectangle", Color.WHITE, new Color(210, 180, 140));
                    
                    designPanel.loadDesign(furnitureJson, room);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error loading design: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String createDefaultRoomJson() {
        // Create a default room configuration JSON string
        return String.format(
            "{\"width\":400,\"height\":250,\"depth\":500," +
            "\"shape\":\"Rectangle\"," +
            "\"wallColor\":%d,\"floorColor\":%d}",
            Color.WHITE.getRGB(),
            new Color(210, 180, 140).getRGB()
        );
    }
    private void deleteSelectedDesign() {
        DesignInfo selected = designList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete design '" + selected.getDesignName() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "DELETE FROM designs WHERE design_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, selected.getDesignId());
                    
                    int affected = stmt.executeUpdate();
                    if (affected > 0) {
                        listModel.removeElement(selected);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error deleting design: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    // ... rest of your methods (loadDesigns, loadSelectedDesign, deleteSelectedDesign) ...
    
    // Custom cell renderer
    private static class DesignListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof DesignInfo) {
                DesignInfo design = (DesignInfo) value;
                setText(String.format("<html><b>%s</b><br><small>Created: %s</small></html>",
                    design.getDesignName(),
                    design.getCreatedAt()));
            }
            return this;
        }
    }
}