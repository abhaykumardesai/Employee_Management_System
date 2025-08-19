import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateEmp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtId, txtName, txtSalary, txtDept, txtPosition;
    private JTextField searchField;
    private JButton btnSaveChanges;

    // Consistent dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color FORM_BACKGROUND_COLOR = new Color(60, 63, 65);
    private static final Color READ_ONLY_BG_COLOR = new Color(50, 50, 50);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);
    private static final Color SIDE_TEXT_COLOR = new Color(150, 150, 150);

    public UpdateEmp() {
        setTitle("Update Employee Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // This line makes the window open maximized to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- Header Panel for Title and Search ---
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        mainPanel.add(headerContainer, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Update Employee Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setOpaque(false);
        headerContainer.add(searchPanel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("Enter Employee ID to Find:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setForeground(TEXT_COLOR);
        searchPanel.add(idLabel);

        searchField = new JTextField(15);
        styleTextField(searchField);
        searchPanel.add(searchField);

        JButton btnSearch = createModernButton("Find Employee", "/icons/search.png");
        searchPanel.add(btnSearch);

        // --- Form Panel for displaying and editing details ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form components
        JLabel lblId = createStyledLabel("ID:");
        txtId = createStyledTextField();
        txtId.setEditable(false);
        txtId.setBackground(READ_ONLY_BG_COLOR);

        JLabel lblName = createStyledLabel("Name:");
        txtName = createStyledTextField();
        JLabel lblSalary = createStyledLabel("Salary:");
        txtSalary = createStyledTextField();
        JLabel lblDept = createStyledLabel("Department:");
        txtDept = createStyledTextField();
        JLabel lblPosition = createStyledLabel("Position:");
        txtPosition = createStyledTextField();

        // Add components to form grid
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridy = 2; formPanel.add(lblSalary, gbc);
        gbc.gridy = 3; formPanel.add(lblDept, gbc);
        gbc.gridy = 4; formPanel.add(lblPosition, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0; formPanel.add(txtId, gbc);
        gbc.gridy = 1; formPanel.add(txtName, gbc);
        gbc.gridy = 2; formPanel.add(txtSalary, gbc);
        gbc.gridy = 3; formPanel.add(txtDept, gbc);
        gbc.gridy = 4; formPanel.add(txtPosition, gbc);

        // A wrapper panel to protect the form from shrinking/growing
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        mainPanel.add(formWrapper, BorderLayout.CENTER);
        
        // --- Side panels to make the layout responsive ---
        JPanel westPanel = createSidePanel(
            "<html><body style='width: 150px; text-align: right;'>" +
            "<b>How to Update:</b><br><br>" +
            "1. Find an employee using their ID.<br><br>" +
            "2. Edit the details in the form.<br><br>" +
            "3. Click 'Save Changes' to update the record." +
            "</body></html>"
        );
        mainPanel.add(westPanel, BorderLayout.WEST);
        
        JPanel eastPanel = createSidePanel(
            "<html><body style='width: 150px;'>" +
            "<i>\"The secret of change is to focus all of your energy not on fighting the old, but on building the new.\"</i>" +
            "<br><br><b>- Socrates</b>" +
            "</body></html>"
        );
        mainPanel.add(eastPanel, BorderLayout.EAST);
        
        // --- Footer with Action Buttons ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnBack = createModernButton("Back", "/icons/back.png");
        footerPanel.add(btnBack, BorderLayout.WEST);

        btnSaveChanges = createModernButton("Save Changes", "/icons/update_user.png");
        footerPanel.add(btnSaveChanges, BorderLayout.EAST);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Initially, disable the form fields and save button
        setFieldsEnabled(false);

        // --- Action Listeners ---
        btnSearch.addActionListener(e -> findEmployee());
        btnSaveChanges.addActionListener(e -> updateEmployeeData());
        btnBack.addActionListener(e -> dispose());
    }
    
    private void findEmployee() {
        String empId = searchField.getText().trim();
        if (empId.isEmpty()) {
            showStyledMessageDialog(this, "Please enter an Employee ID to find.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845";
        String sql = "SELECT * FROM emsystem WHERE id = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(empId));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtId.setText(rs.getString("id"));
                txtName.setText(rs.getString("name"));
                txtSalary.setText(rs.getString("salary"));
                txtDept.setText(rs.getString("department"));
                txtPosition.setText(rs.getString("position"));
                setFieldsEnabled(true); // Enable form on success
            } else {
                showStyledMessageDialog(this, "No employee found with ID: " + empId, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                setFieldsEnabled(false); // Keep form disabled
            }
        } catch (NumberFormatException ex) {
            showStyledMessageDialog(this, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessageDialog(this, "A database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateEmployeeData() {
        String idStr = txtId.getText();
        String name = txtName.getText().trim();
        String salaryStr = txtSalary.getText().trim();
        String dept = txtDept.getText().trim();
        String position = txtPosition.getText().trim();

        if (name.isEmpty() || salaryStr.isEmpty() || dept.isEmpty() || position.isEmpty()) {
            showStyledMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845";
        String sql = "UPDATE emsystem SET name = ?, salary = ?, department = ?, position = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(salaryStr));
            pstmt.setString(3, dept);
            pstmt.setString(4, position);
            pstmt.setInt(5, Integer.parseInt(idStr));
            
            if (pstmt.executeUpdate() > 0) {
                showStyledMessageDialog(this, "Employee details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showStyledMessageDialog(this, "Could not update details. Employee may no longer exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            showStyledMessageDialog(this, "Salary must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessageDialog(this, "A database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        txtName.setEnabled(enabled);
        txtSalary.setEnabled(enabled);
        txtDept.setEnabled(enabled);
        txtPosition.setEnabled(enabled);
        btnSaveChanges.setEnabled(enabled);
    }
    
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtSalary.setText("");
        txtDept.setText("");
        txtPosition.setText("");
    }

    // --- Helper Methods for Styling ---
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(FORM_BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), new EmptyBorder(5, 5, 5, 5)));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        styleTextField(textField);
        return textField;
    }

    private JButton createModernButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) icon = new ImageIcon(imgURL);
            else System.err.println("Icon not found: " + iconPath);
        } catch (Exception e) { e.printStackTrace(); }
        return new ModernButton(text, icon);
    }

    private void showStyledMessageDialog(Component parent, String message, String title, int messageType) {
        UIManager.put("OptionPane.background", new ColorUIResource(45, 45, 45));
        UIManager.put("Panel.background", new ColorUIResource(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(220, 220, 220));
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
    
    private JPanel createSidePanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel label = new JLabel(text);
        label.setForeground(SIDE_TEXT_COLOR);
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setVerticalAlignment(JLabel.TOP);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new UpdateEmp().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}