package Manager;
import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;


public class OfficeManagerBlanks extends javax.swing.JFrame{
    private JPanel Blanks;
    private JButton logOutButton;
    private JButton homeButton;
    private JButton stockButton;
    private JButton blanksButton;
    private JButton discountPlanButton;
    private JButton ticketStockTurnOverButton;
    private JButton submitBlankUsageReportButton;
    private JTable blanksTable;
    private JScrollPane blankTableScroll;
    private JButton showBlanksButton;
    private JButton viewBlankUsageReportButton;
    private JButton saleReportsButton;
    private JLabel usernameLabel;


    private static int ID;
    private static String username;




    public OfficeManagerBlanks(int ID, String username) {
        blanksTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        blankTableScroll.setPreferredSize(new Dimension(500, 500));

        this.username = username;
        this.ID = ID;
        usernameLabel.setText("Manager: "+ username);
        setContentPane(Blanks);
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);


        /*       NEED TO SORT ADD THE FILTER SYSTEM, MAKE IT THE A METHOD SO IT CAN BE CALLAED MUILT TOMES WITHOUT REAPIGN ALL THE CODE
        */


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

        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnOverButton = new OfficeManagerTicketStockTurnOverReport(ID, username);
                ticketStockTurnOverButton.setVisible(true);
                dispose();

            }
        });



        viewBlankUsageReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File logFile = new File("/Users/aadilghani/Desktop/AirViaTicketSalesSystem/src/LogFile Report.txt");
                    Desktop.getDesktop().open(logFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        showBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) blanksTable.getModel();
                model.setRowCount(0);

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT Blank (Employee.first_name, Employee.last_name) " +
                            "FROM Blank " +
                            "LEFT JOIN Employee ON Blank.Employee_ID = Employee.Employee_ID AND Employee.role = 'advisor';";


                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();



                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++) {
                        colName[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colName);
                    String blankNumber, Type, IsSold, IsAssigned, employee_ID, employee_first_name, employee_last_name;
                    while (rs.next()) {
                        blankNumber = rs.getString(1);
                        Type = rs.getString(2);
                        IsSold = rs.getString(3);
                        IsAssigned = rs.getString(4);
                        employee_ID = rs.getString(5);
                        employee_first_name = rs.getString(6);
                        employee_last_name = rs.getString(7);

                        String[] row = {blankNumber, Type, IsSold, IsAssigned, employee_ID, employee_first_name, employee_last_name};
                        model.addRow(row);
                    }
                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

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
    }



    public static void main(String[] args){
        OfficeManagerBlanks Blanks = new OfficeManagerBlanks(ID,username);
        Blanks.show();


    }

}
