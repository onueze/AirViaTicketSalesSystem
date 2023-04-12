package Advisor.Sales;

import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OutstandingPayment extends javax.swing.JFrame {

    private JTextField blankNumberText;
    private JButton continueButton;
    private JButton backButton;
    private JPanel mainPanel;
    private boolean blankFound;
    private int saleID;
    private float amount;
    private int paymentDate;
    private String paymentType;
    private int employeeID;
    private int currencyCode;
    private int customerID;
    private int commissionID;
    private int ticketID;
    private int refundID;
    private int blankNumber;
    private int ID;
    private String username;
    private String blankType;
    private Document document;


    public OutstandingPayment(int ID, String username) {
        this.ID = ID;
        this.username = username;
        setContentPane(mainPanel);
        setSize(1000,600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (Connection con = DBConnectivity.getConnection()) {
                    String blankStr = blankNumberText.getText();
                    blankStr = blankStr.replace(" ","");
                    int blankNo = Integer.parseInt(blankStr);
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT Sale.Sale_ID, Sale.Amount, Sale.Payment_Date, Blank.Type, " +
                            " Sale.PaymentType, Sale.Employee_ID, Sale.Currency_Code," +
                            " Sale.Customer_ID, Sale.Commission_ID, Sale.TicketID, Sale.Refund_ID, Sale.BlankNumber  " +
                            "FROM Sale \n" +
                            "JOIN Blank " +
                            "ON Sale.BlankNumber = Blank.BlankNumber " +
                            "WHERE Sale.BlankNumber  = '" + blankNo  + "' AND paylater = 'pay later' ";
                    System.out.println(query);
                    ResultSet rs = st.executeQuery(query);


                    if(!rs.next()){
                        blankFound = false;
                        JOptionPane.showMessageDialog(mainPanel,"blank number not found or pay later not applicable");
                    }
                    else {
                        saleID = rs.getInt("Sale_ID");
                        amount = rs.getFloat("Amount");
                        paymentDate = rs.getInt("Payment_Date");
                        paymentType = rs.getString("PaymentType");
                        employeeID = rs.getInt("Employee_ID");
                        currencyCode = rs.getInt("Currency_Code");
                        customerID = rs.getInt("Customer_ID");
                        commissionID = rs.getInt("Commission_ID");
                        ticketID = rs.getInt("TicketID");
                        refundID = rs.getInt("Refund_ID");
                        blankNumber = rs.getInt("BlankNumber");
                        blankType = rs.getString("Type");
                        blankFound = true;

                        JOptionPane.showMessageDialog(mainPanel,"pay later eligible");

                        int optionSelected = JOptionPane.showOptionDialog(null, "Select payment method:", "Payment Method", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Card", "Cash" }, "Card");
                        String paymentMethod = "";
                        if (optionSelected == JOptionPane.YES_OPTION) {
                            paymentMethod = "Card";

                            try {
                                document = new Document();
                                PdfWriter.getInstance(document, new FileOutputStream("/Users/alexelemele/Documents/testPDF.pdf"));

                                document.open();

                                // Create a header with "AirVia Ltd" in bold font
                                Paragraph header = new Paragraph("AirVia Ltd", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
                                header.setAlignment(Element.ALIGN_CENTER);
                                document.add(header);

                                Paragraph message = new Paragraph("Dear Customer,\n" +
                                        "\n" +
                                        "I would like to take this opportunity to thank you for choosing Air Via for your recent travel needs. We hope you had a comfortable and pleasant journey with us.\n" +
                                        "\n" +
                                        "Your satisfaction is our top priority, and we are glad to know that we were able to provide you with a safe and enjoyable flight experience. We appreciate your business and look forward to serving you again in the future.\n" +
                                        "\n" +
                                        "If you have any feedback or suggestions for us, please do not hesitate to reach out to us. We value your opinions and would love to hear from you.\n" +
                                        "\n" +
                                        "Thank you once again for choosing Air Via. We hope to see you on board with us soon.\n" +
                                        "\n" +
                                        "Best regards,\n" +
                                        "\n" +
                                        "Air Via Customer Service");
                                message.setAlignment(Element.ALIGN_CENTER);
                                message.add(new Phrase(Chunk.NEWLINE));
                                document.add(message);

                                Paragraph customerInfo = new Paragraph();
                                customerInfo.setAlignment(Element.ALIGN_CENTER);
                                customerInfo.add(new Phrase("Customer ID:          " + customerID + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("blankNumber:          " + blankNumber + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("blankType:          " + blankType + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("payment Period:   pay now     " + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("payment Method:         " + paymentType + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("total price:          " + amount + "\n"));
                                document.add(customerInfo);

                                document.close();


                            } catch (DocumentException | FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                            dispose();
                            SalesCardPayNow salesCardPayNow = new SalesCardPayNow(ID,username,customerID,amount,blankNumber,blankType, "pay now","card", ticketID, paymentDate, currencyCode,document);
                        } else if (optionSelected == JOptionPane.NO_OPTION) {
                            paymentMethod = "Cash";

                            try {
                                document = new Document();
                                PdfWriter.getInstance(document, new FileOutputStream("/Users/alexelemele/Documents/testPDF.pdf"));

                                document.open();

                                // Create a header with "AirVia Ltd" in bold font
                                Paragraph header = new Paragraph("AirVia Ltd", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
                                header.setAlignment(Element.ALIGN_CENTER);
                                document.add(header);

                                Paragraph message = new Paragraph("Dear Customer,\n" +
                                        "\n" +
                                        "I would like to take this opportunity to thank you for choosing Air Via for your recent travel needs. We hope you had a comfortable and pleasant journey with us.\n" +
                                        "\n" +
                                        "Your satisfaction is our top priority, and we are glad to know that we were able to provide you with a safe and enjoyable flight experience. We appreciate your business and look forward to serving you again in the future.\n" +
                                        "\n" +
                                        "If you have any feedback or suggestions for us, please do not hesitate to reach out to us. We value your opinions and would love to hear from you.\n" +
                                        "\n" +
                                        "Thank you once again for choosing Air Via. We hope to see you on board with us soon.\n" +
                                        "\n" +
                                        "Best regards,\n" +
                                        "\n" +
                                        "Air Via Customer Service");
                                message.setAlignment(Element.ALIGN_CENTER);
                                message.add(new Phrase(Chunk.NEWLINE));
                                document.add(message);

                                Paragraph customerInfo = new Paragraph();
                                customerInfo.setAlignment(Element.ALIGN_CENTER);
                                customerInfo.add(new Phrase("Customer ID:          " + customerID + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("blankNumber:          " + blankNumber + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("blankType:          " + blankType + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("payment Period:   pay now     " + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("payment Method:         " + paymentType + "\n"));
                                customerInfo.add(new Phrase(Chunk.NEWLINE));
                                customerInfo.add(new Phrase("total price:          " + amount + "\n"));
                                document.add(customerInfo);

                                document.close();


                            } catch (DocumentException | FileNotFoundException ex) {
                                ex.printStackTrace();
                            }

                            dispose();
                            SalesCashPayNow salesCashPayNow = new SalesCashPayNow(ID,username,customerID,amount,blankNumber,blankType, "pay now","card", ticketID, paymentDate, currencyCode,document);
                            System.out.println("sales cash offen");
                        }
                        System.out.println("Payment method selected: " + paymentMethod);

                    }


                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TravelAdvisorHome travelAdvisorHome = new TravelAdvisorHome(ID,username);
            }
        });
        blankNumberText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
    }
}
