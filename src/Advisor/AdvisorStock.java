package Advisor;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdvisorStock extends javax.swing.JFrame {
    private JTable stockTable;
    private JPanel stockPage;
    private JComboBox blankType;
    private JButton HomeButton;
    private JButton showBlanksButton;
    private JScrollPane stockTableScroll;
    private static int ID;

    public AdvisorStock(int ID) {
        this.ID = ID;
        stockTableScroll.setPreferredSize(new Dimension(500,500));
        stockTable.setPreferredScrollableViewportSize(new Dimension(500,500));
        setContentPane(stockPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        showBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try(Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT Employee.First_name, Employee.Last_name, Blank.blankNumber, Blank.Type \n" +
                            "FROM Blank \n" +
                            "INNER JOIN Employee \n" +
                            "ON Blank.Employee_ID = Employee.Employee_ID\n" +
                            "WHERE Employee.Employee_ID = '"+ID+"' ";
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) stockTable.getModel();

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

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args){
        AdvisorStock advisorStock = new AdvisorStock(ID);
        advisorStock.show();

    }
}
