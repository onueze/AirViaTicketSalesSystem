package Authentication;

import Admin.Home.SystemAdminHome;
import Advisor.Home.TravelAdvisorHome;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterDate extends javax.swing.JFrame {
    private JTextField dateTodayText;
    private JButton continueButton;
    private JButton logOutButton;
    private JPanel mainPanel;

    public static int getDateToday() {
        return dateToday;
    }

    private static int dateToday;

    public EnterDate(int ID, String username) {
        setContentPane(mainPanel);
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
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateTodayText.getText().replace("/","");
                dateToday = Integer.parseInt(date);
                dispose();
                SystemAdminHome home = new SystemAdminHome(ID,username,dateToday);

            }
        });
    }
}
