import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewAllDetails extends JFrame {

    private JPanel contentPane;

    public ViewAllDetails() {
        setTitle("All Employee Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblHeading = new JLabel("List of All Employees");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeading.setForeground(new Color(0, 102, 204));
        lblHeading.setBounds(220, 10, 210, 30);
        contentPane.add(lblHeading);

        // Table column names
        
        String dpath = "com.mysql.cj.jdbc.Driver";
    	String URL = "jdbc:mysql://localhost:3306/ems";
    	String sql = "SELECT * FROM emsystem";
    	ArrayList<Object[]> dataList = new ArrayList<>();

    	try {
    	    Class.forName(dpath);
    	    Connection con = DriverManager.getConnection(URL, "root", "9845");
    	    Statement ps = con.createStatement();
    	    ResultSet rs = ps.executeQuery(sql);
    	    
    	    while (rs.next()) {
                Object[] row = new Object[5]; // assuming 5 columns (ID, Name, Salary, Dept, Position)
                row[0] = rs.getString("id");
                row[1] = rs.getString("name");
                row[2] = rs.getString("salary");
                row[3] = rs.getString("department");
                row[4] = rs.getString("position");
                dataList.add(row);
            }


    	    rs.close();
    	    ps.close();
    	    con.close();
    	} catch (Exception e2) {
    	    e2.printStackTrace();  // <-- This is important for debugging
    	}
    	Object[][] data = new Object[dataList.size()][5];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        
        
        
        
        String[] columns = {"ID", "Name", "Salary", "Department", "Position"};
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 60, 620, 250);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));

        contentPane.add(scrollPane);

        
        JButton btnNewButton = new JButton("Back");
        btnNewButton.setBounds(10, 329, 100, 30);
        btnNewButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNewButton.setForeground(Color.WHITE); // text color
        btnNewButton.setBackground(new Color(0, 102, 204)); // blue background
        btnNewButton.setFocusPainted(false); // removes focus border
        btnNewButton.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153), 2)); // custom border
        btnNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // hand cursor on hover
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(e -> {
            dispose(); // Closes only the current JFrame
        });


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
