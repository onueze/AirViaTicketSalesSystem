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
                char[] password = passwordField.getPassword();


                try(Connection con = DBConnectivity.getConnection()) {
                    Statement stm = con.createStatement();
                    String sql = "Select ID, role from Staff where password = ? and username = ?";
                    ResultSet rs = stm.executeQuery(sql);
                    role = rs.getString("role");
                    switch(role){
                        case "officeManager":
                            dispose();
                            OfficeManagerHome officeHome = new OfficeManagerHome();
//                            officeHome.setVisible(true);
                            break;
                        case "advisor":
                            dispose();
                            TravelAdvisorHome advisorHome = new TravelAdvisorHome();
                            break;
                        case "admin":
                            dispose();
                            SystemAdminHome adminHome = new SystemAdminHome();
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
