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
        setContentPane(loginBackground);
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameText.setText("");
                passwordField.setText("");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameText.getText();
                String password = String.valueOf(passwordField.getPassword());


                try(Connection con = DBConnectivity.getConnection()) {
                    PreparedStatement stm = con.prepareStatement("Select Employee_ID, role from Staff where password = ? and username = ?");
                    stm.setString(1,password);
                    stm.setString(1,username);
                    ResultSet rs = stm.executeQuery();
                    role = rs.getString("role");
                    switch(role){
                        case "officeManager":
                            dispose();
                            OfficeManagerHome officeHome = new OfficeManagerHome();
                            officeHome.setVisible(true);
                            officeHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            break;
                        case "advisor":
                            dispose();
                            TravelAdvisorHome advisorHome = new TravelAdvisorHome();
                            advisorHome.setVisible(true);
                            advisorHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            break;
                        case "admin":
                            dispose();
                            SystemAdminHome adminHome = new SystemAdminHome();
                            adminHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            break;
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
