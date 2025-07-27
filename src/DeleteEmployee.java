import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class DeleteEmployee extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtId;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DeleteEmployee frame = new DeleteEmployee();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DeleteEmployee() {
        setTitle("Employee Deboarding");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 550, 450);
        setLocationRelativeTo(null); // Center the frame

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Heading Label
        JLabel lblHeading = new JLabel("Delete Employee");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeading.setForeground(new Color(0, 102, 204));
        lblHeading.setBounds(150, 10, 300, 30);
        contentPane.add(lblHeading);

        // Label and TextField
        JLabel lblId = new JLabel("Employee ID:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblId.setBounds(50, 70, 120, 25);
        contentPane.add(lblId);

        txtId = new JTextField();
        txtId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtId.setBounds(180, 70, 137, 25);
        contentPane.add(txtId);
        
        
        String[] columns = { "ID", "Name", "Salary", "Department", "Position" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

        JButton btnOnboard = new JButton("Submit");
        btnOnboard.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//Fetch EMP DATA
        		String empId = txtId.getText().trim();

    		    if (empId.isEmpty()) {
    		        JOptionPane.showMessageDialog(null, "Please enter an Employee ID.");
    		        return;
    		    }

    		    try {
    		    	
    		        int idToSearch = Integer.parseInt(empId);
    		        Class.forName("com.mysql.cj.jdbc.Driver");
    		        String URL = "jdbc:mysql://localhost:3306/ems";

    		        Connection con = DriverManager.getConnection(URL, "root", "9845");

    		        String sql = "SELECT * FROM emsystem WHERE id = ?";
    		        PreparedStatement pstmt = con.prepareStatement(sql);
    		        pstmt.setInt(1, idToSearch);

    		        ResultSet rs = pstmt.executeQuery();
    		        model.setRowCount(0);
    		        boolean found = false;
    		        while (rs.next()) {
    		            int id = rs.getInt("Id");
    		            String name = rs.getString("Name");
    		            int salary = rs.getInt("Salary");
    		            String department = rs.getString("Department");
    		            String position = rs.getString("Position");

    		            model.addRow(new Object[]{id, name, salary, department, position});
    		            found = true;
    		        }

    		        if (!found) {
    		            JOptionPane.showMessageDialog(null, "No employee found with ID: " + empId);
    		        }

    		        rs.close();
    		        pstmt.close();
    		        con.close();

    		    } catch (NumberFormatException ex) {
    		        JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a number.");
    		    } catch (Exception ex) {
    		        ex.printStackTrace();
    		        JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
    		    }
        	}
        });
        btnOnboard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOnboard.setBackground(new Color(0, 153, 76));
        btnOnboard.setForeground(Color.WHITE);
        btnOnboard.setBounds(351, 67, 110, 30);
        btnOnboard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(btnOnboard);

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 120, 470, 160);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        contentPane.add(scrollPane);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(30, 320, 100, 30);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());
        contentPane.add(btnBack);

        JButton btnConformDeboarding = new JButton("Confirm Deboarding");
        btnConformDeboarding.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "9845");
					String query = "DELETE FROM emsystem WHERE id = ?";
					PreparedStatement pst = c.prepareStatement(query);
					int employeeId = Integer.parseInt(txtId.getText().trim());
					pst.setInt(1, employeeId); // Replace employeeId with your actual ID value
					int rowsAffected = pst.executeUpdate();
					if (rowsAffected > 0) {
					    JOptionPane.showMessageDialog(null, " ✅Employee deleted successfully!");
					    model.setRowCount(0);
					    txtId.setText("");
					} else {
					    JOptionPane.showMessageDialog(null, "No employee found with the given ID.");
					}
					c.close();
					pst.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
        		
        	}
        });
        btnConformDeboarding.setForeground(Color.WHITE);
        btnConformDeboarding.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConformDeboarding.setBackground(new Color(0, 153, 76));
        btnConformDeboarding.setBounds(331, 320, 179, 30);
        contentPane.add(btnConformDeboarding);
    }
}
