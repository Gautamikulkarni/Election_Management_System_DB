package newpractice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Date;

public class Main extends JFrame {
	String voter_id;
	private JTextField usernameField;
	private JPasswordField passwordField;
	String epic_no,v_id,poll_name,poll_loc;
	String user,pass;
    int poll_id,ward;
	public Main() {
		Random random = new Random();
		int a1 = random.nextInt(100000, 999999); // Generate random 6 digits
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Only alphabets
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int index = random.nextInt(alphabets.length()); // Get random index
			result.append(alphabets.charAt(index)); // Append random letter
		}
		String b1 = result.toString();
		voter_id = b1.concat(String.valueOf(a1));
		String a = "MH/01/";

		int b = random.nextInt(100000, 999999);

		epic_no = a + String.valueOf(b);

		setTitle("Voter Registration system!");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
// Set layout and create main panel
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
// Username label and text field
		JLabel userLabel = new JLabel("Username:");
		userLabel.setBounds(50, 50, 100, 30);
		panel.add(userLabel);
		JTextField userText = new JTextField();
		userText.setBounds(150, 50, 150, 30);
		panel.add(userText);
// Password label and password field
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(50, 100, 100, 30);
		panel.add(passwordLabel);
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(150, 100, 150, 30);
		panel.add(passwordText);
// Login button
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(80, 150, 100, 30);
		panel.add(loginButton);
// Create New Account button
		JButton createAccountButton = new JButton("Create New Account");
		createAccountButton.setBounds(200, 150, 160, 30);
		panel.add(createAccountButton);
// Add action listener for login button
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				user = userText.getText();
				pass = new String(passwordText.getPassword());
// Here you can add actual authentication logic
				String sql = " select * from voter v inner join allotment a inner join polling_station p on v.voter_id=a.v_id AND p.p_id=a.p_id;";
				boolean loginSuccessful = false;
				try (Connection conn = DatabaseConnection.getConnection();
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery(sql)) {
					// Check each row for matching username and password
					while (rs.next()) {
						v_id=rs.getString("voter_id");
						String dbUsername = rs.getString("v_user");
						String dbPassword = rs.getString("v_pass");
						poll_id=rs.getInt("p_id");
						poll_name=rs.getString("p_name");
						poll_loc=rs.getString("p_loc");
						ward=rs.getInt("p_ward");
						if (user.equals(dbUsername) && pass.equals(dbPassword)) {
							//user=username;
							//pass=password;
							loginSuccessful = true;
							break; // Exit loop once a match is found
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				// Perform actions based on login success
				if (loginSuccessful) {
					JOptionPane.showMessageDialog(null, "Welcome to the Main Panel!");
					showInsideMenuPage();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid credentials", "Login Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

// Add action listener for create account button
		createAccountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCreateAccountPage();
			}
		});
		add(panel);
	}

// Method to show the inside menu page after successful login
	private void showInsideMenuPage() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 3, 12, 12));
		JFrame MenuFrame = new JFrame("Menu Page");
		MenuFrame.setSize(400, 400);
		MenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MenuFrame.setLocationRelativeTo(null);
		// welcome label
		JLabel welcomeLabel = new JLabel("Welcome to Voter registration System!!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(welcomeLabel);
		// first button
		JButton bvoter = new JButton("Voter Details");
		bvoter.setPreferredSize(new Dimension(180, 40));
		panel.add(bvoter);
		// second button
		JButton ps = new JButton("Polling Station Details");
		ps.setPreferredSize(new Dimension(180, 40));
		panel.add(ps);
		// third button
		JButton ele = new JButton("Election Schedule");
		ele.setPreferredSize(new Dimension(180, 40));
		panel.add(ele);
		JButton cc = new JButton("Contesting Candidates");
		cc.setPreferredSize(new Dimension(180, 40));
		panel.add(cc);
		JButton Id = new JButton("Incharge Duties");
		Id.setPreferredSize(new Dimension(180, 40));
		panel.add(Id);
		// all panels added to menuFrame
		MenuFrame.add(panel);

		bvoter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showVoterRegistrationDetails();
			}
		});
		ps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pollSd() ;
			}
		});
		ele.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				EleSch();
			}
		});
		cc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contestingcandi();
			}
		});
		Id.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Incharge();
			}
		});

		MenuFrame.setVisible(true);
	}

	void pollSd() {
		JFrame frame = new JFrame("Polling Station Details");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 300);

		frame.setLocationRelativeTo(null);

		JLabel pol = new JLabel("Polling Station Details", JLabel.CENTER);

		frame.add(pol);

		DefaultTableModel pollS = new DefaultTableModel();

		pollS.addColumn("P_id");

		pollS.addColumn("P_Location");

		pollS.addColumn("P_Name");

		pollS.addColumn("P_Ward");

		JTable polling = new JTable(pollS);

		JScrollPane scrollPane = new JScrollPane(polling);

		frame.add(scrollPane, BorderLayout.CENTER);

		fetchDataforPolling(pollS);

		frame.setLocationRelativeTo(null);

		// Set frame visibility

		frame.setVisible(true);
	}

	private void fetchDataforPolling(DefaultTableModel pollS) {
		String sql = "Select * from polling_Station";

		try {Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery(sql);
			while(rs.next()) {
				int pid=rs.getInt("p_id");
				String  loc=rs.getString("p_loc");
				String  nam=rs.getString("p_name");
				int ward=rs.getInt("p_ward");
				pollS.addRow(new Object[] {pid,loc,nam,ward});
			}
			rs.close();
		    pstmt.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in Displying the Details of Polling Stations !");
		}
		
		

	}

	void EleSch() {

		JFrame frame = new JFrame("Election Schedule");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 300);

		frame.setLocationRelativeTo(null);

		JLabel ele = new JLabel("Election Schedule", JLabel.CENTER);

		frame.add(ele);

		DefaultTableModel elesc = new DefaultTableModel();

		elesc.addColumn("E_Date");

		elesc.addColumn("E_Type");

		elesc.addColumn("E_Location");

		JTable eleschl = new JTable(elesc);

		JScrollPane scrollPane = new JScrollPane(eleschl);

		frame.add(scrollPane, BorderLayout.CENTER);

		fetchDataforEleSch(elesc);

		frame.setLocationRelativeTo(null);

		// Set frame visibility

		frame.setVisible(true);

	}

	private void fetchDataforEleSch(DefaultTableModel elesc) {
		String sql = "Select * from elect_Schedule";

		try {Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery(sql);
			while(rs.next()) {
				Date date=rs.getDate("E_date");
				String  type=rs.getString("E_type");
				String  loca=rs.getString("E_location");
				
				elesc.addRow(new Object[] {date,type,loca});
			}
			rs.close();
		    pstmt.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in Displying the Details of Election Schedule !");
		}
		
		

		

	}

	void contestingcandi() {

		JFrame frame = new JFrame("Contesting Candidates");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 300);

		frame.setLocationRelativeTo(null);

		JLabel conc = new JLabel("Contesting Candidates", JLabel.CENTER);

		frame.add(conc);

		DefaultTableModel candidates = new DefaultTableModel();

		candidates.addColumn("C_Id");

		candidates.addColumn("C_Name");

		candidates.addColumn("Party");

		candidates.addColumn("Sector");

		candidates.addColumn("Ward");

		JTable cc = new JTable(candidates);

		JScrollPane scrollPane = new JScrollPane(cc);

		frame.add(scrollPane, BorderLayout.CENTER);

		fetchDataforcontcand(candidates);

		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

	}

	private void fetchDataforcontcand(DefaultTableModel candidates) {

		String sql = "Select * from candidates";

		try {Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs=pstmt.executeQuery(sql);
			while(rs.next()) {
				int cand=rs.getInt("cand_id");
				String name=rs.getString("cand_name");
				String  par=rs.getString("party");
				String  sect=rs.getString("sector");
				int  w=rs.getInt("ward_no");
				
				 candidates.addRow(new Object[] {cand,name,par,sect,w});
			}
			rs.close();
		    pstmt.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in Displying the Details of Contesting Candidates !");
		}
		
		
	}

	void Incharge() {

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(4, 1, 10, 10));

		JFrame voterFrame = new JFrame("Inchage Section!!");

		voterFrame.setSize(400, 400);

		voterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		voterFrame.setLocationRelativeTo(null);

		voterFrame.setLayout(new FlowLayout());

		JLabel label = new JLabel("Wel Come to Inchage Section!!");

		label.setFont(new Font("Arial", Font.BOLD, 16));

		label.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(label);

		JLabel Inchargeid = new JLabel(" Enter your Incharge Id : ");

		label.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(Inchargeid);

		JPasswordField inchidtext = new JPasswordField();

		panel.add(inchidtext);

		JButton Submit = new JButton("Sumbit");

		panel.add(Submit);

		voterFrame.add(panel);
		
		Submit.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {
				int in_id = 0,p_id=0,ward=0;
				String p_loc=null,p_name = null;
				String Inchage_id = inchidtext.getText();

				// Here you can add actual authentication logic
				String sql = "SELECT * FROM incharge i inner join polling_station p on i.p_id=p.p_id";
				boolean loginSuccessful = false;
				try (Connection conn = DatabaseConnection.getConnection();
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery(sql)) {
					// Check each row for matching username and password
					while (rs.next()) {
						int ID = rs.getInt("i_id");
						p_id=rs.getInt("p_id");
						 p_loc=rs.getString("p_loc");
						p_name=rs.getString("p_name");
					 ward=rs.getInt("p_ward");
						if (Integer.parseInt(Inchage_id)==ID) {
							in_id=ID;
							loginSuccessful = true;
							 
							break; // Exit loop once a match is found
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				// Perform actions based on login success
				if (loginSuccessful) {
					JOptionPane.showMessageDialog(null, "Sumbitted Successfully!!");
					inchdetails(in_id,p_id,p_loc,p_name,ward);
				} else {
					JOptionPane.showMessageDialog(null, "Invalid Incharge Id! Try Again!", "Login Error",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		voterFrame.setVisible(true);
	}

	void inchdetails(int I_id,int p_id,String p_loc,String p_name,int ward) {

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(6, 3, 12, 12));

		JFrame Frame = new JFrame("Inchage Section!!");

		Frame.setSize(400, 200);

		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Frame.setLocationRelativeTo(null);

		Frame.setLayout(new FlowLayout());
		// String sql="select i.i_name,p.p_id,p.p_loc,p.p_name from incharge i inner join polling_station p on p.p_id=i.p_id where i.i_id=";
          
		JLabel label = new JLabel("** Polling Station Details **");

		label.setFont(new Font("Arial", Font.BOLD, 16));

		label.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(label);

		JLabel pid = new JLabel("Polling Station Id : " + p_id, JLabel.CENTER);

		pid.setBounds(50, 100, 100, 30);

		panel.add(pid);
     
		JLabel pnam = new JLabel("Polling Station Name : " + p_name, JLabel.CENTER);

		pnam.setBounds(50, 100, 100, 30);

		panel.add(pnam);
		
		JLabel plo = new JLabel("Polling Station Location : " + p_loc, JLabel.CENTER);

		plo.setBounds(50, 100, 100, 30);

		panel.add(plo);

		JLabel pw = new JLabel("Polling Station Ward : " + ward, JLabel.CENTER);

		pw.setBounds(50, 100, 100, 30);

		panel.add(pw);

		Frame.add(panel);

		Frame.setVisible(true);

	}

	private void showVoterRegistrationDetails() {

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(4, 1, 10, 10));

		JFrame voterFrame = new JFrame("Voter Details");

		voterFrame.setSize(400, 200);

		voterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		voterFrame.setLocationRelativeTo(null);

		JLabel label = new JLabel("Voter Registration Details Page!!");

		label.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(label);

		JLabel vot_id = new JLabel("Voter Id : " + v_id, JLabel.CENTER);

		vot_id.setBounds(50, 100, 100, 30);

		panel.add(vot_id);

		JLabel En = new JLabel("Epic No. : " + epic_no, JLabel.CENTER);

		En.setBounds(50, 100, 100, 30);

		panel.add(En);

		JButton in = new JButton("See your Polling Station");

		in.setBounds(80, 150, 100, 30);

		panel.add(in);

		in.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			}
		});

		voterFrame.add(panel);

		in.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seePolS();
			}
		});
		voterFrame.setVisible(true);
	}

	void seePolS() {

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(5, 1, 10, 10));

		JFrame fm = new JFrame("*See your Polling Station Details:- ");

		fm.setSize(400, 400);

		fm.setDefaultCloseOperation(EXIT_ON_CLOSE);

		fm.setLocationRelativeTo(null);
		JLabel pn1 = new JLabel("1.Polling Station Id : " +poll_id, JLabel.CENTER);

		panel.add(pn1);

		JLabel pn = new JLabel("2.Polling Station Name : " +poll_name, JLabel.CENTER);

		panel.add(pn);

		JLabel pl = new JLabel("3.Polling Station Location : " + poll_loc, JLabel.CENTER);

		panel.add(pl);

		JLabel w = new JLabel("4.Ward No. :" + ward, JLabel.CENTER);

		panel.add(w);

		fm.add(panel);

		fm.setVisible(true);

	}

