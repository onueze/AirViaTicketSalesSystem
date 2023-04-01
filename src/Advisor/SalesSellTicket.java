package Advisor;

import DB.DBConnectivity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SalesSellTicket extends javax.swing.JFrame {
    private JTextField departureText;
    private JTextField arrivalText;
    private JComboBox selectBlankType;
    private JTable blankTable;
    private JButton searchFlightsButton;
    private JTable flightTable;
    private JPanel titelpanel;
    private JPanel flightsInfoPanel;
    private JPanel mainPanel;
    private JButton showBlanksButton;
    private JButton continueButton;
    private JPanel filterPanel;
    private JComboBox selectBlank;
    private static int ID;
    private static String username;
    private static ResultSet customerID;

    public SalesSellTicket(int ID, String username, ResultSet customerID) {
        SalesSellTicket.ID = ID;
        SalesSellTicket.username = username;
        SalesSellTicket.customerID = customerID;
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
//        String[] options = new String[]{"All Blanks","MCO","Interline","Domestic"};
//        selectBlank = new JComboBox<>(options);
//        filterPanel.add(selectBlank);



        searchFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String departure = departureText.getText();
                String arrival = arrivalText.getText();
                DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
                model.setRowCount(0);
                try(Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT Flight.number, Flight.departure, Flight.destination, Flight.depTime, Flight.arrTime,\n" +
                            " Flight.price, Flight.Airline, Flight.F_Date\n" +
                            "FROM Flight \n" +
                            "WHERE Flight.departure = '"+departure+"' AND Flight.destination = '"+arrival+"' ";
                    System.out.println(query);
                    ResultSet rs = st.executeQuery(query);
                    ResultSetMetaData rsmd = rs.getMetaData();

                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for(int i = 0; i < cols; i++){
                        colName[i] = rsmd.getColumnName(i+1);
                    }
                    model.setColumnIdentifiers(colName);
                    String flightNumber,  flightDeparture, flightArrival, flightDestination, flightDepTime, flightArrtime,
                            price, airline, flightDate;
                    while(rs.next()){
                        flightNumber = rs.getString(1);
                        flightDeparture  = rs.getString(2);
                        flightArrival = rs.getString(3);
                        flightDepTime = rs.getString(4);
                        flightArrtime  = rs.getString(5);
                        price = rs.getString(6);
                        airline  = rs.getString(7);
                        flightDate = rs.getString(8);
                        String[] row = {flightNumber,  flightDeparture, flightArrival, flightDepTime, flightArrtime,
                                price, airline, flightDate};
                        model.addRow(row);
                    }
                    st.close();

                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });

        selectBlankType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) blankTable.getModel();
                model.setRowCount(0);
                String selected = (String) selectBlankType.getSelectedItem();
                if(e.getSource() == selectBlankType){
                    switch (selected) {
                        case "Interline" -> {
                            displayBlankTable("'Interline'");
                        }
                        case "Domestic" -> {
                            displayBlankTable("'Domestic'");
                        }
                        case "MCO" -> {
                            displayBlankTable("'MCO'");
                        }
                        case "All Blanks" ->{
                            displayBlankTable("SELECT DISTINCT Blank.Type FROM Blank");
                        }

                    }

                }
            }
        });
        showBlanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBlankTable("SELECT DISTINCT Blank.Type FROM Blank");
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowBlank = blankTable.getSelectedRow();
                int selectedRowFlight = flightTable.getSelectedRow();

                if (selectedRowBlank != -1 && selectedRowFlight != -1) {
                    // A row has been selected
                    // Perform your desired action here
                    // PaymentsPage
                } else {
                    // No row has been selected
                    // Handle this case as needed
                    JOptionPane.showMessageDialog(mainPanel,"Please select both Flight and desired Blank. If there are no available /n" +
                            "Flights please let the customer know");
                }
            }
        });
    }

    public void displayBlankTable(String blankConstraint){
        DefaultTableModel model = (DefaultTableModel) blankTable.getModel();
        model.setRowCount(0);
        try(Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Blank.blankNumber, Blank.Type \n" +
                    "FROM Blank \n" +
                    "WHERE Blank.Employee_ID = '"+ID+"' AND Blank.Type IN ("+blankConstraint+") ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int cols = rsmd.getColumnCount();
            String[] colName = new String[cols];
            for(int i = 0; i < cols; i++){
                colName[i] = rsmd.getColumnName(i+1);
            }
            model.setColumnIdentifiers(colName);
            String blankNumber,blankType;
            while(rs.next()){
                blankNumber = rs.getString(1);
                blankType  = rs.getString(2);
                String[] row = {blankNumber,blankType};
                model.addRow(row);
            }
            st.close();

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void main (String[] args){
        SalesSellTicket salesSellTicket = new SalesSellTicket(ID, username,customerID);
        salesSellTicket.show();
    }
}
