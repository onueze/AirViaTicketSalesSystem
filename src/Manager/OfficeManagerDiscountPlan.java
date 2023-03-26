package Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OfficeManagerDiscountPlan extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton homeButton;
    private JButton domesticSalesReportButton;
    private JButton discountPlanButton;
    private JButton blanksButton;
    private JButton stockButton;
    private JButton interlineSalesReportButton;
    private JButton ticketStockTurnOverButton;
    private JTable table1;
    private JComboBox comboBox1;
    private JTextField enterCustomerIDTextField;
    private JButton verifyCustomerSalesForButton;
    private JTextField autoFilledByVerifyTextField;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JButton submitCustomerDiscountPlanButton;
    private JPanel DiscountPlan;


    public OfficeManagerDiscountPlan(){
        setContentPane(DiscountPlan);
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

        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnOverButton = new OfficeManagerTicketStockTurnOverReport();
                ticketStockTurnOverButton.setVisible(true);
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
        OfficeManagerDiscountPlan DiscountPlan = new OfficeManagerDiscountPlan();
        DiscountPlan.show();
    }
}
