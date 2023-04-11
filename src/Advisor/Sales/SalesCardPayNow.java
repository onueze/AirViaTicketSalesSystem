package Advisor.Sales;

import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;
import SMTP.Mail;
import com.itextpdf.text.Document;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SalesCardPayNow extends javax.swing.JFrame {
    private float priceInUSD;
    private JButton checkCardDetailsButton;
    private JButton cancelPaymentButton;
    private JButton completePaymentButton;
    private JTextField customerName;
    private JTextField creditCardnumber;
    private JLabel pricePercentageLabel;
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField enterCurrencyCodeTextField;
    private JButton exchangeButton;
    private JLabel usdPriceLabel;
    private static int ID;
    private static String username;
    private static int customerID;
    private static float price;
    private static int blankNumber;
    private static String blankType;
    private static String paymentPeriod;
    private static String paymentType;
    private float exchangeRate;
    private boolean cardValid;
    private static int ticketID;
    private static int date;
    private static int currencyID;
    private static Document document;
    private String customerEmail;
    private String cardNumber;

    public SalesCardPayNow(int ID, String username, int customerID,
                           float price, int blankNumber, String blankType,
                           String paymentPeriod, String paymentType, int ticketID, int date, int currencyID, Document document) {
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
        this.document = document;

        usdPriceLabel.setText(String.valueOf(price));



        checkCardDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardNumber = creditCardnumber.getText();
                cardNumber = cardNumber.replace(" ","");
                System.out.println(cardNumber + "CARDNUMBER");
                cardValid = validateCreditCardNumber(cardNumber);

                if(cardValid && !(creditCardnumber.getText().equals("")) ){
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card number is valid");
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card number is not valid");
                }


            }
        });
        cancelPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        completePaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cardValid) {
                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Statement st = con.createStatement();
                        String query = " INSERT INTO Sale SELECT" +
                                "(SELECT COALESCE(MAX(Sale_ID), 0) + 1 FROM Sale), '"+priceInUSD+"','"+paymentPeriod+"'," +
                                " null,'"+date+"'," +
                                "'"+paymentType+"', '"+ID+"','"+ SalesCardPayNow.currencyID +"'," +
                                "'"+customerID+"',1,'"+ticketID+"','"+blankNumber+"', null, 1";
                        System.out.println(query);
                        int insert = st.executeUpdate(query);

                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(mainPanel,"Payment was successful and has been recorded");
                } else {
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card information was not valid");

                }

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "UPDATE Blank " +
                            "SET Blank.isSold = '" + 1 + "' " +
                            "WHERE Blank.blankNumber = '" + blankNumber + "' ";
                    System.out.println(query);
                    int rs = st.executeUpdate(query);

                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                advisorHome.show();

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



                BigInteger bigNum = new BigInteger(cardNumber);

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = " INSERT INTO Card_Details VALUES" +
                            "('"+ bigNum +"', '"+customerID+"')";
                    System.out.println(query);
                    int insert = st.executeUpdate(query);
                    st.close();

                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                Mail mail = new Mail();
                mail.setupServerProperties();
                try {
                    mail.draftEmail(customerEmail,"Dear Customer for AirVia, this" +
                            "is your receipt for your most recent flight purchase", "/Users/alexelemele/Documents/testPDF.pdf");
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

            }
        });
//        exchangeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                try (Connection con = DBConnectivity.getConnection()) {
//                    assert con != null;
//                    Class.forName("com.mysql.cj.jdbc.Driver");
//                    Statement st = con.createStatement();
//                    String query = "SELECT Currency_Code.Currency_Code ,Currency_Code.Exchange_Rate " +
//                            "FROM Currency_Code " +
//                            "WHERE Currency_Code.Currency_name = '"+enterCurrencyCodeTextField.getText()+"'  ";
//                    ResultSet rs = st.executeQuery(query);
//
//                    if(rs.next()){
//                        exchangeRate = rs.getFloat("Exchange_Rate");
//                        currencyID = rs.getInt("Currency_Code");
//                    }
//                    System.out.println(enterCurrencyCodeTextField.getText() + "CURRENCY CODE");
//                    System.out.println(exchangeRate+ "exchange rate");
//                    System.out.println(price + "price");
//
//                    priceInUSD = price / exchangeRate;
//                } catch (SQLException | ClassNotFoundException ex) {
//                    ex.printStackTrace();
//                }
//                System.out.println(priceInUSD);
//                usdPriceLabel.setText(String.valueOf(priceInUSD));
//                pricePercentageLabel.setText("Price to pay in USD (exchange Rate " + exchangeRate + " applied)");
//            }
//        });
        creditCardnumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }

    private static boolean validateCreditCardNumber(String input){

        //convert input to int
        int[] creditCardInt = new int[input.length()];

        // place all elements of the string into the int arrays
        for(int i = 0; i < input.length(); i++){
            creditCardInt[i] = Integer.parseInt(input.substring(i,i+1));

        }
        //Starting from the right, double each other digit, if greater than 9 mod 10 + 1 to the remainder
        for(int i = creditCardInt.length - 2; i >= 0; i = i - 2){

            int tempValue = creditCardInt[i];
            tempValue = tempValue * 2;
            if(tempValue > 9){
                tempValue =  tempValue % 10 + 1;
            }

            creditCardInt[i] = tempValue;

        }
        // Add up all digits
        int total = 0;
        for(int i = 0; i < creditCardInt.length; i++){
            total += creditCardInt[i];
        }

        if(total % 10 == 0){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[]args){
        SalesCardPayNow salesCardPayNow = new SalesCardPayNow(ID,username,customerID,
         price,blankNumber, blankType,paymentPeriod,paymentType,ticketID,date, currencyID, document);
    }

}

