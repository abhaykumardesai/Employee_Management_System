import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteEmployee extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtId;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnConfirmDelete;

    // Consistent dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color TABLE_BACKGROUND_COLOR = new Color(60, 63, 65);
    private static final Color TABLE_ALT_ROW_COLOR = new Color(70, 73, 75);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);
    // Colors for the destructive delete button
    private static final Color DESTRUCTIVE_COLOR_START = new Color(217, 83, 79); // Red
    private static final Color DESTRUCTIVE_COLOR_END = new Color(200, 60, 56);
    private static final Color DESTRUCTIVE_HOVER_START = new Color(230, 95, 91);
    private static final Color DESTRUCTIVE_HOVER_END = new Color(217, 83, 79);

    public DeleteEmployee() {
        setTitle("Delete Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- Header Panel for Title and Search ---
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        mainPanel.add(headerContainer, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Delete Employee Record");
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

        txtId = new JTextField(15);
        styleTextField(txtId);
        searchPanel.add(txtId);

        JButton btnSearch = createModernButton("Search", "/icons/search.png");
        searchPanel.add(btnSearch);
        
        // --- Table to display employee to be deleted ---
        model = new DefaultTableModel(new String[]{"ID", "Name", "Salary", "Department", "Position"}, 0);
        table = new StripedTable(model);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // --- Footer with Action Buttons ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnBack = createModernButton("Back", "/icons/back.png");
        footerPanel.add(btnBack, BorderLayout.WEST);

        btnConfirmDelete = new ModernButton("Confirm Delete", null, DESTRUCTIVE_COLOR_START, DESTRUCTIVE_COLOR_END, DESTRUCTIVE_HOVER_START, DESTRUCTIVE_HOVER_END);
        btnConfirmDelete.setEnabled(false); // Disabled until an employee is found
        footerPanel.add(btnConfirmDelete, BorderLayout.EAST);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnSearch.addActionListener(e -> searchEmployee());
        btnConfirmDelete.addActionListener(e -> deleteEmployee());
        btnBack.addActionListener(e -> dispose());
    }
    
    private void searchEmployee() {
        String empId = txtId.getText().trim();
        model.setRowCount(0); // Clear previous results
        btnConfirmDelete.setEnabled(false); // Disable delete button for new search

        if (empId.isEmpty()) {
            showStyledMessageDialog(this, "Please enter an Employee ID to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Database logic...
        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845";
        String sql = "SELECT * FROM emsystem WHERE id = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(empId));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"), rs.getString("name"), rs.getString("salary"),
                    rs.getString("department"), rs.getString("position")
                });
                btnConfirmDelete.setEnabled(true); // Enable delete button on success
            } else {
                showStyledMessageDialog(this, "No employee found with ID: " + empId, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            showStyledMessageDialog(this, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessageDialog(this, "A database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteEmployee() {
        // Safety Check: Confirm before deleting
        int choice = showStyledConfirmDialog(this, "Are you sure you want to permanently delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String empId = (String) model.getValueAt(0, 0); // Get ID from the table for safety
        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845";
        String sql = "DELETE FROM emsystem WHERE id = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(empId));
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showStyledMessageDialog(this, "Employee record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                model.setRowCount(0); // Clear table
                txtId.setText("");
                btnConfirmDelete.setEnabled(false); // Disable button after deletion
            } else {
                showStyledMessageDialog(this, "Could not delete employee. They may have already been removed.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessageDialog(this, "A database error occurred during deletion.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Helper Methods for Styling ---
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(TABLE_BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), new EmptyBorder(5, 5, 5, 5)));
    }
    
    private void styleTable(JTable tbl) {
        tbl.setForeground(TEXT_COLOR);
        tbl.setGridColor(new Color(85, 85, 85));
        tbl.setRowHeight(32);
        tbl.setFont(new Font("Arial", Font.PLAIN, 15));
        tbl.setSelectionBackground(ACCENT_COLOR);
        tbl.setSelectionForeground(Color.WHITE);
        JTableHeader header = tbl.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(100, 40));
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

    private int showStyledConfirmDialog(Component parent, String message, String title, int optionType, int messageType) {
        UIManager.put("OptionPane.background", new ColorUIResource(45, 45, 45));
        UIManager.put("Panel.background", new ColorUIResource(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(220, 220, 220));
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        return JOptionPane.showConfirmDialog(parent, message, title, optionType, messageType);
    }
    
    class StripedTable extends JTable {
        public StripedTable(DefaultTableModel model) { super(model); }
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                c.setBackground(row % 2 == 0 ? TABLE_BACKGROUND_COLOR : TABLE_ALT_ROW_COLOR);
            }
            return c;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new DeleteEmployee().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}