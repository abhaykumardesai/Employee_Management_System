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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class UpdateEmp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtId, txtName, txtSalary, txtDept, txtPosition;
	private JTextField searchField;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UpdateEmp frame = new UpdateEmp();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public UpdateEmp() {
		setTitle("Update Employee");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 420);
		setLocationRelativeTo(null); // Center the frame

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblHeading = new JLabel("Update Employee");
		lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblHeading.setForeground(new Color(0, 102, 204));
		lblHeading.setBounds(140, 10, 250, 30);
		contentPane.add(lblHeading);

		Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
		Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

		// Search ID field
		JLabel lblSearchId = new JLabel("Employee ID:");
		lblSearchId.setFont(labelFont);
		lblSearchId.setBounds(52, 60, 120, 25);
		contentPane.add(lblSearchId);

		searchField = new JTextField();
		searchField.setFont(fieldFont);
		searchField.setBounds(182, 60, 154, 25);
		contentPane.add(searchField);

		

		// ID field
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(labelFont);
		lblId.setBounds(52, 100, 120, 25);
		contentPane.add(lblId);

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setFont(fieldFont);
		txtId.setBounds(182, 100, 154, 25);
		contentPane.add(txtId);

		// Name field
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(labelFont);
		lblName.setBounds(52, 140, 120, 25);
		contentPane.add(lblName);

		txtName = new JTextField();
		txtName.setFont(fieldFont);
		txtName.setBounds(182, 140, 154, 25);
		contentPane.add(txtName);

		// Salary field
		JLabel lblSalary = new JLabel("Salary:");
		lblSalary.setFont(labelFont);
		lblSalary.setBounds(52, 180, 120, 25);
		contentPane.add(lblSalary);

		txtSalary = new JTextField();
		txtSalary.setFont(fieldFont);
		txtSalary.setBounds(182, 180, 154, 25);
		contentPane.add(txtSalary);

		// Department field
		JLabel lblDept = new JLabel("Department:");
		lblDept.setFont(labelFont);
		lblDept.setBounds(52, 220, 120, 25);
		contentPane.add(lblDept);

		txtDept = new JTextField();
		txtDept.setFont(fieldFont);
		txtDept.setBounds(182, 220, 154, 25);
		contentPane.add(txtDept);

		// Position field
		JLabel lblPosition = new JLabel("Position:");
		lblPosition.setFont(labelFont);
		lblPosition.setBounds(52, 260, 120, 25);
		contentPane.add(lblPosition);

		txtPosition = new JTextField();
		txtPosition.setFont(fieldFont);
		txtPosition.setBounds(182, 260, 154, 25);
		contentPane.add(txtPosition);
		
		
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		//Fetch EMP DATA
        		String empId = searchField.getText().trim();

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
    		        boolean found = false;
    		        while (rs.next()) {
    		            int id = rs.getInt("Id");
    		            String name = rs.getString("Name");
    		            int salary = rs.getInt("Salary");
    		            String department = rs.getString("Department");
    		            String position = rs.getString("Position");
    		            txtId.setText("" + id);
    		            txtName.setText(name);
    		            txtSalary.setText("" + salary);
    		            txtDept.setText(department);
    		            txtPosition.setText(position);
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
		btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnSubmit.setBackground(new Color(0, 153, 76));
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBounds(366, 60, 100, 30);
		btnSubmit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnSubmit);
		
		
		

		// Update Button
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "UPDATE emsystem SET Name = ?, Salary = ?, Department = ?, Position = ? WHERE id = ?";

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "9845");
					PreparedStatement pst = con.prepareStatement(sql);
					int id = Integer.parseInt(txtId.getText().trim());
					String name = txtName.getText().trim();
					int salary = Integer.parseInt(txtSalary.getText().trim());
					String dept = txtDept.getText().trim();
					String position = txtPosition.getText().trim();

					pst.setString(1, name);
					pst.setInt(2, salary);
					pst.setString(3, dept);
					pst.setString(4, position);
					pst.setInt(5, id);

					int rows = pst.executeUpdate();
					if (rows > 0) {
						JOptionPane.showMessageDialog(null, "Employee updated successfully!");
					} else {
						JOptionPane.showMessageDialog(null, "No employee found with the given ID.");
					}

				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		});
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnUpdate.setBackground(new Color(0, 153, 76));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBounds(182, 310, 110, 30);
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnUpdate);

		// Back Button
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(new Color(0, 102, 204));
		btnBack.setFocusPainted(false);
		btnBack.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2));
		btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBack.setBounds(10, 330, 100, 30);
		contentPane.add(btnBack);

		// Back Action
		btnBack.addActionListener(e -> dispose());
	}
}
