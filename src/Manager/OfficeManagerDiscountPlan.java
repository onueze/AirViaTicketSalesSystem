package Manager;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OfficeManagerDiscountPlan extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton homeButton;
    private JButton domesticSalesReportButton;
    private JButton discountPlanButton;
    private JButton blanksButton;
    private JButton stockButton;
    private JButton interlineSalesReportButton;
    private JButton ticketStockTurnOverButton;
    private JTable DiscountPlanTable;
    private JComboBox comboBox1;
    private JButton verifyCustomerSalesForButton;
    private JTextField autoFilledByVerifyTextField;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField discountRatetextFormattedTextField;
    private JButton submitCustomerDiscountPlanButton;
    private JPanel DiscountPlan;
    private JButton ViewDetails;
    private JScrollPane DiscountPlanScroll;
    private JComboBox CustomerIDDropdown;
    private JButton viewCustomerIDButton;
    private static int ID;
    private static String username;


    public OfficeManagerDiscountPlan(int ID, String username) {
        DiscountPlanTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        DiscountPlanScroll.setPreferredSize(new Dimension(500, 500));


        this.username = username;
        this.ID = ID;
        setContentPane(DiscountPlan);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);







        ViewDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT CustomerAccount.Customer_ID, " +
                            "       CASE WHEN CustomerAccount.sales_this_month > 5 " +
                            "            THEN 'FlexibleDiscount' " +
                            "            ELSE 'FixedDiscount' " +
                            "       END AS DiscountPlan, " +
                            "       CASE WHEN CustomerAccount.sales_this_month > 5 " +
                            "            THEN FlexibleDiscount.Rate " +
                            "            ELSE FixedDiscount.Rate " +
                            "       END AS DiscountRate " +
                            "FROM CustomerAccount " +
                            "LEFT JOIN FixedDiscount ON CustomerAccount.Customer_ID = FixedDiscount.CustomerID " +
                            "LEFT JOIN FlexibleDiscount ON CustomerAccount.Customer_ID = FlexibleDiscount.CustomerID;";

                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) DiscountPlanTable.getModel();

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++) {
                        colName[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colName);
                    String customer_ID, discountPlan, discountRate;


                    while (rs.next()) {
                        customer_ID = rs.getString(1);
                        discountPlan = rs.getString(2);
                        discountRate = rs.getString(3);


                        String[] row = {customer_ID, discountPlan, discountRate};
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

        viewCustomerIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT CustomerAccount.Customer_ID from CustomerAccount;";

                    ResultSet rs = st.executeQuery(query);

                    while (rs.next()) {
                        CustomerIDDropdown.addItem(rs.getString("CustomerAccount.Customer_ID"));

                    }
                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });





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





    }

    public static void main(String[] args){
        OfficeManagerDiscountPlan DiscountPlan = new OfficeManagerDiscountPlan(ID,username);
        DiscountPlan.show();
    }
}
