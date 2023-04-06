package Manager;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;


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
    private JButton openBlankReportButton;
    private static int ID;
    private static String username;






    private void appendToLogFile(String message) {
        File logFile = new File("/Users/aadilghani/Desktop/AirViaTicketSalesSystem/src/LogFile Report.txt");

        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public OfficeManagerStock(int ID, String username) {

        stockTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        stockTableScroll.setPreferredSize(new Dimension(500, 500));
        this.username = username;
        this.ID = ID;

        setContentPane(Stock);
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        // add this to assign and reassign somehow

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);






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
                dispose();

                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID, username);
                officeManagerBlanks.setVisible(true);

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

        interlineSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerInterlineSalesReports interlineSalesReportButton = new OfficeManagerInterlineSalesReports(ID, username);
                interlineSalesReportButton.setVisible(true);
                dispose();

            }
        });

        domesticSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDomesticSalesReport domesticSalesReportButton = new OfficeManagerDomesticSalesReport(ID, username);
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
                    String query = "SELECT  Blank.BlankNumber FROM Blank WHERE isAssigned = false";


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
                    String query = "SELECT  Employee.Employee_ID FROM Employee where Employee.role = 'advisor'";


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
                    String query = "UPDATE Blank SET Employee_ID = ?, isAssigned = true WHERE BlankNumber = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);

                    // Set the values for the prepared statement
                    preparedStatement.setString(1, assignAdvisorID);
                    preparedStatement.setString(2, assignBlankNumber);


                    int result2 = preparedStatement.executeUpdate();

                    if (result2 > 0) {
                        // Show success message
                        JOptionPane.showMessageDialog(null, "Blank table updated successfully");

                        String message = "Assigned Blank Number: " + assignBlankNumber + ", Employee ID: " + assignAdvisorID;

                        appendToLogFile(message);
                    } else {
                        // Show error message
                        JOptionPane.showMessageDialog(null, "Failed to update Blank table");
                    }
                    // Maybe delte one okf the jOption pane, or leave both if not really afectign runnign

                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Database driver not found");
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating the blank table");
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

                            String message = "Reassigned Blank Number: " + reAssignBlankNumber + ", Employee ID: " + reAssignAdvisorID;
                            appendToLogFile(message);
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
