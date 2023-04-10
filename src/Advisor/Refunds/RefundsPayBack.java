package Advisor.Refunds;

import DB.DBConnectivity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RefundsPayBack extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton processRefundButton;
    private JLabel cardNumberText;
    private JButton checkButton;
    private int cardNumber;
    private static Document document;
    private int saleID;
    private int commissionID;
    private int ID;
    private static String username;
    private static int customerID;
    private static int currentDate;
    private static String paymentType;
    private static int blankNumber;
    private static int ticketID;
    private static float price;

    public RefundsPayBack(int ID, String username, int customerID, int currentDate,
                          String paymentType, int blankNumber, int ticketID, int saleID, int commissionID, float price) {
        this.ID = ID;
        this.username = username;
        this.customerID = customerID;
        this.currentDate = currentDate;
        this.paymentType = paymentType;
        this.blankNumber = blankNumber;
        this.ticketID = ticketID;
        this.saleID = saleID;
        this.commissionID = commissionID;

        setContentPane(mainPanel);
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = " SELECT Card_Number FROM" +
                    " Card_Details " +
                    "WHERE Customer_ID = '"+ customerID +"' ";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                cardNumber = rs.getInt("Card_Number");
            }
            st.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }


        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardNumberText.setText(String.valueOf(cardNumber));
            }
        });

        processRefundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "INSERT INTO Refund SELECT" +
                            "(SELECT COALESCE(MAX(Refund_ID), 0) + 1 FROM Refund), '"+currentDate+"','"+customerID+"'," +
                            "'"+saleID+"'," +
                            "'"+ID+"', '"+commissionID+"', 1,";
                    System.out.println(query);
                    int insert = st.executeUpdate(query);

                    st.close();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }


                try{
//                    document = new Document();
//                    PdfWriter.getInstance(document, new FileOutputStream("/Users/alexelemele/Documents/testPDF.pdf"));
//
//                    document.open();
//
//// Create a header with "AirVia Ltd" in bold font
//                    Paragraph header = new Paragraph("AirVia Ltd", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
//                    header.setAlignment(Element.ALIGN_CENTER);
//                    document.add(header);
//
//                    Paragraph message = new Paragraph("Dear Customer, your Ticket with blank number ");
//                    message.setAlignment(Element.ALIGN_CENTER);
//                    message.add(new Phrase(Chunk.NEWLINE));
//                    document.add(message);
//
//                    Paragraph customerInfo = new Paragraph();
//                    customerInfo.setAlignment(Element.ALIGN_CENTER);
//                    customerInfo.add(new Phrase("Customer ID:          " + customerID + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("First and Last name:          " + firstName + " " + surname + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("blankNumber:          " + blankNumber + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("blankType:          " + blankType + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("flightNumber:          " + flightID + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("Airline:          " + airline + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("departure Airport:          " + flightDeparture + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("arrival Airport:          " + flightArrival + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("Date of departure:          " + flightDate + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("Departure time:          " + flightDepTime + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("Arrival time:          " + flightArrtime + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("payment Period:          " + paymentPeriod + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("payment Method:         " + paymentType + "\n"));
//                    customerInfo.add(new Phrase(Chunk.NEWLINE));
//                    customerInfo.add(new Phrase("total price:          " + price + "\n"));
//                    document.add(customerInfo);
//
//                    document.close();


                    PdfReader reader = new PdfReader("logfile.pdf");
                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("logfile.pdf"));
                    Document document = new Document();
                    stamper.getOverContent(1).beginText();
                    stamper.getOverContent(1).showTextAligned(com.itextpdf.text.Element.ALIGN_LEFT,
                            "Refund of blankNumber: " + blankNumber + " to Customer " + customerID + " on date " +
                                    " " + currentDate, 36, 788, 0);
                    stamper.getOverContent(1).endText();
                    stamper.close();
                    reader.close();



                } catch (DocumentException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
