package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemStock extends javax.swing.JFrame{
    private JButton button1;
    private JButton createUserButton;
    private JButton homeButton;
    private JButton manageUserDetailsButton;
    private JButton manageSystemStockButton;
    private JButton manageCommissionRatesButton;
    private JButton manageCustomerDetailsButton;
    private JPanel systemStockPage;

    private static int ID;
    private static String username;




    public SystemStock(int ID,String username) {

        this.username= username;
        this.ID= ID;
        setContentPane(systemStockPage);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);



        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemAdminHome homeButton = new SystemAdminHome(ID,username);
                homeButton.setVisible(true);
                dispose();

            }
        });

        manageUserDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDetails manageUserDetailsButton = new UserDetails(ID,username);
                manageUserDetailsButton.setVisible(true);
                dispose();

            }
        });

        manageCustomerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDetails manageCustomerDetailsButton = new CustomerDetails(ID,username);
                manageCustomerDetailsButton.setVisible(true);
                dispose();


            }
        });
        manageCommissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CommissionRates manageCommissionRatesButton = new CommissionRates(ID,username);
                manageCommissionRatesButton.setVisible(true);
                dispose();

            }
        });
        manageSystemStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemStock manageSystemStockButton = new SystemStock(ID,username);
                manageSystemStockButton.setVisible(true);
                dispose();


            }
        });
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateUser createUserButton = new CreateUser(ID,username);
                createUserButton.setVisible(true);
                dispose();

            }
        });



    }

    public static void main(String[] args) {
        SystemStock systemStock = new SystemStock(ID,username);
        systemStock.show();
    }
}
