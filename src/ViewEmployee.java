import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewEmployee extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField eIDField;
    private JToggleButton toggleClear;

    // Consistent dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color TABLE_BACKGROUND_COLOR = new Color(60, 63, 65);
    private static final Color TABLE_ALT_ROW_COLOR = new Color(70, 73, 75);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_SECONDARY_COLOR = new Color(100, 100, 100);

    public ViewEmployee() {
        setTitle("Search Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // --- NEW: Header container to hold both title and search controls ---
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        mainPanel.add(headerContainer, BorderLayout.NORTH);

        // --- Main Title (Consistent with other screens) ---
        JLabel titleLabel = new JLabel("Search Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        headerContainer.add(titleLabel, BorderLayout.NORTH);

        // --- Search Controls Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setOpaque(false);
        headerContainer.add(searchPanel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("Enter Employee ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setForeground(TEXT_COLOR);
        searchPanel.add(idLabel);

        eIDField = new JTextField();
        eIDField.setFont(new Font("Arial", Font.PLAIN, 16));
        eIDField.setPreferredSize(new Dimension(150, 35));
        eIDField.setBackground(TABLE_BACKGROUND_COLOR);
        eIDField.setForeground(TEXT_COLOR);
        eIDField.setCaretColor(Color.WHITE);
        eIDField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR), new EmptyBorder(5, 5, 5, 5)));
        searchPanel.add(eIDField);

        JButton searchButton = createModernButton("Search", "/icons/search.png");
        searchPanel.add(searchButton);

        toggleClear = new JToggleButton("Clear Results");
        styleToggleButton();
        toggleClear.addActionListener(e -> {
            if (toggleClear.isSelected()) {
                toggleClear.setText("Stack Results");
                toggleClear.setBackground(ACCENT_COLOR); // Blue to match theme
            } else {
                toggleClear.setText("Clear Results");
                toggleClear.setBackground(BUTTON_SECONDARY_COLOR); // Neutral grey
            }
        });
        searchPanel.add(toggleClear);

        // --- Table for Results ---
        JTable table = new StripedTable(new DefaultTableModel(new String[]{"ID", "Name", "Salary", "Department", "Position"}, 0));
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // --- Footer with Back Button ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        JButton backButton = createModernButton("Back", "/icons/back.png");
        backButton.addActionListener(e -> dispose());
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Action Listener for Search Button
        searchButton.addActionListener(e -> searchEmployee((DefaultTableModel) table.getModel()));
    }
    
    private void styleToggleButton() {
        toggleClear.setFont(new Font("Arial", Font.BOLD, 14));
        toggleClear.setPreferredSize(new Dimension(140, 35));
        toggleClear.setForeground(Color.WHITE);
        toggleClear.setBackground(BUTTON_SECONDARY_COLOR); // Default to grey
        toggleClear.setFocusPainted(false);
        toggleClear.setBorderPainted(false);
        toggleClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void searchEmployee(DefaultTableModel model) {
        String empId = eIDField.getText().trim();
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!toggleClear.isSelected()) {
            model.setRowCount(0);
        }

        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845"; // Ensure your password is correct
        String sql = "SELECT * FROM emsystem WHERE id = ?";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(empId));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"), rs.getString("name"),
                    rs.getString("salary"), rs.getString("department"),
                    rs.getString("position")
                });
            } else {
                JOptionPane.showMessageDialog(this, "No employee found with ID: " + empId, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "A database error occurred.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        eIDField.setText("");
    }

    private void styleTable(JTable table) {
        table.setForeground(TEXT_COLOR);
        table.setGridColor(new Color(85, 85, 85));
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(100, 40));
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
                ViewEmployee frame = new ViewEmployee();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}