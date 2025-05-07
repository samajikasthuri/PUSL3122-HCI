package com.furnitureapp.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.furnitureapp.utils.DBUtil;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Furniture App - Login");
        setSize(600, 350);  // Increased the size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create the main panel with a background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(248, 248, 255));  // Lighter background for freshness

        // Title Panel (Optional) - You can add a logo or title here
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 51, 102));  // Dark blue header for contrast
        JLabel titleLabel = new JLabel("Login to Furniture App");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));  // Larger title font
        titlePanel.add(titleLabel);

        // Create a panel for centering the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));  // Use BoxLayout for centering
        formPanel.setBackground(new Color(255, 255, 255));  // White background for the form panel
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the form

        // Add username label and field with larger font
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(25);  // Increased the width of the text field
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));  // Larger text size for input fields
        usernamePanel.add(usernameField);
        formPanel.add(usernamePanel);

        // Add password label and field with larger font
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(25);  // Increased the width of the password field
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));  // Larger text size for input fields
        passwordPanel.add(passwordField);
        formPanel.add(passwordPanel);

        // Add the login button centered
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 204));  // Blue button for better visibility
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));  // Larger font for the button
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(150, 50));  // Larger button size
        loginButton.addActionListener(this::performLogin);
        buttonPanel.add(loginButton);
        formPanel.add(buttonPanel);

        // Add a footer error message panel (optional)
        JPanel errorPanel = new JPanel();
        errorPanel.setBackground(new Color(255, 255, 255));
        errorPanel.setLayout(new BorderLayout());
        errorPanel.setVisible(false);

        // Add everything to the main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(errorPanel, BorderLayout.SOUTH);

        // Add some padding around the frame
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                SwingUtilities.invokeLater(() -> {
                    new MainFrame(userId).setVisible(true);
                    this.dispose();
                });
            } else {
                showError("Invalid username or password");
            }
        } catch (Exception ex) {
            showError("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
