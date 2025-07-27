import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ViewEmployee extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField eID;
	private JTable table;

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

	public ViewEmployee() {
		String dpath = "com.mysql.cj.jdbc.Driver";
		String URL = "jdbc:mysql://localhost:3306/ems";
		setTitle("All Employee Details");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 450);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// Heading
		JLabel lblHeading = new JLabel("Employee Details");
		lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblHeading.setBounds(260, 10, 300, 40);
		contentPane.add(lblHeading);

		// Employee ID label & text field
		JLabel lblEmpId = new JLabel("Employee ID:");
		lblEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblEmpId.setBounds(40, 60, 100, 25);
		contentPane.add(lblEmpId);

		eID = new JTextField();
		eID.setBounds(140, 60, 150, 25);
		contentPane.add(eID);
		
		

		// Search button
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(310, 60, 100, 25);
		btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setBackground(new Color(0, 102, 204));
		btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSearch.setFocusPainted(false);
		btnSearch.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));
		contentPane.add(btnSearch);
		
		
		
		// Toggle Switch Button
		JToggleButton toggleClear = new JToggleButton("Clear On");
		toggleClear.setBounds(430, 60, 100, 25);
		toggleClear.setFont(new Font("Segoe UI", Font.BOLD, 13));
		toggleClear.setForeground(Color.WHITE);
		toggleClear.setBackground(new Color(255, 153, 0)); // Orange
		toggleClear.setFocusPainted(false);
		toggleClear.setBorder(BorderFactory.createLineBorder(new Color(204, 102, 0), 2));
		toggleClear.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Toggle label on/off
		toggleClear.addActionListener(e -> {
		    if (toggleClear.isSelected()) {
		        toggleClear.setText("Clear Off");
		    } else {
		        toggleClear.setText("Clear On");
		    }
		});

		contentPane.add(toggleClear);

		

		// Back button
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(20, 360, 100, 30);
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(new Color(0, 102, 204));
		btnBack.setFocusPainted(false);
		btnBack.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));
		btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBack.addActionListener(e -> dispose());
		contentPane.add(btnBack);

		// Table
		String[] columns = { "ID", "Name", "Salary", "Department", "Position" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(24);
		table.setGridColor(Color.LIGHT_GRAY);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(0, 102, 204));
		table.getTableHeader().setForeground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 110, 690, 148);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
		contentPane.add(scrollPane);

		btnSearch.addActionListener(e -> {
		    String empId = eID.getText().trim();

		    if (empId.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "Please enter an Employee ID.");
		        return;
		    }

		    try {
		        int idToSearch = Integer.parseInt(empId);

		        Class.forName(dpath);
		        Connection con = DriverManager.getConnection(URL, "root", "9845");

		        String sql = "SELECT * FROM emsystem WHERE id = ?";
		        PreparedStatement pstmt = con.prepareStatement(sql);
		        pstmt.setInt(1, idToSearch);

		        ResultSet rs = pstmt.executeQuery();
		        
		        if (!toggleClear.isSelected()) {
		        	model.setRowCount(0);
		        }

		        boolean found = false;

		        while (rs.next()) {
		            int id = rs.getInt("Id");
		            String name = rs.getString("Name");
		            int salary = rs.getInt("Salary");
		            String department = rs.getString("Department");
		            String position = rs.getString("Position");

		            model.addRow(new Object[]{id, name, salary, department, position});
		            found = true;
		            eID.setText("");
		        }

		        if (!found) {
		            JOptionPane.showMessageDialog(this, "No employee found with ID: " + empId);
		        }

		        rs.close();
		        pstmt.close();
		        con.close();

		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a number.");
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage());
		    }
		});
	}
}
