package Admin.CustomerDetails;

import Admin.Blanks.SystemStock;
import Admin.Commission.CommissionRates;
import Admin.Home.SystemAdminHome;


import Admin.UserDetails.UserDetails;
import Admin.UserDetails.CreateUser;
import Authentication.EnterDate;
import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerDetails extends javax.swing.JFrame {
    private JTable customerTable;
    private JButton homeButton;
    private JButton createUserButton;
    private JButton manageSystemStockButton;
    private JButton manageUserDetailsButton;
    private JButton manageCommissionRatesButton;
    private JButton manageCustomerDetailsButton;
    private JButton deleteCustomerButton;
    private JTextField customerInputText;
    private JButton searchCustomerButton;
    private JButton updateDetailsButton;
    private JComboBox accountTypeFilter;
    private DefaultTableModel model;
    private int selectedID;
    private JPanel mainPanel;
    private static int ID;
    private static String username;


    public CustomerDetails(int ID, String username){
        this.ID = ID;
        this.username = username;

        setContentPane(mainPanel);
        setSize(1500, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        model = (DefaultTableModel) customerTable.getModel();
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SystemAdminHome homeButton = new SystemAdminHome(ID, username, EnterDate.getDateToday());
                homeButton.setVisible(true);


            }
        });

        manageUserDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDetails manageUserDetailsButton = new UserDetails(ID, username);
                manageUserDetailsButton.setVisible(true);


            }
        });

        manageCustomerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerDetails manageCustomerDetailsButton = new CustomerDetails(ID, username);
                manageCustomerDetailsButton.setVisible(true);


            }
        });
        manageCommissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CommissionRates manageCommissionRatesButton = new CommissionRates(ID, username);
                manageCommissionRatesButton.setVisible(true);


            }
        });
        manageSystemStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SystemStock manageSystemStockButton = new SystemStock(ID, username);
                manageSystemStockButton.setVisible(true);



            }
        });
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateUser createUserButton = new CreateUser(ID, username);
                createUserButton.setVisible(true);


            }
        });


        searchCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT CustomerAccount.Customer_ID, CustomerAccount.Firstname, " +
                            " CustomerAccount.Surname, CustomerAccount.Email, " +
                            " CustomerAccount.PhoneNumber, CustomerAccount.Address, " +
                            "CustomerAccount.AccountType, CustomerAccount.DiscountType, CustomerAccount.current_Month, CustomerAccount.sales_this_month \n" +
                            "FROM CustomerAccount \n" +
                            "WHERE CustomerAccount.Firstname = '" + customerInputText.getText() + "' OR CustomerAccount.Surname = '" + customerInputText.getText() + "' ";
                    System.out.println(query);
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++) {
                        colName[i] = rsmd.getColumnName(i + 1);
                    }
                    model.setColumnIdentifiers(colName);
                    String  customer_ID, first_name, last_name, email, phoneNumber, address , accountType, discountType , current_Month, sales_this_month;
                    while (rs.next()) {
                        customer_ID = rs.getString(1);
                        first_name = rs.getString(2);
                        last_name = rs.getString(3);
                        email = rs.getString(4);
                        phoneNumber = rs.getString(5);
                        address = rs.getString(6);
                        accountType = rs.getString(7);
                        discountType = rs.getString(8);
                        current_Month = rs.getString(9);
                        sales_this_month = rs.getString(10);
                        String[] row = {customer_ID, first_name, last_name, email, phoneNumber, address , accountType, discountType , current_Month, sales_this_month};
                        model.addRow(row);
                    }
                    st.close();

                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        updateDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        accountTypeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
                    model.setRowCount(0);
                    String selected = (String) accountTypeFilter.getSelectedItem();
                    if (e.getSource() == accountTypeFilter) {
                        switch (selected) {
                            case "valued" -> {
                                displayCustomerTable("'valued'");
                            }
                            case "regular" -> {
                                displayCustomerTable("'regular'");
                            }
                            case "select all customers" -> {
                                displayCustomerTable("SELECT DISTINCT CustomerAccount.AccountType FROM CustomerAccount");
                            }

                        }

                    }
            }
        });
        deleteCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow(); // get the index of the selected row

                String message = "Are you sure you want to delete this customer?" +
                        " This is a crucial operation and the user will be deleted permanently.";


                // show a JOptionPane with YES_NO_OPTION and the warning message
                int option = JOptionPane.showConfirmDialog(null, message, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);


                if (selectedRow != -1) {
                    // check if the user clicked Yes
                    if (option == JOptionPane.YES_OPTION) {
                        // user clicked Yes, continue with the action


                        // get the value of the first column in the selected row
                        Object value = customerTable.getModel().getValueAt(selectedRow, 0);

                        // cast the value to the appropriate type (e.g. String or Integer)
                        String stringID = (String) value;

                        selectedID = Integer.parseInt(stringID);

                        try (Connection con = DBConnectivity.getConnection()) {
                            assert con != null;
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Statement st = con.createStatement();
                            String query = "DELETE FROM CustomerAccount " +
                                    " WHERE Customer_ID = '" + selectedID + "'";
                            ;
                            System.out.println(query);
                            int rs = st.executeUpdate(query);

                            st.close();
                        } catch (SQLException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // user clicked No, cancel the action
                        // ...
                    }
                }else {
                    // no row is selected, show an error message or do nothing
                    JOptionPane.showMessageDialog(null, "Please select a row from the table.");
                }
            }
        });
    }

    private void displayCustomerTable(String constraint) {
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);
        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT CustomerAccount.Customer_ID, CustomerAccount.Firstname, " +
                    " CustomerAccount.Surname, CustomerAccount.Email, " +
                    " CustomerAccount.PhoneNumber, CustomerAccount.Address, " +
                    "CustomerAccount.AccountType, CustomerAccount.DiscountType, CustomerAccount.current_Month, CustomerAccount.sales_this_month \n" +
                    "FROM CustomerAccount \n" +
                    "WHERE CustomerAccount.AccountType IN (" +constraint+ ") ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];
            for (int i = 0; i < cols; i++) {
                colName[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colName);
            String  customer_ID, first_name, last_name, email, phoneNumber, address , accountType, discountType , current_Month, sales_this_month;
            while (rs.next()) {
                customer_ID = rs.getString(1);
                first_name = rs.getString(2);
                last_name = rs.getString(3);
                email = rs.getString(4);
                phoneNumber = rs.getString(5);
                address = rs.getString(6);
                accountType = rs.getString(7);
                discountType = rs.getString(8);
                current_Month = rs.getString(9);
                sales_this_month = rs.getString(10);
                String[] row = {customer_ID, first_name, last_name, email, phoneNumber, address , accountType, discountType , current_Month, sales_this_month};
                model.addRow(row);
            }
            st.close();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[]args){
        CustomerDetails customerDetails = new CustomerDetails(ID, username);
        customerDetails.show();
    }

}
