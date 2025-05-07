package com.furnitureapp.views;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import com.furnitureapp.models.FurnitureItem;
import com.jogamp.opengl.GLProfile;

import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;

public class MainFrame extends JFrame {
    private int currentUserId;
    private DesignPanel designPanel;

    // 3D Environment Check Class
    public static class ThreeDEnvironmentCheck {
        
        public static boolean checkEnvironment() {
            System.out.println("\n=== 3D Environment Diagnostics ===");
            
            // 1. Check Java Version
            System.out.println("Java Version: " + System.getProperty("java.version"));
            
            // 2. Check JOGL
            boolean joglOK = checkJOGL();
            
            // 3. Check Java3D
            boolean java3dOK = checkJava3D();
            
            System.out.println("=== Diagnostics Complete ===");
            return joglOK && java3dOK;
        }
        
        private static boolean checkJOGL() {
            try {
                System.out.println("\n[Checking JOGL]");
                GLProfile.initSingleton();
                
                // Correct way to check available profiles in JOGL 2.4.0
                System.out.println("Default Profile: " + GLProfile.getDefault());
                System.out.println("GL2 Available: " + GLProfile.isAvailable(GLProfile.GL2));
                System.out.println("GL4 Available: " + GLProfile.isAvailable(GLProfile.GL4));
                
                return true;
            } catch (Exception e) {
                System.err.println("JOGL Initialization Failed:");
                e.printStackTrace();
                return false;
            }
        }
        
        private static boolean checkJava3D() {
            try {
                System.out.println("\n[Checking Java3D]");
                GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
                
                if (config == null) {
                    System.err.println("Failed to get graphics configuration");
                    return false;
                }
                
                System.out.println("Graphics Configuration: " + config.getClass().getName());
                System.out.println("Java3D appears to be initialized properly");
                return true;
                
            } catch (Exception e) {
                System.err.println("Java3D Initialization Failed:");
                e.printStackTrace();
                return false;
            }
        }
    }

    // Constructor to initialize the main UI
    public MainFrame(int userId) {
        this.currentUserId = userId;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Furniture Design App");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tabbed pane for multiple views
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create design panel with user ID
        designPanel = new DesignPanel(currentUserId);
        tabbedPane.addTab("Design", designPanel);
        
        DesignListPanel designsPanel = new DesignListPanel(currentUserId, designPanel);
        tabbedPane.addTab("My Designs", designsPanel);
        
        add(tabbedPane);
        
        // Add menu bar with 3D view option
        setJMenuBar(createMenuBar());
        
        // Add 3D view button to toolbar
        add3DViewButton();
    }
    
    // Adding a button to display 3D View
    private void add3DViewButton() {
        JToolBar toolBar = new JToolBar();
        JButton view3DButton = new JButton("3D View");
        
        view3DButton.addActionListener(this::show3DView);
        toolBar.add(view3DButton);
        
        add(toolBar, BorderLayout.NORTH);
    }

    // Show 3D View on button click
    private void show3DView(ActionEvent e) {
        List<FurnitureItem> items = designPanel.getFurnitureItems();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please add furniture items first", 
                "No Items", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create in Event Dispatch Thread
            EventQueue.invokeLater(() -> {
                ThreeDViewFrame frame = new ThreeDViewFrame(items);
                frame.setVisible(true);
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "3D System Unavailable:\n" + ex.getMessage(),
                "Graphics Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Creating a menu bar with '3D View' option
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New Design");
        newItem.addActionListener(e -> designPanel.clearDesign());
        fileMenu.add(newItem);
        
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem("Exit"));
        
        JMenu viewMenu = new JMenu("View");
        JMenuItem view3DMenuItem = new JMenuItem("3D View");
        view3DMenuItem.addActionListener(this::show3DView);
        viewMenu.add(view3DMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }
    
    // Main Entry Point
    public static void main(String[] args) {
        // First, check if 3D environment is set up correctly
        if (ThreeDEnvironmentCheck.checkEnvironment()) {
            // Only launch the frame if the environment is ready
            SwingUtilities.invokeLater(() -> new MainFrame(1).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(null, "3D Environment not properly configured!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}