package Advisor.Sales;

import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SalesCashPayNow extends javax.swing.JFrame {

    private JButton voidTicketButton;
    private JButton completeSaleButton;
    private JTextField amountGivenText;
    private JPanel mainPanel;
    private JLabel amountToBePaidText;
    private JLabel amountTobePaidLabel;
    private JButton calculateButton;
    private JLabel amountToBeReturnedText;
    private static int ID;
    private static String username;
    private static int customerID;
    private static float price;
    private static int blankNumber;
    private static String blankType;
    private static String paymentPeriod;
    private static String paymentType;
    private static int ticketID;
    private static int date;
    private static int currencyID;


    public SalesCashPayNow(int ID, String username, int customerID,
                           float price, int blankNumber, String blankType,
                           String paymentPeriod, String paymentType, int ticketID, int date,int currencyID) {
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        this.ID = ID;
        this.username = username;
        this.customerID = customerID;
        this.price = price;
        this.blankNumber = blankNumber;
        this.blankType = blankType;
        this.paymentPeriod = paymentPeriod;
        this.paymentType = paymentType;
        this.ticketID = ticketID;
        this.date = date;
        this.currencyID = currencyID;

        amountToBePaidText.setText(String.valueOf(price));

        completeSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = " INSERT INTO Sale SELECT" +
                            "(SELECT COALESCE(MAX(Sale_ID), 0) + 1 FROM Sale), '"+price+"','"+paymentPeriod+"'," +
                            " null,'"+date+"'," +
                            "'"+paymentType+"', '"+ID+"','"+currencyID+"'," +
                            "'"+customerID+"',1,'"+ticketID+"','"+blankNumber+"'";
                    System.out.println(query);
                    int insert = st.executeUpdate(query);

                    st.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(mainPanel,"Payment was successful and has been recorded");

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "UPDATE Blank " +
                            "SET Blank.isSold = '" + 1 + "' " +
                            "WHERE Blank.blankNumber = '" + blankNumber + "' ";
                    System.out.println(query);
                    int rs = st.executeUpdate(query);

                    st.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                advisorHome.show();



            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountGivenString = amountGivenText.getText();
                float amountGivenInt = Float.parseFloat(amountGivenString);
                float amountToBeReturned = amountGivenInt - price;
                if(amountToBeReturned < 0){
                    JOptionPane.showMessageDialog(mainPanel,"Customer has not given enough");
                }
                else{
                    amountToBeReturnedText.setText(String.valueOf(amountToBeReturned));
                }

            }
        });
    }


    public static void main (String[]args){
        SalesCashPayNow salesCashPayNow = new SalesCashPayNow(ID, username,customerID,price,blankNumber,blankType,paymentPeriod,paymentType,ticketID,date,currencyID);
        salesCashPayNow.show();
    }
}
