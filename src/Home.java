import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Home() {
		setTitle("EMS - Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 439);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Employee Management System", JLabel.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblNewLabel.setForeground(new Color(25, 25, 112)); // Royal Blue
		lblNewLabel.setBounds(50, 20, 540, 40);
		contentPane.add(lblNewLabel);

		JButton btnNewButton = new JButton("View All Employees");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewAllDetails viewAll = new ViewAllDetails();
				viewAll.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton.setBounds(28, 110, 216, 50);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("View Employee");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewEmployee viewEmp = new ViewEmployee();
				viewEmp.setVisible(true);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton_1.setBounds(396, 110, 216, 50);
		contentPane.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Add Employee");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddEmp addEmp = new AddEmp();
				addEmp.setVisible(true);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton_2.setBounds(28, 211, 216, 50);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Delete Employee");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeleteEmployee delEmp = new DeleteEmployee();
				delEmp.setVisible(true);
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton_3.setBounds(396, 211, 216, 50);
		contentPane.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Update Employee");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateEmp upEmp = new UpdateEmp();
				upEmp.setVisible(true);
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnNewButton_4.setBounds(207, 290, 216, 50);
		contentPane.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("Exit");
		btnNewButton_5.setBounds(523, 361, 89, 30);
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(btnNewButton_5);

		btnNewButton_5.addActionListener(e -> System.exit(0));
	}
}
