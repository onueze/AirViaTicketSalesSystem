package Manager;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class OfficeManagerAdvisorIndividualReport extends javax.swing.JFrame{
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
    private static int ID;
    private static String username;




    public OfficeManagerAdvisorIndividualReport(int ID, String username)  {
        this.username= username;
        this.ID= ID;
        setContentPane(individualReportPage);
        setSize(1000,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        String FileName = "Advisor Individual Report";





        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerHome officeManagerPage = new OfficeManagerHome(ID,username);
                officeManagerPage.setVisible(true);
                dispose();

            }
        });
        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerStock officeManagerStock = new OfficeManagerStock(ID,username);
                officeManagerStock.setVisible(true);
                dispose();

            }
        });
        blanksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerBlanks officeManagerBlanks = new OfficeManagerBlanks(ID,username);
                officeManagerBlanks.setVisible(true);
                dispose();

            }
        });
        discountPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerDiscountPlan discountPlanButton = new OfficeManagerDiscountPlan(ID,username);
                discountPlanButton.setVisible(true);
                dispose();

            }
        });
        ticketStockTurnOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                OfficeManagerTicketStockTurnOverReport ticketStockTurnoverReportButton = new OfficeManagerTicketStockTurnOverReport(ID,username);
                ticketStockTurnoverReportButton.setVisible(true);
                dispose();

            }
        });
        interlineSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerInterlineSalesReports interlineSalesReportButton = new OfficeManagerInterlineSalesReports(ID,username);
                interlineSalesReportButton.setVisible(true);
                dispose();


            }
        });
        domesticSalesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerDomesticSalesReport domesticSalesReportButton = new OfficeManagerDomesticSalesReport(ID,username);
                domesticSalesReportButton.setVisible(true);
                dispose();

            }
        });
        advisorIndividualReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManagerAdvisorIndividualReport advisorIndividualReportButton = new OfficeManagerAdvisorIndividualReport(ID,username);
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
                String advisorID = (String) chooseAdvisorName.getSelectedItem();
                String saleType = (String) selectSaleType.getSelectedItem();

                try (Connection con = DBConnectivity.getConnection()) {
                    assert con != null;
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    String query = "SELECT " +
                            "Sale.Employee_ID, " +
                            "Blank.BlankNumber, " +
                            "ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate), 2) AS TotalSaleAmount, " +
                            "ROUND(SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS TotalCommissionAmount, " +
                            "ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS NetSaleAmount " +
                            "FROM " +
                            "Sale " +
                            "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "JOIN Currency_Code ON Sale.Currency_Code = Currency_Code.Currency_Code " +
                            "WHERE Sale.Employee_ID = ? AND Blank.Type = ? " +
                            "GROUP BY " +
                            "Sale.Employee_ID, Blank.BlankNumber;";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, advisorID);
                    preparedStatement.setString(2, saleType);

                    String additionalQuery = "SELECT ROUND(SUM(NetSaleAmount), 2) AS TotalNetSaleAmount, ROUND(SUM(TotalCommissionAmount), 2) AS TotalOverallCommissionEarned FROM (" +
                            "SELECT Sale.Employee_ID, " +
                            "       ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate), 2) AS TotalSaleAmount, " +
                            "       ROUND(SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS TotalCommissionAmount, " +
                            "       ROUND(SUM(Sale.Amount * Currency_Code.Exchange_Rate) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate * Currency_Code.Exchange_Rate ELSE 0 END), 2) AS NetSaleAmount " +
                            "FROM Sale " +
                            "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "JOIN Currency_Code ON Sale.Currency_Code = Currency_Code.Currency_Code " +
                            "WHERE Sale.Employee_ID = ? AND Blank.Type = ? " +
                            "GROUP BY Sale.Employee_ID" +
                            ") AS subquery;";

                    PreparedStatement preparedStatementA = con.prepareStatement(additionalQuery);
                    preparedStatementA.setString(1, advisorID);
                    preparedStatementA.setString(2, saleType);

                    ResultSet rs = preparedStatement.executeQuery();
                    ResultSet rsAdditional = preparedStatementA.executeQuery();

                    Path pdfPath = Paths.get("Individual report.pdf");
                    if (Files.exists(pdfPath)) {
                        Files.delete(pdfPath);
                    }


                    Document PDFdoc = new Document(PageSize.A4.rotate());
                    PdfWriter.getInstance(PDFdoc,new FileOutputStream("Individual report.pdf"));
                    PDFdoc.open();
                    PdfPTable queryTable = new PdfPTable(5);

                    String[] columnNames = {"Employee_ID", "Blank Number", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount"};

                    float[] columnWidths = {2f, 2f, 1.5f, 1.8f, 1f};
                    queryTable.setWidths(columnWidths);
                    queryTable.setWidthPercentage(100);

                    for (String columnName : columnNames) {
                        PdfPCell header = new PdfPCell(new Phrase(columnName));
                        header.setMinimumHeight(20);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        queryTable.addCell(header);
                    }

                    PdfPCell table_cell;

                    while(rs.next()){

                        String Employee_ID = rs.getString("Employee_ID");
                        table_cell=new PdfPCell(new Phrase(Employee_ID));
                        queryTable.addCell(table_cell);

                        String BlankNumber = rs.getString("BlankNumber");
                        table_cell = new PdfPCell(new Phrase(BlankNumber));
                        queryTable.addCell(table_cell);

                        String TotalSaleAmount = rs.getString("TotalSaleAmount");
                        table_cell=new PdfPCell(new Phrase(TotalSaleAmount));
                        queryTable.addCell(table_cell);

                        String TotalCommissionAmount = rs.getString("TotalCommissionAmount");
                        table_cell=new PdfPCell(new Phrase(TotalCommissionAmount));
                        queryTable.addCell(table_cell);

                        String NetSaleAmount = rs.getString("NetSaleAmount");
                        table_cell=new PdfPCell(new Phrase(NetSaleAmount));
                        queryTable.addCell(table_cell);



                    }






                    PdfPTable additionalQueryTable = new PdfPTable(2);

                    String[] columnName2 = {"Total Net Sale Amount","Total Earned Commission"};

                    float[] columnWidths2 = {1f,1f};
                    additionalQueryTable.setWidths(columnWidths2);
                    additionalQueryTable.setWidthPercentage(100);

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



                } catch (SQLException exception) {
                    exception.printStackTrace();
                } catch (DocumentException exception) {
                    exception.printStackTrace();
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }

        });


        viewProducedReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File PDFdoc = new File("Individual report.pdf");
                    Desktop.getDesktop().open(PDFdoc);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        OfficeManagerAdvisorIndividualReport individualReport = new OfficeManagerAdvisorIndividualReport(ID,username);
        individualReport.show();
    }
}



