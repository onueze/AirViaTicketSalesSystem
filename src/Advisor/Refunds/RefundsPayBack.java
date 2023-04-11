package Advisor.Refunds;

import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;
import SMTP.Mail;
import com.itextpdf.text.*;
import SMTP.Log;


import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RefundsPayBack extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton processRefundButton;
    private JLabel cardNumberText;
    private JButton checkButton;
    private JButton cancelButton;
    private JLabel customerIDText;
    private JLabel amountReturnText;
    private int cardNumber;
    private static Document logfile;
    private int saleID;
    private int commissionID;
    private int ID;
    private static String username;
    private static int customerID;
    private static int currentDate;
    private static String paymentType;
    private static int blankNumber;
    private static int ticketID;
    private static float price;
    private String customerEmail;

    public RefundsPayBack(int ID, String username, int customerID, int currentDate,
                          String paymentType, int blankNumber, int ticketID, int saleID, int commissionID, float price) {
        this.ID = ID;
        this.username = username;
        this.customerID = customerID;
        this.currentDate = currentDate;
        this.paymentType = paymentType;
        this.blankNumber = blankNumber;
        this.ticketID = ticketID;
        this.saleID = saleID;
        this.commissionID = commissionID;
        this.price = price;

        setContentPane(mainPanel);
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        customerIDText.setText(String.valueOf(customerID));
        amountReturnText.setText(String.valueOf(price));

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = " SELECT Card_Number FROM" +
                    " Card_Details " +
                    "WHERE Customer_ID = '"+ customerID +"' ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                cardNumber = rs.getInt("Card_Number");
            }
            st.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }


        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardNumberText.setText(String.valueOf(cardNumber));
            }
        });

        processRefundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "BEGIN;" +
                            "SELECT * FROM Refund WHERE Refund_ID = (SELECT COALESCE(MAX(Refund_ID), 0) FROM Refund) FOR UPDATE;" +
                            "INSERT INTO Refund SELECT" +
                            "(SELECT COALESCE(MAX(Refund_ID), 0) + 1 FROM Refund), '"+currentDate+"','"+customerID+"'," +
                            "'"+saleID+"'," +
                            "'"+ID+"', '"+commissionID+"', 1;" +
                            "COMMIT;";
                    System.out.println(query);
                    int insert = st.executeUpdate(query);

                    st.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }


                try {
                    Log log = new Log("/Users/alexelemele/Documents/logfile.txt");
                    log.logger.info("Refund of blankNumber: " + blankNumber + " to Customer: " + customerID + " on date:" +
                            " " + currentDate);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "BEGIN; " +
                            "SELECT * FROM Sale WHERE Sale.BlanlNumber = '"+blankNumber+"' FOR UPDATE; " +
                            "UPDATE Sale " +
                            "SET Sale.Refund_ID = 1 " +
                            "WHERE Sale.BlankNumber = '"+blankNumber+"';" +
                            "COMMIT; ";
                    System.out.println(query);
                    int rs = st.executeUpdate(query);
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(mainPanel,"Refund successfull");



                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT CustomerAccount.Email " +
                            "FROM CustomerAccount " +
                            "WHERE CustomerAccount.Customer_ID = '" + customerID + "' ";
                    System.out.println(query);
                    ResultSet rs = st.executeQuery(query);

                    if(rs.next()){
                        customerEmail = rs.getString("Email");
                    }

                    st.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                Mail mail = new Mail();
                mail.setupServerProperties();
                try {
                    mail.draftEmail(customerEmail,"Dear Customer " + customerID + ", this is to confirm that" +
                            "your refund for blankNumber: " + blankNumber + " was successfull and a amount of " + price);
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    mail.sendEmail();
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }

                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                advisorHome.show();

            }


        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialog = JOptionPane.showConfirmDialog(mainPanel,"Are you sure you want to cancel the refund?");

                if(dialog == -1){
                    dispose();
                    TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                    advisorHome.show();
                }


            }
        });
    }
}
