package Manager;

import DB.DBConnectivity;
import com.itextpdf.io.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
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
    private JButton testPdfButton;
    private static int ID;
    private static String username;


    private static void createPDF(String pdfPath, ResultSet resultSet, String advisorName) throws FileNotFoundException, SQLException {
        // Initialize the PdfWriter
        PdfWriter pdfWriter = new PdfWriter(pdfPath);

        // Initialize the PdfDocument
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        // Initialize the Document
        Document document = new Document(pdfDocument);

        // Add title to the document
        String title = advisorName + " Individual Report";
        Paragraph titleParagraph = new Paragraph(title).setFontSize(18).setBold().setUnderline();
        document.add(titleParagraph);

        // Create a table and populate it with the fetched data
        Table table = new Table(5);
        table.addHeaderCell("Employee ID");
        table.addHeaderCell("Blank Number");
        table.addHeaderCell("Is Sold");
        table.addHeaderCell("Type");
        table.addHeaderCell("Date Assign");

        // Iterate through the result set without calling resultSet.first()
        while (resultSet.next()) {
            table.addCell(resultSet.getString("Employee_ID"));
            table.addCell(resultSet.getString("BlankNumber"));
            table.addCell(resultSet.getString("IsSold"));
            table.addCell(resultSet.getString("Type"));
            table.addCell(resultSet.getString("Date_Assign"));
        }

        // Add the table to the document
        document.add(table);

        // Close the document
        document.close();
    }











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
                    String query = "SELECT Employee.Employee_ID, Employee.First_name, Blank.BlankNumber, Blank.isSold, Blank.Type, Blank.date_assign "
                            + "FROM Employee "
                            + "INNER JOIN Blank ON Employee.Employee_ID = Blank.isAssigned "
                            + "WHERE Employee.role = 'advisor' "
                            + "AND Employee.Employee_ID = ? "
                            + "AND Blank.isAssigned = true "
                            + "AND Blank.Type = ?";

                    PreparedStatement preparedStatement = con.prepareStatement(query);

                    // Set the values for the prepared statement
                    preparedStatement.setString(1, advisorID);
                    preparedStatement.setString(2, saleType);

                    // Execute the query and fetch the data
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Generate the PDF report
                    String advisorName = "";
                    if (resultSet.next()) {
                        advisorName = resultSet.getString("First_name");
                    }

                    String pdfPath = advisorName.replaceAll("\\s+", "") + "_IndividualReport.pdf";
                    createPDF(pdfPath, resultSet, advisorName);

                    // Display the generated PDF on the screen
                    if (Desktop.isDesktopSupported()) {
                        try {
                            File pdfFile = new File(pdfPath);
                            Desktop.getDesktop().open(pdfFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (java.io.IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch (ClassNotFoundException | SQLException | FileNotFoundException ex) {
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



