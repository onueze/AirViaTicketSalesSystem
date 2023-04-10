package Admin.Blanks;

import Admin.Commission.CommissionRates;
import Admin.CustomerDetails.CustomerDetails;
import Admin.Home.SystemAdminHome;
import Admin.UserDetails.CreateUser;
import Admin.UserDetails.UserDetails;
import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.List;


public class SystemStock extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton createUserButton;
    private JButton homeButton;
    private JButton manageUserDetailsButton;
    private JButton manageSystemStockButton;
    private JButton manageCommissionRatesButton;
    private JButton manageCustomerDetailsButton;
    private JPanel systemStockPage;

    private JButton ShowBlanks;
    private JTable blankTable;
    private JScrollPane blankScrollPane;
    private JComboBox selectOfficeManagerID;
    private JComboBox selectFilter;
    private JButton SUBMITBLANKASSIGNButton;
    private JTextField lowerRange;
    private JTextField upperRange;
    private JTextField assignDate;
    private JComboBox selectblankPrefix;
    private JComboBox SelectblankType;


    private static int ID;
    private static String username;


    private boolean validateInput() {
        String lowerInput = lowerRange.getText();
        String upperInput = upperRange.getText();
        List<String> validPrefixes = Arrays.asList("444", "440", "420", "201", "101", "451", "452");

        if (lowerInput.length() != 9 || upperInput.length() != 9) {
            JOptionPane.showMessageDialog(null, "Blank number must be exactly 9 digits");
            return false;
        }

        String lowerPrefix = lowerInput.substring(0, 3);
        String upperPrefix = upperInput.substring(0, 3);

        if (!validPrefixes.contains(lowerPrefix) || !validPrefixes.contains(upperPrefix)) {
            JOptionPane.showMessageDialog(null, "Invalid blank type provided");
            return false;
        }

        if (!lowerPrefix.equals(upperPrefix)) {
            JOptionPane.showMessageDialog(null, "Ensure blank types are the same");
            return false;
        }

        if (Long.parseLong(lowerInput) >= Long.parseLong(upperInput)) {
            JOptionPane.showMessageDialog(null, "Lower batch shouldn't be higher than the upper batch");
            return false;
        }

        JOptionPane.showMessageDialog(null, "Input is valid");
        return true;
    }





    public SystemStock(int ID, String username) {

        blankTable.setPreferredScrollableViewportSize(new Dimension(550, 500));
        blankScrollPane.setPreferredSize(new Dimension(500, 500));

        this.username = username;
        this.ID = ID;
        setContentPane(systemStockPage);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        // testing the git push







        ShowBlanks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) blankTable.getModel();
                model.setRowCount(0);

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query;
                    String selectedOption = (String) selectFilter.getSelectedItem();

                    switch (selectedOption) {
                        case "Sold Blanks":
                            query = "SELECT Blank.BlankNumber, Blank.Type, Blank.Employee_ID " +
                                    "FROM Blank " +
                                    "WHERE Blank.isSold = '1';";
                            model.setColumnIdentifiers(new String[]{"BlankNumber", "Type", "Employee_ID"});
                            break;
                        case "Assigned Blanks":
                            query = "SELECT Blank.BlankNumber, Blank.Type, Blank.Employee_ID, Blank.date_assign " +
                                    "FROM Blank " +
                                    "WHERE Blank.isAssigned = '1';";
                            model.setColumnIdentifiers(new String[]{"BlankNumber", "Type", "Employee_ID", "date_assign"});
                            break;
                        case "Unassigned Blanks":
                            query = "SELECT Blank.BlankNumber, Blank.Type " +
                                    "FROM Blank " +
                                    "WHERE Blank.isAssigned = '0';";
                            model.setColumnIdentifiers(new String[]{"BlankNumber", "Type"});
                        case "Blank Types":
                            query = "SELECT Blank.BlankNumber, Blank.Type " +
                                    "FROM Blank;";
                            model.setColumnIdentifiers(new String[]{"BlankNumber", "Type"});
                            break;
                        default:
                            return;
                    }

                    ResultSet rs = st.executeQuery(query);

                    while (rs.next()) {
                        String[] row;
                        switch (selectedOption) {
                            case "Sold Blanks":
                                row = new String[]{rs.getString("BlankNumber"), rs.getString("Type"), rs.getString("Employee_ID")};
                                break;
                            case "Assigned Blanks":
                                row = new String[]{rs.getString("BlankNumber"), rs.getString("Type"), rs.getString("Employee_ID"), rs.getString("date_assign")};
                                break;
                            case "Unassigned Blanks":
                                row = new String[]{rs.getString("BlankNumber"), rs.getString("Type")};
                                break;
                            case "Blank Types":
                                row = new String[]{rs.getString("BlankNumber"), rs.getString("Type")};
                                break;
                            default:
                                return;
                        }
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




        selectOfficeManagerID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT  Employee.Employee_ID FROM Employee where Employee.role = 'officeManager'";

                    ResultSet rs = st.executeQuery(query);
                    selectOfficeManagerID.removeAllItems();


                    while (rs.next()) {
                        String Id = rs.getString("Employee_ID");
                        selectOfficeManagerID.addItem(Id);
                    }

                    st.close();

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });



        SUBMITBLANKASSIGNButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateInput();
                String lowerRangeText = lowerRange.getText();
                String upperRangeText = upperRange.getText();
                String managerID = (String) selectOfficeManagerID.getSelectedItem();

                int lowerBound = Integer.parseInt(lowerRangeText);
                int upperBound = Integer.parseInt(upperRangeText);

                String blankType = (String) SelectblankType.getSelectedItem();
                String blankPrefix = (String) selectblankPrefix.getSelectedItem();

                try {
                    int assignDateBlank = Integer.parseInt(assignDate.getText().replace("/",""));

                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        String query = "INSERT INTO Blank (BlankNumber, Employee_ID, date_assign, Type, blank_prefix, isSold,isAssigned) VALUES (?, ?, ?, ?, ?, ?,?)";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        for (int i = lowerBound; i <= upperBound; i++) {
                            preparedStatement.setInt(1, i);
                            preparedStatement.setString(2, managerID);
                            preparedStatement.setInt(3, assignDateBlank);
                            preparedStatement.setString(4, blankType);
                            preparedStatement.setString(5, blankPrefix);
                            preparedStatement.setInt(6, 0);
                            preparedStatement.setInt(7,1);
                            preparedStatement.executeUpdate();


                            //preparedStatement.addBatch();
                        }
                        //preparedStatement.executeBatch();

                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });







        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemAdminHome homeButton = new SystemAdminHome(ID,username);
                homeButton.setVisible(true);
                dispose();

            }
        });

        manageUserDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDetails manageUserDetailsButton = new UserDetails(ID,username);
                manageUserDetailsButton.setVisible(true);
                dispose();

            }
        });

        manageCustomerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDetails manageCustomerDetailsButton = new CustomerDetails(ID,username);
                manageCustomerDetailsButton.setVisible(true);
                dispose();


            }
        });
        manageCommissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CommissionRates manageCommissionRatesButton = new CommissionRates(ID,username);
                manageCommissionRatesButton.setVisible(true);
                dispose();

            }
        });
        manageSystemStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemStock manageSystemStockButton = new SystemStock(ID,username);
                manageSystemStockButton.setVisible(true);
                dispose();


            }
        });
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateUser createUserButton = new CreateUser(ID,username);
                createUserButton.setVisible(true);
                dispose();

            }
        });


        SelectblankType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectblankType.addItem("Interline");
                SelectblankType.addItem("Domestic");
            }
        });
        selectblankPrefix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectblankPrefix.addItem("444");
                selectblankPrefix.addItem("440");
                selectblankPrefix.addItem("420");
                selectblankPrefix.addItem("201");
                selectblankPrefix.addItem("101");
                selectblankPrefix.addItem("101");
                selectblankPrefix.addItem("101");
            }
        });
    }

    public static void main(String[] args) {
        SystemStock systemStock = new SystemStock(ID,username);
        systemStock.show();
    }
}
