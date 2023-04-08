package Advisor.Sales;

import DB.DBConnectivity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SalesCardPayNow extends javax.swing.JFrame {
    private JButton checkCardDetailsButton;
    private JButton cancelPaymentButton;
    private JButton completePaymentButton;
    private JTextField customerName;
    private JTextField creditCardnumber;
    private JLabel priceToPayLabel;
    private JLabel PriceIntoUSD;
    private JLabel pricePercentageLabel;
    private JPanel mainPanel;
    private JTextField textField1;
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

    public SalesCardPayNow(int ID, String username, int customerID,
                           float price, int blankNumber, String blankType,
                           String paymentPeriod, String paymentType, int ticketID) {
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        this.ID = ID;
        this.username = username;
        this.customerID =customerID;
        this.price = price;
        this.blankNumber = blankNumber;
        this.blankType = blankType;
        this.paymentPeriod = paymentPeriod;
        this.paymentType = paymentType;
        this.ticketID = ticketID;

        checkCardDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardNumber = creditCardnumber.getText();
                cardNumber = cardNumber.replace(" ","");
                System.out.println(cardNumber + "CARDNUMBER");
                cardValid = validateCreditCardNumber(cardNumber);
                if((creditCardnumber.getText().equals(""))){
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card number is not valid");
                }
                if(cardValid ){
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card number is valid");
                }
                else{
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
//                        String query = " INSERT INTO Sale SELECT" +
//                                "(SELECT COALESCE(MAX(Sale_ID), 0) + 1 FROM Sale), '"+price+"','"+paymentPeriod+"'," +
//                                " null,'"+date+"'," +
//                                "'"+paymentType+"', '"+ID+"','"+currencyCode+"'," +
//                                "'"+customerID+"',1,'"+ticketID+"','"+blankNumber+"'";
//                        System.out.println(query);
//                        ResultSet rs = st.executeQuery(query);

                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel,"Credit Card information was not valid");

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
         price,blankNumber, blankType,paymentPeriod,paymentType, ticketID);
    }

}

