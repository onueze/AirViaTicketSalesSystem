package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateUser extends javax.swing.JFrame {
    private JButton button1;
    private JButton homeButton;
    private JButton manageUserDetailsButton;
    private JButton createUserButton;
    private JButton manageSystemStockButton;
    private JButton managerCustomerDetailsButton;
    private JPanel createUserpage;
    private JButton manageCommissionRatesButton;
    private static int ID;
    private static String username;


    public CreateUser(int ID,String username) {

        this.username= username;
        this.ID= ID;
        setContentPane(createUserpage);
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

        managerCustomerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDetails managerCustomerDetailsButton = new CustomerDetails(ID,username);
                managerCustomerDetailsButton.setVisible(true);
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
        CreateUser createUser = new CreateUser(ID,username);
        createUser.show();
    }

}