// Method to show the create account page

	private void showCreateAccountPage() {

		JFrame createAccountFrame = new JFrame("Create New Account");

		createAccountFrame.setSize(500, 500);

		createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		createAccountFrame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(12, 12, 4, 12));

		JLabel lab = new JLabel("Enter the following details!!");

		lab.setAlignmentY(CENTER_ALIGNMENT);

		panel.add(lab);

		panel.add(new JLabel(""));

// Adding fields to capture user information

		panel.add(new JLabel("Name:"));

		JTextField nameField = new JTextField();

		panel.add(nameField);

		panel.add(new JLabel("Age:"));

		JTextField ageField = new JTextField();

		panel.add(ageField);

		panel.add(new JLabel("DOB(YYYY-MM-DD):"));

		JTextField DOBField = new JTextField();

		panel.add(DOBField);

		panel.add(new JLabel("Gender:"));

		JTextField gField = new JTextField();

		panel.add(gField);

		panel.add(new JLabel("Phone:"));

		JTextField phoneField = new JTextField();

		panel.add(phoneField);
		panel.add(new JLabel("Aadhar Card No.:"));
		JTextField adharc = new JTextField();
		panel.add(adharc);

		panel.add(new JLabel("Email:"));

		JTextField emailField = new JTextField();

		panel.add(emailField);

		panel.add(new JLabel("Address(City):"));

		JTextField addressField = new JTextField();

		panel.add(addressField);

		panel.add(new JLabel("Set your Username: "));
		JTextField usernameField = new JTextField();
		panel.add(usernameField);

		panel.add(new JLabel("Set your Password:"));

		JPasswordField passwordField = new JPasswordField();

		panel.add(passwordField);

		panel.add(new JLabel(""));

		JButton createAccountButton = new JButton("Create Account");

		panel.add(createAccountButton);

		createAccountFrame.add(panel);

		createAccountButton.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {

// Capture the data entered (you can add code here to save it in a database)

				String name = nameField.getText();
				String age = ageField.getText();
				String DOB = DOBField.getText();
				String gender = gField.getText();
				String phone = phoneField.getText();
				String Adhar = adharc.getText();
				String email = emailField.getText();
				String address = addressField.getText();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				saveVoterDetails(name, address, DOB, phone, username, password, gender, Adhar);
				JOptionPane.showMessageDialog(createAccountFrame, "Account Created Successfully!");
    createAccountFrame.dispose();
			}
		});
		createAccountFrame.setVisible(true);
	}

	private void saveVoterDetails(String voter_name, String v_city, String v_dob, String mobno,
			String user, String pass, String v_gender, String v_aadhar) {
		String sql = "INSERT INTO voter(voter_id,voter_name, voter_city, voter_dob, mob, v_user, v_pass, gender, voter_aadhar) " +
	             "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";


		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
		
			pstmt.setString(1, voter_id);
			pstmt.setString(2, voter_name);
			pstmt.setString(3, v_city);
			pstmt.setString(4, v_dob);
			pstmt.setString(5, mobno);
			pstmt.setString(6, user);
			pstmt.setString(7, pass);
			pstmt.setString(8, v_gender);
			pstmt.setString(9, v_aadhar);
            pstmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error in saving voter details!");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main frame = new Main();
			frame.setVisible(true);
		});
	}
}

class DatabaseConnection {
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/voter_helpline?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "Gauta@2005";
	static {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("MySQL JDBC Driver not found!!");
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}

