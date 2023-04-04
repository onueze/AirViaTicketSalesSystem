package Advisor;

import DB.DBConnectivity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SaleSummaryPage extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton voidTicketButton;
    private JButton payButton;
    private JLabel CustomerNamesLabel;
    private JLabel blankNumberLabel;
    private JLabel blankTypeLabel;
    private JLabel flightNumberLabel;
    private JLabel airlineLabel;
    private JLabel departureLabel;
    private JLabel arrivalLabel;
    private JLabel departureDateLabel;
    private JLabel departureTimeLabel;
    private JLabel arrivalTimeLabel;
    private JLabel paymentPeriodLabel;
    private JLabel paymentMethodLabel;
    private JLabel totalPriceLabel;
    private JLabel customerIDLabel;
    private static int ID;
    private static String username;
    static float price;
    private static int flightID;
    private static String paymentPeriod;
    private static String paymentType;
    private static int blankNumber;
    private static String blankType;
    private static int customerID;
    private String firstName;
    private String surname;
    private String flightDeparture;
    private String flightArrival;
    private String flightDepTime;
    private String flightArrtime;
    private String airline;
    private String flightDate;



    public SaleSummaryPage(int ID, String username, int customerID, float price,
                           int flightID,String paymentPeriod,String paymentType,int blankNumber,String blankType){
        this.ID = ID;
        this.username = username;
        this.price = price;
        this.flightID = flightID;
        this.paymentPeriod = paymentPeriod;
        this.paymentType = paymentType;
        this.blankNumber = blankNumber;
        this.blankType = blankType;
        this.customerID = customerID;
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        customerIDLabel.setText(Integer.toString(customerID));
        blankNumberLabel.setText(Integer.toString(blankNumber));
        blankTypeLabel.setText(blankType);
        flightNumberLabel.setText(Integer.toString(flightID));
        paymentPeriodLabel.setText(paymentPeriod);
        paymentMethodLabel.setText(paymentType);
        totalPriceLabel.setText(Float.toString(price));


        try(Connection con = DBConnectivity.getConnection()){
            Class.forName("com.mysql.cj.jdbc.Driver");

            Statement stCustName = con.createStatement();
            String queryCustName = "SELECT Firstname,Surname  " +
                    "FROM CustomerAccount " +
                    "WHERE CustomerAccount.Customer_ID = '" + customerID + "' ";
            ResultSet rsCustName = stCustName.executeQuery(queryCustName);

            if(rsCustName.next()) {
                firstName = rsCustName.getString(1);
                surname = rsCustName.getString(2);
            }
            CustomerNamesLabel.setText(firstName + " " + surname);

            stCustName.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Flight.departure, Flight.destination, Flight.depTime, Flight.arrTime, " +
                    " Flight.Airline, Flight.F_Date " +
                    "FROM Flight \n" +
                    "WHERE Flight.number  = '"+flightID+"' ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);

            if(rs.next()) {
                flightDeparture = rs.getString(1);
                flightArrival = rs.getString(2);
                flightDepTime = rs.getString(3);
                flightArrtime = rs.getString(4);
                airline = rs.getString(5);
                flightDate = rs.getString(6);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        departureLabel.setText(flightDeparture);
        arrivalLabel.setText(flightArrival);
        departureTimeLabel.setText(flightDepTime);
        arrivalTimeLabel.setText(flightArrtime);
        airlineLabel.setText(airline);
        departureDateLabel.setText(flightDate);

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        voidTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    public static void main(String[]args){
        SaleSummaryPage saleSummaryPage = new SaleSummaryPage(ID,  username,customerID,  price,
        flightID, paymentPeriod, paymentType, blankNumber, blankType);
    }
}
