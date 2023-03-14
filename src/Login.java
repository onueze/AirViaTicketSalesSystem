import javax.swing.*;


public class Login extends javax.swing.JFrame {
    private JTextField airViaLtDTextField;
    private JTextField usernameText;
    private JLabel UsernameLabel;
    private JLabel PasswordLabel;
    private JButton loginButton;
    private JButton resetButton;
    private JPasswordField passwordField;


    public static void main(String[]args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login();
            }
        });
    }
}
