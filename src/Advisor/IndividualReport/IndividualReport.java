package Advisor.IndividualReport;

import Advisor.Home.TravelAdvisorHome;
import DB.DBConnectivity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IndividualReport extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton domesticSalesReportButton;
    private JButton homeButton;
    private JButton stocksButton;
    private JButton ticketStockTurnOverButton;
    private JButton blanksButton;
    private JButton discountPlanButton;
    private JButton interlineSalesReportButton;
    private JButton advisorIndividualReportButton;
    private JPanel individualReportPage;
    private JButton produceAdvisorIndivudialReportButton;
    private JComboBox chooseAdvisorName;
    private JComboBox selectSaleType;
    private JButton viewProducedReportButton;
    private JButton notificationButton;
    private JTextField dateForReport;
    private JTextField dateToReport;
    private JButton viewDateButton;

    private static int ID;
    private static String username;
    private String saleType;
    /*
    private void startRefundNotificationWatcher() {
        Thread refundWatcherThread = new Thread(() -> {
            while (true) {
                String message = refundMessage();
                if (!message.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        refundMessage();
                    });
                }
                try {
                    // Check for new records every minute (60000 ms)
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        refundWatcherThread.setDaemon(true);
        refundWatcherThread.start();
    }
 */




    public IndividualReport(int ID, String username) {


        this.username = username;
        this.ID = ID;

        setContentPane(individualReportPage);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        String FileName = "Advisor Individual Report";

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                TravelAdvisorHome advisorHome = new TravelAdvisorHome(ID, username);
                advisorHome.setVisible(true);

            }
        });

        selectSaleType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSaleType.addItem("Interline");
                selectSaleType.addItem("Domestic");
            }
        });


        produceAdvisorIndivudialReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saleType = (String) selectSaleType.getSelectedItem();

                try (Connection con = DBConnectivity.getConnection()) {
                    int dateForReportInitail = Integer.parseInt(dateForReport.getText().replace("/",""));
                    int dateForReportTo = Integer.parseInt(dateToReport.getText().replace("/",""));

                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    String query = "SELECT " +
                            "  Sale.Employee_ID, " +
                            "  Blank.BlankNumber, " +
                            "  Sale.Payment_Date, " +
                            "  ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate), 2) AS TotalSaleAmount, " +
                            "  ROUND(SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS TotalCommissionAmount, " +
                            "  ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS NetSaleAmount " +
                            "FROM " +
                            "  Sale " +
                            "  JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "  JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "  JOIN Currency_Code ON Sale.Currency_Code = Currency_Code.Currency_Code " +
                            "WHERE " +
                            "  Sale.Employee_ID = ? " +
                            "  AND Blank.Type = ? " +
                            "  AND DATE(Sale.Payment_Date) BETWEEN ? AND ? " +
                            "GROUP BY " +
                            "  Sale.Employee_ID, " +
                            "  Blank.BlankNumber;";

                    System.out.println(query);
                    System.out.println(ID);
                    System.out.println(saleType);
                    System.out.println(dateForReportInitail);
                    System.out.println(dateForReportTo);

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, String.valueOf(ID));
                    preparedStatement.setString(2, saleType);
                    preparedStatement.setInt(3, dateForReportInitail);
                    preparedStatement.setInt(4, dateForReportTo);



                    String additionalQuery = "SELECT ROUND(SUM(NetSaleAmount), 2) AS TotalNetSaleAmount, ROUND(SUM(TotalCommissionAmount), 2) AS TotalOverallCommissionEarned FROM (" +
                            "SELECT Sale.Employee_ID, " +
                            "       ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate), 2) AS TotalSaleAmount, " +
                            "       ROUND(SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS TotalCommissionAmount, " +
                            "       ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS NetSaleAmount " +
                            "FROM Sale " +
                            "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "JOIN Currency_Code ON Sale.Currency_Code = Currency_Code.Currency_Code " +
                            "WHERE Sale.Employee_ID = ? AND Blank.Type = ? AND DATE(Sale.Payment_Date) BETWEEN ? AND ? " +
                            "GROUP BY Sale.Employee_ID" +
                            ") AS subquery;";


                    PreparedStatement preparedStatementA = con.prepareStatement(additionalQuery);
                    preparedStatementA.setString(1, String.valueOf(ID));
                    preparedStatementA.setString(2, saleType);
                    preparedStatementA.setInt(3, dateForReportInitail);
                    preparedStatementA.setInt(4, dateForReportTo);


                    ResultSet rs = preparedStatement.executeQuery();
                    ResultSet rsAdditional = preparedStatementA.executeQuery();

                    Path pdfPath = Paths.get("/Users/alexelemele/Downloads/AirViaTicketSalesSystem/data/RefundEmail.pdf");
                    if (Files.exists(pdfPath)) {
                        Files.delete(pdfPath);
                    }


                    Document PDFdoc = new Document(PageSize.A4.rotate());
                    PdfWriter.getInstance(PDFdoc, new FileOutputStream("/Users/alexelemele/Downloads/AirViaTicketSalesSystem/data/RefundEmail.pdf"));
                    PDFdoc.open();
                    PDFdoc.addHeader("AirVia Ltd",saleType + " Sales Report");
                    PdfPTable queryTable = new PdfPTable(6);
                    String[] columnNames = {"Employee_ID", "Blank Number", "Payment Date", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};

                    float[] columnWidths = {2f, 2f, 1f,1.5f, 1.8f, 1f};
                    queryTable.setWidths(columnWidths);
                    queryTable.setWidthPercentage(100);

                    for (String columnName : columnNames) {
                        PdfPCell header = new PdfPCell(new Phrase(columnName));
                        header.setMinimumHeight(20);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        queryTable.addCell(header);
                    }

                    PdfPCell table_cell;

                    while (rs.next()) {

                        String Employee_ID = rs.getString("Employee_ID");
                        table_cell = new PdfPCell(new Phrase(Employee_ID));
                        queryTable.addCell(table_cell);

                        String BlankNumber = rs.getString("BlankNumber");
                        table_cell = new PdfPCell(new Phrase(BlankNumber));
                        queryTable.addCell(table_cell);

                        String Payment_Date = rs.getString("Payment_Date");
                        table_cell = new PdfPCell(new Phrase(Payment_Date));
                        queryTable.addCell(table_cell);

                        String TotalSaleAmount = rs.getString("TotalSaleAmount");
                        table_cell = new PdfPCell(new Phrase(TotalSaleAmount));
                        queryTable.addCell(table_cell);

                        String TotalCommissionAmount = rs.getString("TotalCommissionAmount");
                        table_cell = new PdfPCell(new Phrase(TotalCommissionAmount));
                        queryTable.addCell(table_cell);

                        String NetSaleAmount = rs.getString("NetSaleAmount");
                        table_cell = new PdfPCell(new Phrase(NetSaleAmount));
                        queryTable.addCell(table_cell);


                    }


                    PdfPTable additionalQueryTable = new PdfPTable(2);

                    String[] columnName2 = {"Total Net Sale Amount", "Total Earned Commission"};

                    float[] columnWidths2 = {0.5f, 0.5f};
                    additionalQueryTable.setWidths(columnWidths2);
                    additionalQueryTable.setWidthPercentage(50);

                    for (String columnName : columnName2) {
                        PdfPCell header = new PdfPCell(new Phrase(columnName));
                        header.setMinimumHeight(20);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        additionalQueryTable.addCell(header);
                    }

                    PdfPCell table2_cell;
                    if (rsAdditional.next()) {
                        String TotalNetSaleAmount = rsAdditional.getString("TotalNetSaleAmount");
                        table2_cell = new PdfPCell(new Phrase(TotalNetSaleAmount));
                        additionalQueryTable.addCell(table2_cell);

                        String TotalOverallCommissionEarned = rsAdditional.getString("TotalOverallCommissionEarned");
                        table2_cell = new PdfPCell(new Phrase(TotalOverallCommissionEarned));
                        additionalQueryTable.addCell(table2_cell);


                    }


                    rsAdditional.close();
                    PDFdoc.add(queryTable);
                    PDFdoc.add(additionalQueryTable);
                    PDFdoc.close();

                    preparedStatement.close();
                    preparedStatementA.close();


                } catch (SQLException | DocumentException | IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }

        });


        viewProducedReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File PDFdoc = new File("/Users/alexelemele/Downloads/AirViaTicketSalesSystem/data/RefundEmail.pdf");
                    Desktop.getDesktop().open(PDFdoc);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }



    public static void main(String[] args) {
        IndividualReport individualReport = new IndividualReport(ID,username);
        individualReport.show();
    }
}



