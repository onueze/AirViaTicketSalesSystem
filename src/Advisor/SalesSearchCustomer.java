package Advisor;

import DB.DBConnectivity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SalesSearchCustomer extends javax.swing.JFrame {
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField emailAddressTextField;
    private JTextField phoneNumberTextField;
    private JButton checkDetailsButton;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton requestConversionRateButton;
    private JButton homeButton;
    private JPanel mainPanel;
    private JPanel checkCustomerpanel;
    private JPanel payPanel;
    private JPanel discountPanel;
    private static int ID;
    private static String username;
    private int customerID;

    public SalesSearchCustomer(int ID, String username) {
        this.ID = ID;
        this.username = username;
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        checkDetailsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String email = emailAddressTextField.getText();
                String phoneNumber = phoneNumberTextField.getText();

                try(Connection con = DBConnectivity.getConnection()){
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT Customer_ID\n" +
                            "FROM CustomerAccount \n" +
                            "WHERE Firstname = '"+firstName+"' AND Surname = '"+lastName+"'\n" +
                            "AND Email = '"+email+"' AND PhoneNumber = '"+phoneNumber+"'";
                    System.out.println(query);
                    ResultSet rs = st.executeQuery(query);

                    System.out.println(requireText(firstNameTextField));
                    System.out.println(requireText(lastNameTextField));
                    System.out.println(requireText(emailAddressTextField));
                    System.out.println(requireText(phoneNumberTextField));


                    // check if all fields have text in them. If yes it evaluates the information
                    if(requireText(firstNameTextField) &&
                    requireText(lastNameTextField) &&
                    requireText(emailAddressTextField) &&
                    requireText(phoneNumberTextField)) {

                        if (!rs.next()) {
                            int dialogResult = JOptionPane.showConfirmDialog(mainPanel, "The Customer not was found./n" +
                                    " Do you want to continue to create a new customer/n" +
                                    "with this information?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // User clicked the "Yes" button
                                // Do something here
                                Statement newCustomer = con.createStatement();
                                String queryCustomer = "INSERT INTO CustomerAccount VALUES/n" +
                                        "('"+rs+"','"+firstName+"','"+lastName+"','"+email+"','"+phoneNumber+"',/n" +
                                        "'"+""+"','regular','null','January',0)";
                                System.out.println(query);
                                ResultSet rsCustomer = newCustomer.executeQuery(queryCustomer);

                                dispose();
                                SalesSellTicket salesSellTicket = new SalesSellTicket(ID,username,rs);
                                salesSellTicket.show();

                            } else {
                                // do nothing
                            }
                        } else {
                            int dialogResult = JOptionPane.showConfirmDialog(mainPanel, "The Customer was found./n" +
                                    " Do you want to continue to sell the ticket to this customer?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            if (dialogResult == JOptionPane.YES_OPTION) {
                                // User clicked the "Yes" button
                                // Do something here
                                dispose();
                                SalesSellTicket salesSellTicket = new SalesSellTicket(ID,username,rs);
                                salesSellTicket.show();

                            } else {
                                // do nothing
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(mainPanel,"Each field requires Text");
                    }
                    st.close();

                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }


                }

        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                advisorHome.show();

            }
        });
    }

    public boolean requireText(JTextField c){
        String text = c.getText();
        return !text.isEmpty();
    }


    public static void main(String[]args ){
        SalesSearchCustomer advisorSales = new SalesSearchCustomer(ID,username);
        advisorSales.show();
        advisorSales.setVisible(true);
    }
}
