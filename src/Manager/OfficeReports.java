package Manager;

import DB.DBConnectivity;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OfficeReports extends javax.swing.JFrame {
    private JButton logOutButton;
    private JButton homeButton;
    private JButton stocksButton;
    private JButton ticketStockTurnOverButton;
    private JButton blanksButton;
    private JButton discountPlanButton;
    private JButton advisorIndividualReportButton;
    private JPanel individualReportPage;
    private JButton produceAdvisorIndivudialReportButton;
    private JComboBox chooseAdvisorName;
    private JComboBox selectSaleType;
    private JButton viewProducedReportButton;
    private JButton notificationButton;
    private JTextField dateForReport;
    private JButton viewDateButton;
    private JTextField lowerDateRange;
    private JTextField upperDateRange;
    private JComboBox selectIndividualGlobal;
    private PdfPTable lastGeneratedPdfFile;




    private static int ID;
    private static String username;
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


/*
    private boolean validateInput() {
        String lowerInput = lowerDateRange.getText();
        String upperInput = upperDateRange.getText();

        try {
            long lowerDate = Long.parseLong(lowerInput.substring(6));
            long upperDate = Long.parseLong(upperInput.substring(6));

            if (lowerDate <= 0 || upperDate <= 0 || lowerDate > 999999 || upperDate > 99999999) {
                JOptionPane.showMessageDialog(null, "Invalid date range provided");
                return false;
            }

            long lowerRange = Long.parseLong(lowerInput);
            long upperRange = Long.parseLong(upperInput);

            if (lowerRange > upperRange) {
                JOptionPane.showMessageDialog(null, "Lower date range must be less than or equal to upper date range");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid date range provided");
            return false;
        }

        JOptionPane.showMessageDialog(null, "Input is valid");
        return true;
    }

 */



    private List<Integer> refundMessage() {
        List<Integer> refundIDs = new ArrayList<>();

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement st = con.createStatement();
            String query = "SELECT Refund_ID FROM Refund WHERE refund_status = false";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                int refundID = rs.getInt("Refund_ID");
                refundIDs.add(refundID);
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return refundIDs;
    }


    private JPanel createRefundApprovalPanel(int refundID) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Refund ID: " + refundID + " needs approval");
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");

        // Add action listeners for approve and reject buttons
        approveButton.addActionListener(e -> {
            // Approve refund logic
        });
        rejectButton.addActionListener(e -> {
            // Reject refund logic
        });

        buttonsPanel.add(approveButton);
        buttonsPanel.add(rejectButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }


    private PdfPTable createIndividualInterlineTable(String advisorID, int lowerDateRange, int upperDateRange) throws SQLException, FileNotFoundException, DocumentException, ClassNotFoundException {
        //blank Number,commsiaion rate applied the percentage,form of payemtn that was used, and if it was chas the sale price shoudl be placed next to it

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "SELECT Sale.BlankNumber, " +
                    "Sale.Amount AS TotalSaleAmount, " +
                    "CASE WHEN Commission.blankType = Blank.Type " +
                    "THEN Sale.Amount * Commission.Rate " +
                    "ELSE 0 " +
                    "END AS TotalCommissionAmount, " +
                    "Sale.Amount - " +
                    "CASE WHEN Commission.blankType = Blank.Type " +
                    "THEN Sale.Amount * Commission.Rate " +
                    "ELSE 0 " +
                    "END AS NetSaleAmount " +
                    "FROM Sale " +
                    "JOIN Commission " +
                    "ON Sale.Commission_ID = Commission.Commission_ID " +
                    "JOIN Blank " +
                    "ON Sale.BlankNumber = Blank.BlankNumber " +
                    "WHERE Sale.Employee_ID = ? AND DATE(Sale.Payment_Date) BETWEEN ? AND ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, advisorID);
            pstmt.setInt(2,lowerDateRange);
            pstmt.setInt(3,upperDateRange);


            ResultSet rs = pstmt.executeQuery();


            PdfPTable table = new PdfPTable(4);

            String[] headers = {"Blank Number", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            BaseColor headerBackgroundColor = new BaseColor(204, 204, 204);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBackgroundColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            while (rs.next()) {
                for (int i = 1; i <= 4; i++) {
                    table.addCell(rs.getString(i));
                }


                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream("individual_interline_report.pdf"));
                document.open();
                document.add(new Paragraph("Individual Interline Report"));
                document.add(table);
                document.close();
                //lastGeneratedPdfFile = "individual_interline_report.pdf";
                lastGeneratedPdfFile = new PdfPTable(table);



            }
            return table;
        }

    }



    private PdfPTable createIndividualDomesticTable(String advisorID, int lowerDateRange, int upperDateRange) throws SQLException, FileNotFoundException, DocumentException, ClassNotFoundException {
        //ensure

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "SELECT Sale.Employee_ID,Sale.BlankNumber, Sale.Amount AS TotalSaleAmount, " +
                    "CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END AS TotalCommissionAmount, " +
                    "Sale.Amount - CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END AS NetSaleAmount " +
                    "FROM Sale " +
                    "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                    "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                    "WHERE Sale.Employee_ID = ? AND Blank.Type = 'Domestic' AND DATE(Sale.Payment_Date) BETWEEN ? AND ? ";


            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, advisorID);
            pstmt.setInt(2,lowerDateRange);
            pstmt.setInt(3,upperDateRange);
            ResultSet rs = pstmt.executeQuery();


            PdfPTable table = new PdfPTable(5);


            String[] headers = {"Employee ID ","Blank Number", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            BaseColor headerBackgroundColor = new BaseColor(204, 204, 204);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBackgroundColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }



            while (rs.next()) {
                for (int i = 1; i <= 5; i++) {
                    table.addCell(rs.getString(i));
                }
            }

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("individual_domestic_report.pdf"));
            document.open();
            document.add(new Paragraph("Individual Domestic Report"));
            document.add(table);
            document.close();
            //lastGeneratedPdfFile = "individual_domestic_report.pdf";
            lastGeneratedPdfFile = new PdfPTable(table);

            return table;
        }
    }

    private PdfPTable createGlobalInterlineTable(int lowerDateRange, int upperDateRange) throws SQLException, FileNotFoundException, DocumentException, ClassNotFoundException {

        //and cilue the tyoe of [ayent, and the ammoutn next to it


        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "SELECT Sale.Employee_ID, SUM(Sale.Amount) AS TotalSaleAmount, " +
                    "SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS TotalCommissionAmount, " +
                    "SUM(Sale.Amount) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS NetSaleAmount " +
                    "FROM Sale " +
                    "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                    "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                    "WHERE DATE(Sale.Payment_Date) BETWEEN ? AND ? "+
                    "GROUP BY Sale.Employee_ID";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,lowerDateRange);
            pstmt.setInt(2,upperDateRange);

            ResultSet rs = pstmt.executeQuery();

            PdfPTable table = new PdfPTable(4);

            String[] headers = {"Employee ID", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            BaseColor headerBackgroundColor = new BaseColor(204, 204, 204);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBackgroundColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            while (rs.next()) {
                for (int i = 1; i <= 4; i++) {
                    table.addCell(rs.getString(i));
                }
            }

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("global_interline_report.pdf"));
            document.open();
            document.add(new Paragraph("Global Interline Report"));
            document.add(table);
            document.close();
            //lastGeneratedPdfFile = "global_interline_report.pdf";
            lastGeneratedPdfFile = new PdfPTable(table);


            return table;
        }
    }



    private PdfPTable createGlobalDomesticTable(int lowerDateRange, int upperDateRange) throws SQLException, FileNotFoundException, DocumentException, ClassNotFoundException {

        try (Connection con = DBConnectivity.getConnection()) {
            assert con != null;
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "SELECT Sale.Employee_ID, " +
                    "SUM(Sale.Amount) AS TotalSaleAmount, " +
                    "SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS TotalCommissionAmount, " +
                    "SUM(Sale.Amount) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS NetSaleAmount " +
                    "FROM Sale " +
                    "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                    "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                    "WHERE Blank.Type = 'Domestic' AND DATE(Sale.Payment_Date) BETWEEN ? AND ? " +
                    "GROUP BY Sale.Employee_ID" ;

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,lowerDateRange);
            pstmt.setInt(2,upperDateRange);

            PdfPTable table = new PdfPTable(4);

            String[] headers = {"Employee ID", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            BaseColor headerBackgroundColor = new BaseColor(204, 204, 204);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBackgroundColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                for (int i = 1; i <= 4; i++) {
                    table.addCell(rs.getString(i));
                }
            }

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("global_domestic_report.pdf"));
            document.open();
            document.add(new Paragraph("Global Domestic Report"));
            document.add(table);
            document.close();
            //lastGeneratedPdfFile = "global_domestic_report.pdf";
            lastGeneratedPdfFile = new PdfPTable(table);


            return table;
        }
    }




    private void showPopup() {
        JComponent glassPane = new JPanel();
        glassPane.setLayout(null);
        glassPane.setOpaque(false);

        JPanel popup = new JPanel();
        popup.setBounds(getWidth() - 200, 0, 200, 400);
        popup.setBackground(Color.LIGHT_GRAY);
        popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));

        List<Integer> refundIDs = refundMessage();
        for (int refundID : refundIDs) {
            popup.add(createRefundApprovalPanel(refundID));
        }

        JScrollPane scrollPane = new JScrollPane(popup);
        scrollPane.setBounds(getWidth() - 200, 0, 200, 400);
        glassPane.add(scrollPane);

        JButton closePopupButton = new JButton("Close");
        closePopupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                glassPane.setVisible(false);
            }
        });
        closePopupButton.setBounds(getWidth() - 200, 400, 200, 30);
        glassPane.add(closePopupButton);

        setGlassPane(glassPane);
        glassPane.setVisible(true);
    }



    public OfficeReports(int ID, String username) {


        this.username = username;
        this.ID = ID;

        setContentPane(individualReportPage);
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        String FileName = "Advisor Individual Report";



        //startRefundNotificationWatcher();



        notificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPopup();
            }
        });


        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerHome officeManagerPage = new OfficeManagerHome(ID, username);
                officeManagerPage.setVisible(true);
                dispose();

            }
        });
        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerStock officeManagerStock = new OfficeManagerStock(ID, username);
                officeManagerStock.setVisible(true);
                dispose();

            }
        });
        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID, username);
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });
        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan(ID, username);
                discountPlanButton.setVisible(true);
                dispose();

            }
        });
        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerTicketStockTurnOverReport ticketStockTurnoverReportButton = new OfficeManagerTicketStockTurnOverReport(ID, username);
                ticketStockTurnoverReportButton.setVisible(true);
                dispose();

            }
        });


        advisorIndividualReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeReports advisorIndividualReportButton = new OfficeReports(ID, username);
                advisorIndividualReportButton.setVisible(true);
                dispose();

            }
        });
        chooseAdvisorName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "SELECT  Employee.Employee_ID FROM Employee where Employee.role = 'advisor'";

                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        String Id = rs.getString("Employee_ID");
                        chooseAdvisorName.addItem(Id);
                    }
                    st.close();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

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

                //validateInput();

                String lowerRangeText = lowerDateRange.getText();
                String upperRangeText = upperDateRange.getText();

                int lowerBound = Integer.parseInt(lowerRangeText);
                int upperBound = Integer.parseInt(upperRangeText);

                String saleType = (String) selectSaleType.getSelectedItem();
                String IndividualGlobal = (String) selectIndividualGlobal.getSelectedItem();
                String advisorName = (String) chooseAdvisorName.getSelectedItem();

                try {
                    if (saleType.equals("Interline")) {
                        if (IndividualGlobal.equals("Global")) {
                             lastGeneratedPdfFile = new PdfPTable(createGlobalInterlineTable(lowerBound, upperBound));
                        } else {
                             lastGeneratedPdfFile = new PdfPTable(createIndividualInterlineTable(advisorName, lowerBound, upperBound));
                        }
                    } else if (saleType.equals("Domestic")) {
                        if (IndividualGlobal.equals("Global")) {
                             lastGeneratedPdfFile = new PdfPTable(createGlobalDomesticTable(lowerBound, upperBound));
                        } else {
                             lastGeneratedPdfFile = new PdfPTable(createIndividualDomesticTable(advisorName, lowerBound, upperBound));
                        }
                    }
                } catch (SQLException | FileNotFoundException | DocumentException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }

        });


        viewProducedReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PdfPTable lastGeneratedPdfTable= new PdfPTable(lastGeneratedPdfFile);


                if (lastGeneratedPdfFile != null) {
                    try {
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
                        document.open();
                        document.add(lastGeneratedPdfTable);
                        document.close();

                        if (Desktop.isDesktopSupported()) {
                            File pdfFile = new File("report.pdf");
                            Desktop.getDesktop().open(pdfFile);
                        } else {
                            System.out.println("Desktop is not supported.");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error opening PDF file: " + ex.getMessage());
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please generate a report first by clicking the 'Submit' button.", "No report generated", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        selectIndividualGlobal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectIndividualGlobal.addItem("Individual");
                selectIndividualGlobal.addItem("Global");
            }
        });

    }



    public static void main(String[] args) {
        OfficeReports individualReport = new OfficeReports(ID,username);
        individualReport.show();


    }
}



