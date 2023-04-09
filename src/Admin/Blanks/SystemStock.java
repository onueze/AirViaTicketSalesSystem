package Admin.Blanks;

import Admin.Commission.CommissionRates;
import Admin.CustomerDetails.CustomerDetails;
import Admin.Home.SystemAdminHome;
import Admin.UserDetails.UserDetails;
import Admin.UserDetails.CreateUser;
import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private JComboBox blankType;


    private static int ID;
    private static String username;





    public SystemStock(int ID, String username) {

        blankTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        blankScrollPane.setPreferredSize(new Dimension(500, 500));

        this.username = username;
        this.ID = ID;
        setContentPane(systemStockPage);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);







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

        //JTextField lowerRange = new JTextField(9);
        //JTextField upperRange = new JTextField(9);


        SUBMITBLANKASSIGNButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lowerRangeText = lowerRange.getText();
                String upperRangeText= upperRange.getText();
                String typeB = (String) blankType.getSelectedItem();
                String managerID = (String) selectOfficeManagerID.getSelectedItem();


                try {


                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Statement st = con.createStatement();
                        String query = "Insert into Blank(BlankNumber,Type,Employee_ID) WHERE BlankNumber = ?,?,Type = ?,Employee_ID = ? ";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1,lowerRangeText);
                        preparedStatement.setString(1,upperRangeText);
                        preparedStatement.setString(3,typeB);
                        preparedStatement.setString(4,managerID);

                        preparedStatement.executeUpdate();


                        st.close();

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
                SystemAdminHome homeButton = new SystemAdminHome(ID, username);
                homeButton.setVisible(true);
                dispose();

            }
        });

        manageUserDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDetails manageUserDetailsButton = new UserDetails(ID, username);
                manageUserDetailsButton.setVisible(true);
                dispose();

            }
        });

        manageCustomerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDetails manageCustomerDetailsButton = new CustomerDetails(ID, username);
                manageCustomerDetailsButton.setVisible(true);
                dispose();


            }
        });
        manageCommissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CommissionRates manageCommissionRatesButton = new CommissionRates(ID, username);
                manageCommissionRatesButton.setVisible(true);
                dispose();

            }
        });
        manageSystemStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemStock manageSystemStockButton = new SystemStock(ID, username);
                manageSystemStockButton.setVisible(true);
                dispose();


            }
        });
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateUser createUserButton = new CreateUser(ID, username);
                createUserButton.setVisible(true);
                dispose();

            }
        });



    }

    public static void main(String[] args) {
        SystemStock systemStock = new SystemStock(ID, username);
        systemStock.show();
    }
}