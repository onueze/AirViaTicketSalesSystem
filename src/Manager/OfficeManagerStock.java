package Manager;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OfficeManagerStock extends javax.swing.JFrame {

    private JPanel Stock;
    private JButton logOutButton;
    private JTable stockTable;
    private JButton domesticSalesReportButton;
    private JButton homeButton;
    private JButton discountPlanButton;
    private JButton blanksButton;
    private JButton stockButton;
    private JButton ticketStockTurnOverButton;
    private JButton interlineSalesReportButton;
    private JComboBox assignTravelAdvisor;
    private JComboBox assignBlank;
    private JButton submitAssignBlanksButton;
    private JButton submitReassignBlanksButton;
    private JComboBox blankType;
    private JButton showStockButton;
    private JScrollPane stockTableScroll;
    private JComboBox ReassignAdvisor;
    private JComboBox reasssignBlank;
    private static int ID;
    private static String username;
    private static String role;

    public OfficeManagerStock(int ID, String username) {

        stockTable.setPreferredScrollableViewportSize(new Dimension(500,500));
        stockTableScroll.setPreferredSize(new Dimension(500,500));
        this.username = username;
        this.ID = ID;
        this.role = "Advisor";
        setContentPane(Stock);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);













        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerHome officeManagerPage = new OfficeManagerHome(ID,username);
                officeManagerPage.setVisible(true);
                dispose();

            }
        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerStock officeManagerStock = new OfficeManagerStock(ID,username);
                officeManagerStock.setVisible(true);
                dispose();

            }
        });

        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID,username);
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });


        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan(ID,username);
                discountPlanButton.setVisible(true);
                dispose();

            }
        });

        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerTicketStockTurnOverReport ticketStockTurnOverButton = new OfficeManagerTicketStockTurnOverReport(ID,username);
                ticketStockTurnOverButton.setVisible(true);
                dispose();

            }
        });

        interlineSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerInterlineSalesReports interlineSalesReportButton = new OfficeManagerInterlineSalesReports(ID,username);
                interlineSalesReportButton.setVisible(true);
                dispose();

            }
        });

        domesticSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDomesticSalesReport domesticSalesReportButton = new OfficeManagerDomesticSalesReport(ID,username);
                domesticSalesReportButton.setVisible(true);
                dispose();

            }
        });


        showStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT  Blank.BlankNumber FROM Blank;";


                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) stockTable.getModel();

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++) {
                        colName[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colName);
                    String blankNumber;
                    while (rs.next()) {
                        blankNumber = rs.getString(1);


                        String[] row = {blankNumber};
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



        assignTravelAdvisor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT  Employee.Employee_ID FROM Employee where Employee.role = 'advisor'";

                    ResultSet rs = st.executeQuery(query);
                    assignTravelAdvisor.removeAllItems();


                    while (rs.next()) {
                        String Id = rs.getString("Employee_ID");
                        assignTravelAdvisor.addItem(Id);
                    }

                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



        assignBlank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query =  "SELECT  Blank.BlankNumber FROM Blank WHERE isAssigned = false";


                    ResultSet rs = st.executeQuery(query);
                    assignBlank.removeAllItems();

                    while (rs.next()) {
                        //assignTravelAdvisor.addItem(rs.getString("BlankNumber"));
                        String number2 = rs.getString("BlankNumber");
                        assignBlank.addItem(number2);
                    }

                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        ReassignAdvisor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query =  "SELECT DISTINCT Blank.Employee_ID FROM Blank WHERE ";


                    ResultSet rs = st.executeQuery(query);
                    ReassignAdvisor.removeAllItems();


                    while (rs.next()) {
                        //assignTravelAdvisor.addItem(rs.getString("BlankNumber"));
                        String id = rs.getString("Employee_ID");
                        ReassignAdvisor.addItem(id);
                    }

                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }



            }
        });

        reasssignBlank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String reassignAdvisorID = (String) ReassignAdvisor.getSelectedItem();


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT BlankNumber FROM Blank WHERE Employee_ID != ? AND isAssigned = true";
                    PreparedStatement preparedStatement = con.prepareStatement(query);

                    // Set the value for the advisor_ID placeholder
                    preparedStatement.setString(1, reassignAdvisorID);


                    ResultSet rs = st.executeQuery(query);
                    reasssignBlank.removeAllItems();


                    while (rs.next()) {
                        //assignTravelAdvisor.addItem(rs.getString("BlankNumber"));
                        String number = rs.getString("BlankNumber");
                        reasssignBlank.addItem(number);
                    }

                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }




            }
        });


        submitAssignBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String assignAdvisorID = (String) assignTravelAdvisor.getSelectedItem();
                String assignBlankNumber = (String) assignBlank.getSelectedItem();


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query =  "UPDATE Blank SET Employee_ID = ?, isAssigned = true WHERE BlankNumber = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);

                    // Set the values for the prepared statement
                    preparedStatement.setString(1, assignAdvisorID);
                    preparedStatement.setString(2, assignBlankNumber );


                    int result = preparedStatement.executeUpdate();

                    if (result > 0) {
                        // Show success message
                        JOptionPane.showMessageDialog(null, "Blank table updated successfully");
                    } else {
                        // Show error message
                        JOptionPane.showMessageDialog(null, "Failed to update Blank table");
                    }

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        submitReassignBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                {
                    String reAssignAdvisorID = (String) ReassignAdvisor.getSelectedItem();
                    String reAssignBlankNumber = (String) reasssignBlank.getSelectedItem();


                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Statement st = con.createStatement();
                        String query = "UPDATE Blank SET Employee_ID = ?, isAssigned = true WHERE BlankNumber = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(query);

                        // Set the values for the prepared statement
                        preparedStatement.setString(1, reAssignAdvisorID);
                        preparedStatement.setString(2, reAssignBlankNumber);


                        int result = preparedStatement.executeUpdate();

                        if (result > 0) {
                            // Show success message
                            JOptionPane.showMessageDialog(null, "Blank table updated successfully");
                        } else {
                            // Show error message
                            JOptionPane.showMessageDialog(null, "Failed to update Blank table");
                        }

                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }


                }
                }
        });
    }



    public static void main(String[] args){
        OfficeManagerStock Stock = new OfficeManagerStock(ID,username);
        Stock.show();

    }







}
