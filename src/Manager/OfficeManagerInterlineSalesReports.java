package Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OfficeManagerInterlineSalesReports extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton homeButton;
    private JButton domesticSalesReportButton;
    private JButton interlineSalesReportButton;
    private JButton stocksButton;
    private JButton blanksButton;
    private JButton ticketStockTurnOverButton;
    private JButton discountPlanButton;
    private JPanel InterlineReport;
    private static int ID;
    private static String username;



    public OfficeManagerInterlineSalesReports(int ID, String username){
            this.username = username;
            this.ID = ID;

        setContentPane(InterlineReport);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerHome officeManagerPage = new OfficeManagerHome(ID,username);
                officeManagerPage.setVisible(true);
                dispose();

            }
        });

        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerStock officeManagerStock = new OfficeManagerStock(ID,username);
                officeManagerStock.setVisible(true);
                dispose();

            }
        });

        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID,username);
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });


        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan(ID,username);
                discountPlanButton.setVisible(true);
                dispose();

            }
        });

        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnOverButton = new OfficeManagerTicketStockTurnOverReport(ID,username);
                ticketStockTurnOverButton.setVisible(true);
                dispose();

            }
        });




    }



    public static void main(String[] args){
        OfficeManagerInterlineSalesReports InterlineReport = new OfficeManagerInterlineSalesReports(ID,username);
        InterlineReport.show();
    }
}
