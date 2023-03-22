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
    private JButton HomeButton;
    private JButton showBlanksButton;
    private JScrollPane stockTableScroll;
    private JPanel blankTypePanel;
    private static int ID;
    private static String username;
    private JComboBox blankType;

    public AdvisorStock(int ID, String username) {
        this.ID = ID;
        this.username = username;
        stockTableScroll.setPreferredSize(new Dimension(500,500));
        stockTable.setPreferredScrollableViewportSize(new Dimension(500,500));
        setContentPane(stockPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        String[] options = new String[]{"All Blanks","MCO","Interline","Domestic"};
        blankType = new JComboBox<>(options);
        blankTypePanel.add(blankType);


        showBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBlankTable("SELECT DISTINCT Blank.Type FROM Blank");
            }
        });
        HomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID,username);
                advisorHome.show();

            }
        });
        blankType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
                model.setRowCount(0);
                System.out.println(e.getSource()+"57697");
                String selected = (String) blankType.getSelectedItem();
                if(e.getSource() == blankType){
                    switch (selected) {
                        case "Interline" -> {
                            displayBlankTable("'Interline'");

                        }

                        case "Domestic" -> {
                         displayBlankTable("'Domestic'");
                        }
                        case "MCO" ->{
                            displayBlankTable("'MCO'");
                        }
                        case "All Blanks" ->{
                            displayBlankTable("SELECT DISTINCT Blank.Type FROM Blank");
                        }

                    }

                }

            }
        });
    }

    public void displayBlankTable(String blankConstraint){
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.setRowCount(0);
        try(Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Employee.First_name, Employee.Last_name, Blank.blankNumber, Blank.Type \n" +
                    "FROM Blank \n" +
                    "INNER JOIN Employee \n" +
                    "ON Blank.Employee_ID = Employee.Employee_ID\n" +
                    "WHERE Employee.Employee_ID = '"+ID+"' AND Blank.Type IN ("+blankConstraint+") ";
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

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    public static void main(String[] args){
        AdvisorStock advisorStock = new AdvisorStock(ID,username);
        advisorStock.show();

    }
}
