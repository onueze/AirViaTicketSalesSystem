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
                    Statement st = con.createStatement();

                    String query = "SELECT " +
                            "Sale.Employee_ID " +
                            "SUM(Sale.Amount) AS TotalSaleAmount, " +
                            "SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS TotalCommissionAmount, " +
                            "SUM(Sale.Amount) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS NetSaleAmount " +
                            "FROM " +
                            "Sale " +
                            "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "WHERE Sale.Employee_ID = ? " +
                            "GROUP BY " +
                            "Sale.Employee_ID;";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1,advisorID);



                    Statement ast = con.createStatement();

                    String additionalQuery = "SELECT SUM(NetSaleAmount) AS TotalNetSaleAmount FROM (" +
                            "SELECT Sale.Employee_ID " +
                            "       SUM(Sale.Amount) AS TotalSaleAmount, " +
                            "       SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS TotalCommissionAmount, " +
                            "       SUM(Sale.Amount) - SUM(CASE WHEN Commission.blankType = Blank.Type THEN Sale.Amount * Commission.Rate ELSE 0 END) AS NetSaleAmount " +
                            "FROM Sale " +
                            "JOIN Commission ON Sale.Commission_ID = Commission.Commission_ID " +
                            "JOIN Blank ON Sale.BlankNumber = Blank.BlankNumber " +
                            "WHERE Sale.Employee_ID = ? " +
                            "GROUP BY Sale.Employee_ID" +
                            ") AS subquery;";

                    PreparedStatement preparedStatementA = con.prepareStatement(additionalQuery);
                    preparedStatementA.setString(1,advisorID);





                    ResultSet rs = st.executeQuery(query);
                    ResultSet rsAdditional = ast.executeQuery(additionalQuery);

                    Document PDFdoc = new Document(PageSize.A4.rotate());
                    PdfWriter.getInstance(PDFdoc,new FileOutputStream("Individual report.pdf"));
                    PDFdoc.open();
                    PdfPTable queryTable = new PdfPTable(5);



                    String[] columnNames = {"Employee_ID", "Total Sale Amount", "Total Commission Amount", "Net Sale Amount","Total Net Sale Amount"};

                    float[] columnWidths = {2f, 1.5f, 1.8f, 1f,1.5f};
                    queryTable.setWidths(columnWidths);
                    queryTable.setWidthPercentage(100);

                    for (String columnName : columnNames) {
                        PdfPCell header = new PdfPCell(new Phrase(columnName));
                        header.setMinimumHeight(20); // Set minimum height for header cells
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY); // Set background color for header cells
                        queryTable.addCell(header);
                    }

                    PdfPCell table_cell;

                    while(rs.next()){

                        String Employee_ID = rs.getString("Employee_ID");
                        table_cell=new PdfPCell(new Phrase(Employee_ID));
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

                    if (rsAdditional.next()) {
                        String TotalNetSaleAmount = rsAdditional.getString("TotalNetSaleAmount");
                        table_cell = new PdfPCell(new Phrase(TotalNetSaleAmount));
                        queryTable.addCell(table_cell);
                    }
                    rsAdditional.close();
                    PDFdoc.add(queryTable);
                    PDFdoc.close();

                    st.close();







                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
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



