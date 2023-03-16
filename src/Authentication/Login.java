package Authentication;

import Admin.SystemAdminHome;
import Advisor.TravelAdvisorHome;
import DB.DBConnectivity;
import Manager.OfficeManagerHome;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Login extends javax.swing.JFrame {
    public String role;
    private JTextField airViaLtDTextField;
    private JTextField usernameText;
    private JLabel UsernameLabel;
    private JLabel PasswordLabel;
    private JButton loginButton;
    private JButton resetButton;
    private JPasswordField passwordField;
    private JPanel loginBackground;
    private JPanel AirViaLogo;


    public Login() {
        // MAKES THE PAGE VISIBLE
        setContentPane(loginBackground);
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        // ACTION ON RESET BUTTON
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameText.setText("");
                passwordField.setText("");
            }
        });

        // ACTION ON LOGINBUTTON
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameText.getText();
                String password = String.valueOf(passwordField.getPassword());


                // TRY TO CONNECT TO DATABASE
                try(Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    PreparedStatement stm = con.prepareStatement("Select Employee_ID, role from Employee where password = ? and username = ?");
                    stm.setString(1, password);
                    stm.setString(2, username);
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        role = rs.getString("role");
                        switch (role) {
                            case "officeManager" -> {
                                dispose();
                                OfficeManagerHome officeHome = new OfficeManagerHome();
                                officeHome.setVisible(true);
                                officeHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            }
                            case "advisor" -> {
                                dispose();
                                TravelAdvisorHome advisorHome = new TravelAdvisorHome();
                                advisorHome.setVisible(true);
                                advisorHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            }
                            case "admin" -> {
                                dispose();
                                SystemAdminHome adminHome = new SystemAdminHome();
                                adminHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            }
                        }
                    }
                    else{
                        System.out.println("Login failed. Invalid username or password.");
                    }
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[]args){
        Login log = new Login();
        log.show();
    }
}
