package Manager;

import Authentication.Login;
import SMTP.Mail;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OfficeManagerHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton stockButton;
    private JButton blanksButton;
    private JButton discountPlanButton;
    private JButton ticketStockTurnoverReportButton;
    private JButton logOutButton;
    private JLabel usernameLabel;
    private JPanel officeManagerPage;
    private JButton saleReportsButton;
    private JButton giveFeedbackButton;
    private JButton advisorIndividualReportButton;
    private static int ID;
    private static String username;


    public OfficeManagerHome(int ID, String username) {
        this.username = username;
        this.ID = ID;
        usernameLabel.setText("Manager: "+ username);
        setContentPane(officeManagerPage);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerHome officeManagerPage = new OfficeManagerHome(ID, username);
                officeManagerPage.setVisible(true);
                dispose();

            }
        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerStock officeManagerStock = new OfficeManagerStock(ID, username);
                officeManagerStock.setVisible(true);
                dispose();

            }
        });

        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID, username);
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });


        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan(ID, username);
                discountPlanButton.setVisible(true);
                dispose();

            }
        });

        ticketStockTurnoverReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnoverReportButton = new OfficeManagerTicketStockTurnOverReport(ID, username);
                ticketStockTurnoverReportButton.setVisible(true);
                dispose();

            }
        });


        saleReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeSaleReports saleReportsButton = new OfficeSaleReports(ID, username);
                saleReportsButton.setVisible(true);
                dispose();
            }

        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
                login.show();
            }
        });


        giveFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPopupFeedBack();
            }
        });
    }


    private void showPopupFeedBack() {
        JComponent glassPane = new JPanel();
        glassPane.setLayout(null);
        glassPane.setOpaque(false);

        JPanel popup = new JPanel();
        popup.setBounds(getWidth() - 200, 0, 200, 400);
        popup.setBackground(Color.LIGHT_GRAY);
        popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));

        JTextField textField = new JTextField();
        textField.setBounds(10, 10, 180, 30); // set the position and size of the text field
        popup.add(textField); // add the text field to the popup panel

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (text.contains("outofbounds")) {
                    textField.setText("");
                    popup.add(new JLabel("outofbounds"));
                    popup.revalidate();
                    popup.repaint();
                } else {
                    // handle submit action here

                    Mail mail = new Mail();
                    mail.setupServerProperties();

                    try {
                        mail.draftEmail("alexobz09@gmail.com",text);
                    } catch (MessagingException | IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        mail.sendEmail();
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        popup.add(submitButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                glassPane.setVisible(false);
            }
        });
        popup.add(closeButton);

        JScrollPane scrollPane = new JScrollPane(popup);
        scrollPane.setBounds(getWidth() - 200, 0, 200, 400);
        glassPane.add(scrollPane);

        setGlassPane(glassPane);
        glassPane.setVisible(true);
    }




    public static void main(String[] args){
        OfficeManagerHome officeHome = new OfficeManagerHome(ID,username);
        officeHome.show();
    }

}
