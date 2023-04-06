package Admin;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserDetails {
    private JPanel mainPanel;
    private JTable userTable;
    private JButton deleteUserButton;
    private JButton changeAccessRoleButton;
    private JComboBox roleCombobox;
    private JTextField employeeIDText;
    private JButton homeButton;
    private JButton createUserButton;
    private JButton manageSystemStockButton;
    private JButton manageCommissionRatesButton;
    private JButton manageCustomerDetailsButton;
    private JButton manageUserDetailsButton;

    public UserDetails() {
        roleCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) userTable.getModel();
                model.setRowCount(0);
                String selected = (String) roleCombobox.getSelectedItem();
                if(e.getSource() == roleCombobox){
                    switch (selected) {
                        case "advisor" -> {
                            displayUserTable("'Interline'");
                        }
                        case "admin" -> {
                            displayUserTable("'Domestic'");
                        }
                        case "officeManager" ->{
                            displayUserTable("'MCO'");
                        }
                        case "select role" ->{
                            displayUserTable("SELECT DISTINCT Blank.Type FROM Blank");
                        }

                    }

                }
            }
        });
    }

    public void displayUserTable(String blankConstraint){
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        try(Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Employee.First_name, Employee.Last_name, Blank.blankNumber, Blank.Type \n" +
                    "FROM Employee \n" +
                    "WHERE Employee.Employee_ID = '"+userTable+"' AND Employee.role IN ("+blankConstraint+") ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];
            for(int i = 0; i < cols; i++){
                colName[i] = rsmd.getColumnName(i+1);
            }
            model.setColumnIdentifiers(colName);
            String first_name,last_name,blankNumber,blankType;
            while(rs.next()){
                first_name = rs.getString(1);
                last_name = rs.getString(2);
                blankNumber = rs.getString(3);
                blankType = rs.getString(4);
                String[] row = {first_name,last_name,blankNumber,blankType};
                model.addRow(row);
            }
            st.close();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

    }
}
