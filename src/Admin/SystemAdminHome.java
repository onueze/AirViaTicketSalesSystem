package Admin;

import javax.swing.*;

public class SystemAdminHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton userDetailsButton;
    private JButton customerDetailsButton;
    private JButton commissionRatesButton;
    private JButton systemStockButton;
    private JButton restoreButton;
    private JButton backUpButton;
    private JButton createUserButton;
    private JButton refundsButton;
    private JButton logOutButton;
    private JPanel adminPage;

    public SystemAdminHome(){
        setContentPane(adminPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }




    public static void main(String[] args){
        SystemAdminHome adminHome = new  SystemAdminHome();
        adminHome.show();
    }
}
