import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewAllDetails extends JFrame {

    private static final long serialVersionUID = 1L;

    // A more dynamic dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color HEADER_GRADIENT_START = new Color(60, 63, 65);
    private static final Color HEADER_GRADIENT_END = new Color(45, 45, 45);
    private static final Color TABLE_BACKGROUND_COLOR = new Color(60, 63, 65);
    private static final Color TABLE_ALT_ROW_COLOR = new Color(70, 73, 75);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(70, 130, 180);

    private JLabel recordCountLabel;

    public ViewAllDetails() {
        setTitle("All Employee Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600); // Increased size for better spacing
        setLocationRelativeTo(null);

        // Main panel with a standard BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);

        // --- NEW: Gradient Header Panel ---
        JPanel headerPanel = new GradientPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Employee Roster");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIcon(new ImageIcon(getClass().getResource("/icons/view_all.png"))); // Using an existing icon
        titleLabel.setIconTextGap(15);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // --- Data Table with Zebra Striping ---
        String[] columnNames = {"ID", "Name", "Salary", "Department", "Position"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new StripedTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, ACCENT_COLOR));
        scrollPane.getViewport().setBackground(TABLE_BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- NEW: Enhanced Footer Panel ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton backButton = createModernButton("Back", "/icons/back.png");
        backButton.addActionListener(e -> dispose());
        footerPanel.add(backButton, BorderLayout.WEST);

        recordCountLabel = new JLabel("Total Records: 0");
        recordCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        recordCountLabel.setForeground(TEXT_COLOR);
        footerPanel.add(recordCountLabel, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Fetch data and update the record count
        populateTableData(model);
    }
    
    /**
     * Applies the custom dark theme styling to the JTable.
     */
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
        header.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
    }
    
    /**
     * Fetches data and populates the table, then updates the record count label.
     */
    private void populateTableData(DefaultTableModel model) {
        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/ems";
        String user = "root";
        String password = "9845"; // Ensure this password is correct
        String sql = "SELECT * FROM emsystem";

        try (Connection con = DriverManager.getConnection(dbUrl, user, password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        "₹" + String.format("%,.2f", rs.getDouble("salary")), // Formats salary nicely
                        rs.getString("department"),
                        rs.getString("position")
                });
            }
            // Update the record count label after data is loaded
            recordCountLabel.setText("Total Records: " + model.getRowCount());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load employee data from the database.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Helper method to create a ModernButton.
     */
    private JButton createModernButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(iconPath));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
        return new ModernButton(text, icon);
    }

    // --- Custom Inner Classes for Styling ---

    /**
     * A custom JTable that draws alternating row colors (zebra striping).
     */
    class StripedTable extends JTable {
        public StripedTable(DefaultTableModel model) {
            super(model);
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                c.setBackground(row % 2 == 0 ? TABLE_BACKGROUND_COLOR : TABLE_ALT_ROW_COLOR);
            }
            return c;
        }
    }

    /**
     * A custom JPanel that paints a vertical gradient background.
     */
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, HEADER_GRADIENT_START, 0, h, HEADER_GRADIENT_END);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ViewAllDetails frame = new ViewAllDetails();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}