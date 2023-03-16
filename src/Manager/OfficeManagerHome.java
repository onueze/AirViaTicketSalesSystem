package Manager;

import javax.swing.*;

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
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }



    public static void main(String[] args){
        OfficeManagerHome officeHome = new OfficeManagerHome();
        officeHome.show();
    }

}
