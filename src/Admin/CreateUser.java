package Admin;

import DB.DBConnectivity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private JPasswordField passwordField;
    private static int ID;
    private static String username;



    public CreateUser(int ID,String username) {

        this.username= username;
        this.ID= ID;
        setContentPane(createUserpage);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);




        firstNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });

        surnameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c)) {
                    e.consume();
                }
            }
        });



        phoneNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        emailAddressField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && c != '@' && c != '.' && c != '-' && c != '_') {
                    e.consume();
                }
            }
        });

        addressField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && c != ',' && c != '.' && c != '-' && c != '#') {
                    e.consume();
                }
            }
        });





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


                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must have a valid input.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");


                    String[] fields = {"email", "phoneNumber", "username"};
                    String[] values = {email, phoneNumber, username};
                    String[] fieldNames = {"Email", "Phone number", "Username"};

                    for (int i = 0; i < fields.length; i++) {
                        String checkQuery = "SELECT * FROM Employee WHERE " + fields[i] + " = ?";
                        PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                        checkStmt.setString(1, values[i]);
                        ResultSet resultSet = checkStmt.executeQuery();

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(null, fieldNames[i] + " already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }


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

