package Admin;

import DB.DBConnectivity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateUser extends javax.swing.JFrame {
    private JButton button1;
    private JButton homeButton;
    private JButton manageUserDetailsButton;
    private JButton createUserButton;
    private JButton manageSystemStockButton;
    private JButton managerCustomerDetailsButton;
    private JPanel createUserpage;
    private JButton manageCommissionRatesButton;
    private JFormattedTextField firstNameField;
    private JFormattedTextField surnameField;
    private JFormattedTextField usernameField;
    private JComboBox roleComboBox;
    private JFormattedTextField phoneNumberField;
    private JFormattedTextField emailAddressField;
    private JButton submitCreationButton;
    private JFormattedTextField addressField;
    private JFormattedTextField passwordField;
    private static int ID;
    private static String username;




    public CreateUser(int ID,String username) {

        this.username= username;
        this.ID= ID;
        setContentPane(createUserpage);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        firstNameField = new JFormattedTextField();
        surnameField = new JFormattedTextField();
        usernameField = new JFormattedTextField();
        passwordField = new JFormattedTextField();

        String[] roles = {"Admin", "Office Manager", "Advisor"};
        roleComboBox = new JComboBox<>(roles);

        phoneNumberField = new JFormattedTextField();
        emailAddressField = new JFormattedTextField();
        addressField = new JFormattedTextField();


        submitCreationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstName = firstNameField.getText();
                System.out.println(firstName);
                String lastName = surnameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = (String) roleComboBox.getSelectedItem();
                String phoneNumber = phoneNumberField.getText();
                String email = emailAddressField.getText();
                String address = addressField.getText();

                //int Company_ID = 1;

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String query = "INSERT INTO Employee SELECT "+
                            "(SELECT COALESCE (MAX(Employee_ID),0)+1 FROM Employee),'"+firstName+"','"+lastName+"','"+username+"','"+password+"','"+role+"','"+phoneNumber+"','"+email+"','"+address+"','1' ";
                    PreparedStatement preparedStatement = con.prepareStatement(query);


                    preparedStatement.executeUpdate();

                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });



                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SystemAdminHome homeButton = new SystemAdminHome(ID, username);
                            homeButton.setVisible(true);
                            dispose();

                        }
                    });

                    manageUserDetailsButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            UserDetails manageUserDetailsButton = new UserDetails(ID, username);
                            manageUserDetailsButton.setVisible(true);
                            dispose();

                        }
                    });

                    managerCustomerDetailsButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CustomerDetails managerCustomerDetailsButton = new CustomerDetails(ID, username);
                            managerCustomerDetailsButton.setVisible(true);
                            dispose();


                        }
                    });
                    manageCommissionRatesButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            CommissionRates manageCommissionRatesButton = new CommissionRates(ID, username);
                            manageCommissionRatesButton.setVisible(true);
                            dispose();

                        }
                    });
                    manageSystemStockButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SystemStock manageSystemStockButton = new SystemStock(ID, username);
                            manageSystemStockButton.setVisible(true);
                            dispose();


                        }
                    });
                    createUserButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CreateUser createUserButton = new CreateUser(ID, username);
                            createUserButton.setVisible(true);
                            dispose();

                        }
                    });



            }

            public static void main(String[] args) {
                CreateUser createUser = new CreateUser(ID, username);
                createUser.show();
            }

        }

