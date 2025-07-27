import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class AddEmp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtName, txtSalary, txtDept, txtPosition;

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

    public AddEmp() {
        setTitle("Employee Onboarding");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null); // Center window

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHeading = new JLabel("Add New Employee");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeading.setForeground(new Color(30, 144, 255)); // Dodger blue
        lblHeading.setBounds(135, 11, 300, 40);
        contentPane.add(lblHeading);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        int labelX = 50;
        int fieldX = 180;
        int y = 70;
        int gap = 40;

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        lblName.setBounds(labelX, y, 100, 25);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setFont(fieldFont);
        txtName.setBounds(fieldX, y, 220, 28);
        contentPane.add(txtName);

        // Salary
        y += gap;
        JLabel lblSalary = new JLabel("Salary:");
        lblSalary.setFont(labelFont);
        lblSalary.setBounds(labelX, y, 100, 25);
        contentPane.add(lblSalary);

        txtSalary = new JTextField();
        txtSalary.setFont(fieldFont);
        txtSalary.setBounds(fieldX, y, 220, 28);
        contentPane.add(txtSalary);

        // Department
        y += gap;
        JLabel lblDept = new JLabel("Department:");
        lblDept.setFont(labelFont);
        lblDept.setBounds(labelX, y, 100, 25);
        contentPane.add(lblDept);

        txtDept = new JTextField();
        txtDept.setFont(fieldFont);
        txtDept.setBounds(fieldX, y, 220, 28);
        contentPane.add(txtDept);

        // Position
        y += gap;
        JLabel lblPosition = new JLabel("Position:");
        lblPosition.setFont(labelFont);
        lblPosition.setBounds(labelX, y, 100, 25);
        contentPane.add(lblPosition);

        txtPosition = new JTextField();
        txtPosition.setFont(fieldFont);
        txtPosition.setBounds(fieldX, y, 220, 28);
        contentPane.add(txtPosition);

        // Onboard Button
        JButton btnOnboard = new JButton("Onboard");
        btnOnboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOnboard.setBackground(new Color(0, 153, 76)); // green
        btnOnboard.setForeground(Color.WHITE);
        btnOnboard.setBounds(fieldX, y + 50, 110, 35);
        btnOnboard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOnboard.setFocusPainted(false);
        btnOnboard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        contentPane.add(btnOnboard);

        btnOnboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sql = "INSERT INTO emsystem (Name, Salary, Department, Position) VALUES (?, ?, ?, ?)";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "9845");

                    PreparedStatement ps = con.prepareStatement(sql);
                    String name = txtName.getText().trim();
                    int salary = Integer.parseInt(txtSalary.getText().trim());
                    String dept = txtDept.getText().trim();
                    String position = txtPosition.getText().trim();

                    ps.setString(1, name);
                    ps.setInt(2, salary);
                    ps.setString(3, dept);
                    ps.setString(4, position);

                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "✅ Employee inserted successfully!");
                        txtName.setText("");
                        txtSalary.setText("");
                        txtDept.setText("");
                        txtPosition.setText("");
                    }

                    ps.close();
                    con.close();
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "❌ Error: " + e2.getMessage());
                    e2.printStackTrace();
                }
            }
        });

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(70, 130, 180)); // steel blue
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setBounds(30, 320, 100, 30);
        contentPane.add(btnBack);

        btnBack.addActionListener(e -> {
            dispose();
        });
    }
}
