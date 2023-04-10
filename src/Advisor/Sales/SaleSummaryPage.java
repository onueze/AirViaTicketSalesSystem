package Advisor.Sales;

import DB.DBConnectivity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;

public class SaleSummaryPage extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton voidTicketButton;
    private JButton payButton;
    private JLabel CustomerNamesLabel;
    private JLabel blankNumberLabel;
    private JLabel blankTypeLabel;
    private JLabel flightNumberLabel;
    private JLabel airlineLabel;
    private JLabel departureLabel;
    private JLabel arrivalLabel;
    private JLabel departureDateLabel;
    private JLabel departureTimeLabel;
    private JLabel arrivalTimeLabel;
    private JLabel paymentPeriodLabel;
    private JLabel paymentMethodLabel;
    private JLabel totalPriceLabel;
    private JLabel customerIDLabel;
    private static int ID;
    private static String username;
    private static float price;
    private static int flightID;
    private static String paymentPeriod;
    private static String paymentType;
    private static int blankNumber;
    private static String blankType;
    private static int customerID;
    private String firstName;
    private String surname;
    private String flightDeparture;
    private String flightArrival;
    private String flightDepTime;
    private String flightArrtime;
    private String airline;
    private String flightDate;
    private int ticketID;
    private static int date;
    private static int currencyID;
    private static Document document;



    public SaleSummaryPage(int ID, String username, int customerID, float price,
                           int flightID, String paymentPeriod, String paymentType, int blankNumber, String blankType, int date, int currencyID){
        this.ID = ID;
        this.username = username;
        this.price = price;
        this.flightID = flightID;
        this.paymentPeriod = paymentPeriod;
        this.paymentType = paymentType;
        this.blankNumber = blankNumber;
        this.blankType = blankType;
        this.customerID = customerID;
        this.date = date;
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        customerIDLabel.setText(Integer.toString(customerID));
        blankNumberLabel.setText(Integer.toString(blankNumber));
        blankTypeLabel.setText(blankType);
        flightNumberLabel.setText(Integer.toString(flightID));
        paymentPeriodLabel.setText(paymentPeriod);
        paymentMethodLabel.setText(paymentType);
        totalPriceLabel.setText(Float.toString(price));


        try(Connection con = DBConnectivity.getConnection()){
            Class.forName("com.mysql.cj.jdbc.Driver");

            Statement stCustName = con.createStatement();
            String queryCustName = "SELECT Firstname,Surname  " +
                    "FROM CustomerAccount " +
                    "WHERE CustomerAccount.Customer_ID = '" + customerID + "' ";
            ResultSet rsCustName = stCustName.executeQuery(queryCustName);

            if(rsCustName.next()) {
                firstName = rsCustName.getString(1);
                surname = rsCustName.getString(2);
            }
            CustomerNamesLabel.setText(firstName + " " + surname);

            stCustName.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Flight.departure, Flight.destination, Flight.depTime, Flight.arrTime, " +
                    " Flight.Airline, Flight.F_Date " +
                    "FROM Flight \n" +
                    "WHERE Flight.number  = '"+flightID+"' ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);

            if(rs.next()) {
                flightDeparture = rs.getString(1);
                flightArrival = rs.getString(2);
                flightDepTime = rs.getString(3);
                flightArrtime = rs.getString(4);
                airline = rs.getString(5);
                flightDate = rs.getString(6);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        departureLabel.setText(flightDeparture);
        arrivalLabel.setText(flightArrival);
        departureTimeLabel.setText(flightDepTime);
        arrivalTimeLabel.setText(flightArrtime);
        airlineLabel.setText(airline);
        departureDateLabel.setText(flightDate);

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int dialog = JOptionPane.showConfirmDialog(mainPanel, "Do you want to continue and complete the payment?");
                if (dialog == JOptionPane.YES_OPTION) {
                    // User clicked the "Yes" button
                    // Do something here
                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Statement st = con.createStatement();
                        String query = "INSERT INTO Ticket SELECT " +
                                "(SELECT COALESCE(MAX(TicketID), 0) + 1 FROM Ticket), '"+blankNumber+"', '"+flightID+"'";
                        System.out.println(query);
                        int rowsInserted = st.executeUpdate(query);

                        st.close();
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }


                    try (Connection con = DBConnectivity.getConnection()) {
                        assert con != null;
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Statement st = con.createStatement();
                        String query = "SELECT TicketID FROM Ticket " +
                                "WHERE blankNumber = '"+blankNumber+"'";
                        ResultSet rs = st.executeQuery(query);

                        if(rs.next()){
                            ticketID = rs.getInt("TicketID");
                            System.out.println("ticketID is: " + ticketID);
                        }


                        st.close();
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    try{
                        document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream("/Users/alexelemele/Documents/testPDF.pdf"));

                        document.open();

// Create a header with "AirVia Ltd" in bold font
                        Paragraph header = new Paragraph("AirVia Ltd", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
                        header.setAlignment(Element.ALIGN_CENTER);
                        document.add(header);

                        Paragraph message = new Paragraph("Dear " + firstName + " " + surname + "," +
                                " this is your receipt for flight number " + flightID + " from " + flightDeparture + " to " + flightArrival + ".");
                        message.setAlignment(Element.ALIGN_CENTER);
                        message.add(new Phrase(Chunk.NEWLINE));
                        document.add(message);

                        Paragraph customerInfo = new Paragraph();
                        customerInfo.setAlignment(Element.ALIGN_CENTER);
                        customerInfo.add(new Phrase("Customer ID:          " + customerID + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("First and Last name:          " + firstName + " " + surname + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("blankNumber:          " + blankNumber + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("blankType:          " + blankType + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("flightNumber:          " + flightID + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("Airline:          " + airline + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("departure Airport:          " + flightDeparture + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("arrival Airport:          " + flightArrival + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("Date of departure:          " + flightDate + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("Departure time:          " + flightDepTime + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("Arrival time:          " + flightArrtime + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("payment Period:          " + paymentPeriod + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("payment Method:         " + paymentType + "\n"));
                        customerInfo.add(new Phrase(Chunk.NEWLINE));
                        customerInfo.add(new Phrase("total price:          " + price + "\n"));
                        document.add(customerInfo);

                        document.close();



                    } catch (DocumentException | FileNotFoundException ex) {
                        ex.printStackTrace();
                    }



                    if (paymentType.equals("card")) {
                        dispose();
                        SalesCardPayNow salesCardPayNow = new SalesCardPayNow(ID, username, customerID, price, blankNumber,
                                blankType, paymentPeriod, paymentType,ticketID,date,currencyID,document);
                    }
                    else if(paymentType.equals("cash")) {
                        dispose();
                        SalesCashPayNow salesCashPayNow = new SalesCashPayNow(ID, username,customerID,price,blankNumber,
                                blankType,paymentPeriod,paymentType,ticketID,date,currencyID,document);
                    }

                } else {


                }
            }
        });

        voidTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    public static void main(String[]args){
        SaleSummaryPage saleSummaryPage = new SaleSummaryPage(ID,  username,customerID,  price,
        flightID, paymentPeriod, paymentType, blankNumber, blankType,date, currencyID);
        saleSummaryPage.show();
    }
}
