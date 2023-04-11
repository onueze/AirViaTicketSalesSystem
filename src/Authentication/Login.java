package Authentication;

import Admin.Home.SystemAdminHome;
import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;
import Manager.OfficeManagerHome;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Login extends javax.swing.JFrame {
    public String role;
    private JTextField usernameText;
    private JLabel UsernameLabel;
    private JLabel PasswordLabel;
    private JButton loginButton;
    private JButton resetButton;
    private JPasswordField passwordField;
    private JPanel loginBackground;

    private String username;
    private String password;
    private int ID;


    public Login() {
        // MAKES THE PAGE VISIBLEu
        setContentPane(loginBackground);
        setSize(1000,600);
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

                username = usernameText.getText();
                password = String.valueOf(passwordField.getPassword());


                // TRY TO CONNECT TO DATABASE
                try(Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    PreparedStatement stm = con.prepareStatement("Select Employee_ID, role from Employee where password = ? and username = ?");
                    stm.setString(1, password);
                    stm.setString(2, username);
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        role = rs.getString("role");
                        ID = rs.getInt("Employee_ID");
//                        switch (role) {
//                            case "officeManager" -> {
//                                dispose();
//                                OfficeManagerHome officeHome = new OfficeManagerHome(ID, username);
//                                officeHome.setVisible(true);
//                                officeHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//                            }
//                            case "advisor" -> {
//                                dispose();
//                                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID, username);
//                                advisorHome.setVisible(true);
//                                advisorHome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//                            }
//                            case "admin" -> {
//                                dispose();
//                                EnterDate enterDate = new EnterDate(ID,username);
//                                enterDate.setVisible(true);
//                                enterDate.show();
//                            }
//                        }
                        dispose();
                        TwoStepVerification twoStepVerification = new TwoStepVerification(ID,username);
                        twoStepVerification.show();
                    }
                    else{
                        JOptionPane.showMessageDialog(loginBackground,"Invalid password or username");
                    }
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public int getID() {
        return ID;
    }

    public static void main(String[]args){
        Login log = new Login();
        log.show();
    }
}
