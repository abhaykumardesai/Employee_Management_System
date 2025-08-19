import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {

    private static final long serialVersionUID = 1L;

    // A modern dark theme color palette
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color FOOTER_TEXT_COLOR = new Color(150, 150, 150);
    private static final Color TITLE_COLOR = Color.WHITE;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Home frame = new Home();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Home() {
        setTitle("EMS - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(30, 30, 15, 30)); // Adjusted bottom padding
        mainPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);

        // --- Title ---
        JLabel titleLabel = new JLabel("Employee Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(TITLE_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- Button Panel (still uses GridLayout for alignment) ---
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setOpaque(false); // Make it transparent

        // Create buttons
        JButton viewAllButton = createModernButton("View All Employees", "/icons/view_all.png");
        viewAllButton.addActionListener(e -> new ViewAllDetails().setVisible(true));

        JButton viewEmployeeButton = createModernButton("View Employee", "/icons/view_one.png");
        viewEmployeeButton.addActionListener(e -> new ViewEmployee().setVisible(true));

        JButton addEmployeeButton = createModernButton("Add Employee", "/icons/add_user.png");
        addEmployeeButton.addActionListener(e -> new AddEmp().setVisible(true));

        JButton deleteEmployeeButton = createModernButton("Delete Employee", "/icons/delete_user.png");
        deleteEmployeeButton.addActionListener(e -> new DeleteEmployee().setVisible(true));

        JButton updateEmployeeButton = createModernButton("Update Employee", "/icons/update_user.png");
        updateEmployeeButton.addActionListener(e -> new UpdateEmp().setVisible(true));

        JButton exitButton = createModernButton("Exit", "/icons/exit.png");
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the grid panel
        buttonPanel.add(viewAllButton);
        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(exitButton);

        // --- NEW: A wrapper panel to center the button grid ---
        // This panel uses GridBagLayout to hold the buttonPanel in the middle
        // without stretching it, which solves the "large buttons" problem.
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel); // Add the grid of buttons to the centering panel
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // --- NEW: Footer Panel ---
        JLabel footerLabel = new JLabel("Welcome to the Employee Management System. Please select an option to continue.", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(FOOTER_TEXT_COLOR);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
    }

    private JButton createModernButton(String text, String iconPath) {
        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource(iconPath));
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
        return new ModernButton(text, icon);
    }
}