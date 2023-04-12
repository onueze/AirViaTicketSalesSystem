package Advisor.Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Advisor.Blanks.AdvisorStock;
import Advisor.IndividualReport.IndividualReport;
import Advisor.Refunds.Refunds;
import Advisor.Sales.OutstandingPayment;
import Advisor.Sales.SalesSearchCustomer;
import Authentication.Login;
import DB.DBConnectivity;



/**

 This class represents the home page for the travel advisor, which displays various options for them to select from.

 It contains buttons for navigating to the stock page, ticket sales page, refunds page, individual report page, and outstanding payment page.

 The advisor's username is also displayed on the page.
 */
public class TravelAdvisorHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton stockButton;
    private JButton ticketSalesButton;
    private JButton refundsButton;
    private JButton logOutButton;
    private JPanel travelAdvisorPage;
    private JPanel logoPanel;
    private JButton individualReportButton;
    private JLabel usernameLabel;
    private JButton outstandingPaymentButton;
    private ImageIcon logoImage;
    private JLabel logoLabel;
    private static int ID;
    private static int dateToday;
    private static String username;



    /**

     Constructs a TravelAdvisorHome object with the specified advisor ID and username.
     Sets the advisor's username on the page, and adds the AirVia logo to the logo panel.
     Sets the size, content pane, and visibility of the frame.
     @param ID the advisor's ID
     @param username the advisor's username
     */
    public TravelAdvisorHome(int ID, String username){
        this.username = username;
        this.ID = ID;
        this.dateToday = dateToday;
        usernameLabel.setText("advisor: "+ username);
        logoImage = new ImageIcon("data/AirViaLogo.png");
        logoLabel = new JLabel(logoImage);
        logoImage.getImage().getScaledInstance(500,500,Image.SCALE_DEFAULT);
        travelAdvisorPage.setPreferredSize(new Dimension(500,500));
        logoPanel.add(logoLabel);
        setContentPane(travelAdvisorPage);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);



        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
                login.show();

            }
        });
        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdvisorStock stock = new AdvisorStock(ID,username);
                stock.show();
            }
        });

        ticketSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SalesSearchCustomer advisorSales = new SalesSearchCustomer(ID,username);
                advisorSales.show();

            }
        });

        refundsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Refunds refunds = new Refunds(ID,username);
                refunds.show();
            }
        });
        individualReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                IndividualReport individualReport = new IndividualReport(ID,username);
                individualReport.show();
            }
        });
        outstandingPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OutstandingPayment outstandingPayment = new OutstandingPayment(ID,username);
            }
        });
    }




    public static void main(String[] args){
        TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID, username);
        advisorHome.show();

    }



}
