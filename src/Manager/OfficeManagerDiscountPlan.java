package Manager;

import DB.DBConnectivity;

import javax.swing.*;
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
    private JPanel DiscountPlan;
    private JScrollPane DiscountPlanScroll;
    private JTextField setDiscountRate;
    private JTextField lowerRange;
    private JTextField midRange;
    private JButton submitFixedDiscountRateButton;
    private JButton submitFlexableDiscountRatesButton;
    private JTextField UpperRange;
    private JTextField lowerRangeRate;
    private JTextField midRangeRate;
    private JTextField upperRangeRate;
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






/*
        ViewDetails.addActionListener(new ActionListener() {
            @Override
            // add to check the total sales not just one tocket sale
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

 */
/*
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


        submitFixedDiscountRateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int fixedRate = Integer.parseInt(setDiscountRate.getText());


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    //Statement st = con.createStatement();
                    String query = "\"WITH MonthlySales AS (\n" +
                            "SELECT Customer_ID, SUM(amount) AS sales_total\n" +
                            "FROM Sale\n" +
                            "WHERE EXTRACT(MONTH FROM Current_month) = EXTRACT(MONTH FROM CURRENT_DATE)\n" +
                            "AND EXTRACT(YEAR FROM Current_month) = EXTRACT(YEAR FROM CURRENT_DATE)\n" +
                            "GROUP BY Customer_ID\n" +
                            "UPDATE FixedDiscount"+
                            "SET Rate = (SELECT fixedRate FROM DiscountType WHERE DiscountType = 'Fixed')"+
                            "FROM FixedDiscount fd"+
                            "JOIN CustomerAccount CurrentAccount ON FixedDiscount.CustomerID = CustomerAccount.Customer_ID"+
                            "JOIN MonthlySales MonthlySales ON CurrentAccount.Customer_ID = MonthlySales.Customer_ID"+
                            "WHERE ms.sales_total < 3";

                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, fixedRate);
                    pstmt.executeQuery();

                } catch (ClassNotFoundException ex) {ex.printStackTrace();


            } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        submitFlexableDiscountRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lowerRate = Integer.parseInt(lowerRangeRate.getText());
                int midRate = Integer.parseInt(midRangeRate.getText());
                int upperRate = Integer.parseInt(upperRangeRate.getText());

                int lowRange = Integer.parseInt(lowerRange.getText());
                int MidRange = Integer.parseInt(midRange.getText());
                int upperRange = Integer.parseInt(UpperRange.getText());




                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    //Statement st = con.createStatement();

                    String query = "WITH MonthlySales AS (" +
                            "  SELECT Customer_ID, SUM(amount) AS sales_total" +
                            "  FROM Sale" +
                            "  WHERE EXTRACT(MONTH FROM Current_month) = EXTRACT(MONTH FROM CURRENT_DATE)" +
                            "  AND EXTRACT(YEAR FROM Current_month) = EXTRACT(YEAR FROM CURRENT_DATE)" +
                            "  GROUP BY Customer_ID" +
                            ")," +
                            "UPDATE FlexibleDiscountPlan" +
                            " SET Rate = CASE" +
                            "             WHEN ms.sales_total BETWEEN 1 AND " + lowRange + " THEN " + lowerRate +
                            "             WHEN ms.sales_total BETWEEN " + (lowRange + 1) + " AND " + midRange + " THEN " + midRate +
                            "             ELSE " + upperRate +
                            "           END" +
                            " FROM FlexibleDiscountPlan fdp" +
                            " JOIN CustomerAccount ca ON fdp.CustomerID = ca.Customer_ID" +
                            " JOIN MonthlySales ms ON ca.Customer_ID = ms.Customer_ID" +
                            " WHERE ms.sales_total > 3;";

                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setInt(1,lowerRate );
                    pstmt.setInt(2,midRate );
                    pstmt.setInt(3,upperRate );

                    pstmt.setInt(4,lowRange );
                    pstmt.setInt(5,MidRange );
                    pstmt.setInt(6,upperRange );
                    pstmt.executeQuery();


                } catch (ClassNotFoundException ex) {ex.printStackTrace();


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }




            }
        });
    }

    public static void main(String[] args){
        OfficeManagerDiscountPlan DiscountPlan = new OfficeManagerDiscountPlan(ID,username);
        DiscountPlan.show();
    }
}
