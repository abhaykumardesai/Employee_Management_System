import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddEmp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtName, txtSalary, txtDept, txtPosition;

    // Consistent dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color FORM_BACKGROUND_COLOR = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);
    private static final Color SIDE_TEXT_COLOR = new Color(150, 150, 150);

    public AddEmp() {
        setTitle("Add New Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Opens the window in full screen

        JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- Title ---
        JLabel titleLabel = new JLabel("New Employee Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- Form Panel using GridBagLayout for alignment ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form components
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
        gbc.gridy = 0; formPanel.add(lblName, gbc);
        gbc.gridy = 1; formPanel.add(lblSalary, gbc);
        gbc.gridy = 2; formPanel.add(lblDept, gbc);
        gbc.gridy = 3; formPanel.add(lblPosition, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0; formPanel.add(txtName, gbc);
        gbc.gridy = 1; formPanel.add(txtSalary, gbc);
        gbc.gridy = 2; formPanel.add(txtDept, gbc);
        gbc.gridy = 3; formPanel.add(txtPosition, gbc);

        // A wrapper panel to protect the form from shrinking/growing
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        mainPanel.add(formWrapper, BorderLayout.CENTER);

        // Side panels
        JPanel westPanel = createSidePanel(
            "<html><body style='width: 150px; text-align: right;'>" +
            "<b>Instructions:</b><br><br>" +
            "Fill in all employee details accurately. The 'Salary' field must be a number." +
            "</body></html>"
        );
        mainPanel.add(westPanel, BorderLayout.WEST);
        
        JPanel eastPanel = createSidePanel(
            "<html><body style='width: 150px;'>" +
            "<i>\"Great things in business are never done by one person. They're done by a team of people.\"</i>" +
            "<br><br><b>- Steve Jobs</b>" +
            "</body></html>"
        );
        mainPanel.add(eastPanel, BorderLayout.EAST);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        JButton btnOnboard = createModernButton("Onboard Employee", "/icons/add_user.png");
        JButton btnBack = createModernButton("Back", "/icons/back.png");
        
        buttonPanel.add(btnBack);
        buttonPanel.add(btnOnboard);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // --- Action Listeners ---
        btnOnboard.addActionListener(e -> onBoardEmployee());
        btnBack.addActionListener(e -> dispose());
    }

    private void onBoardEmployee() {
        String name = txtName.getText().trim();
        String salaryStr = txtSalary.getText().trim();
        String dept = txtDept.getText().trim();
        String position = txtPosition.getText().trim();

        if (name.isEmpty() || salaryStr.isEmpty() || dept.isEmpty() || position.isEmpty()) {
            showStyledMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int salary;
        try {
            salary = Integer.parseInt(salaryStr);
        } catch (NumberFormatException e) {
            showStyledMessageDialog(this, "Salary must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845"; // Ensure your password is correct
        String sql = "INSERT INTO emsystem (Name, Salary, Department, Position) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, salary);
            pstmt.setString(3, dept);
            pstmt.setString(4, position);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showStyledMessageDialog(this, "Employee onboarded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtName.setText("");
                txtSalary.setText("");
                txtDept.setText("");
                txtPosition.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessageDialog(this, "A database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * NEW: Displays a JOptionPane with the custom dark theme.
     */
    private void showStyledMessageDialog(Component parent, String message, String title, int messageType) {
        // Customizing JOptionPane colors
        UIManager.put("OptionPane.background", new ColorUIResource(45, 45, 45));
        UIManager.put("Panel.background", new ColorUIResource(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(220, 220, 220));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0))); // Removes the focus border

        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(40);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(FORM_BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), new EmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    private JButton createModernButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            } else {
                System.err.println("Icon not found: " + iconPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModernButton(text, icon);
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
                AddEmp frame = new AddEmp();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}