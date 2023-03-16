package Advisor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Authentication.Login;

public class TravelAdvisorHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton stockButton;
    private JButton ticketSalesButton;
    private JButton refundsButton;
    private JButton earnedCommissionButton;
    private JButton logOutButton;
    private JPanel travelAdvisorPage;
    private JPanel logoPanel;
    private JButton individualReportButton;
    private ImageIcon logoImage;
    private JLabel logoLabel;


    public TravelAdvisorHome(){
        logoImage = new ImageIcon("data/AirViaLogo.png");
        logoLabel = new JLabel(logoImage);
        logoImage.getImage().getScaledInstance(500,500,Image.SCALE_DEFAULT);
        travelAdvisorPage.setPreferredSize(new Dimension(500,500));
        logoPanel.add(logoLabel);
//        logoPanel.add(new InsertImage("data/AirViaLogo.png"));
        setContentPane(travelAdvisorPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();

            }
        });
        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdvisorStock stock = new AdvisorStock();
            }
        });
    }

    public static void main(String[] args){
        TravelAdvisorHome advisorHome = new TravelAdvisorHome();
        advisorHome.show();

    }



}
