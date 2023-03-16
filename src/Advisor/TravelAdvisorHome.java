package Advisor;

import javax.swing.*;

public class TravelAdvisorHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton stockButton;
    private JButton ticketSalesButton;
    private JButton refundsButton;
    private JButton earnedCommissionButton;
    private JButton logOutButton;
    private JPanel travelAdvisorPage;


    public TravelAdvisorHome(){
        setContentPane(travelAdvisorPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args){
        TravelAdvisorHome advisorHome = new TravelAdvisorHome();
        advisorHome.show();
    }


}
