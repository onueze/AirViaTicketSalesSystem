package Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OfficeManagerHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton stockButton;
    private JButton blanksButton;
    private JButton discountPlanButton;
    private JButton ticketStockTurnoverReportButton;
    private JButton interlineSalesReportButton;
    private JButton domesticSalesReportButton;
    private JButton logOutButton;
    private JPanel logoField;
    private JLabel IDAndUserNameLabel;
    private JPanel officeManagerPage;


    public OfficeManagerHome(){
        setContentPane(officeManagerPage);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerHome officeManagerPage = new OfficeManagerHome();
                officeManagerPage.setVisible(true);
                dispose();

            }
        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerStock officeManagerStock = new OfficeManagerStock();
                officeManagerStock.setVisible(true);
                dispose();

            }
        });

        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks();
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });


        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan();
                discountPlanButton.setVisible(true);
                dispose();

            }
        });

        ticketStockTurnoverReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnoverReportButton = new OfficeManagerTicketStockTurnOverReport();
                ticketStockTurnoverReportButton.setVisible(true);
                dispose();

            }
        });

        interlineSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerInterlineSalesReports interlineSalesReportButton = new OfficeManagerInterlineSalesReports();
                interlineSalesReportButton.setVisible(true);
                dispose();

            }
        });

        domesticSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDomesticSalesReport domesticSalesReportButton = new OfficeManagerDomesticSalesReport();
                domesticSalesReportButton.setVisible(true);
                dispose();

            }
        });




    }



    public static void main(String[] args){
        OfficeManagerHome officeHome = new OfficeManagerHome();
        officeHome.show();
    }

}
