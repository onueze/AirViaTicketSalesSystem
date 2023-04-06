package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemAdminHome extends javax.swing.JFrame {
    private JButton homeButton;
    private JButton userDetailsButton;
    private JButton customerDetailsButton;
    private JButton commissionRatesButton;
    private JButton systemStockButton;
    private JButton restoreButton;
    private JButton backUpButton;
    private JButton createUserButton;
    private JButton logOutButton;
    private JPanel adminPage;
    private static int ID;
    private static String username;
    String location = null;
    String filename;

    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private File backupFile;

    public SystemAdminHome(int ID, String username){
        this.ID = ID;
        this.username = username;
        setContentPane(adminPage);
        setSize(450,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        dbHost = "smcse-stuproj00.city.ac.uk";
        dbPort = "3306";
        dbUser = "in2018g01_a";
        dbPassword = "G3pm6gib";
        dbName = "in2018g01";


        userDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        customerDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        commissionRatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        systemStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Restoring the database will overwrite any existing data. Are you sure you want to continue?", "Confirm Database Restore", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result2 = fileChooser.showOpenDialog(null);
                    if (result2 == JFileChooser.APPROVE_OPTION) {
                        File backupFile = fileChooser.getSelectedFile();
                        try {
                            String[] restoreCmd = new String[]{"mysql", "--user=" + dbUser, "--password=" + dbPassword, dbName, "-e", "source " + backupFile.getAbsolutePath()};
                            Process runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
                            int processComplete = runtimeProcess.waitFor();
                            if (processComplete == 0) {
                                JOptionPane.showMessageDialog(null, "Database restored from " + backupFile.getAbsolutePath());
                            } else {
                                JOptionPane.showMessageDialog(null, "Error restoring database.");
                            }
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error restoring database: " + ex.getMessage());
                        }
                    }
                }


            }
        });
        backUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file and directory
                    backupFile = fileChooser.getSelectedFile();
                    File backupDir = backupFile.getParentFile();

                    // Get today's date in the format yyyy-MM-dd
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = dateFormat.format(new Date());

                    // Construct the backup file name with today's date
                    String backupFileName = "backup" + "_"+ dbName + "_" + dateStr + ".sql";
                    backupFile = new File(backupDir, backupFileName);

                    try {
                        // Build the command to execute mysqldump
                        ProcessBuilder processBuilder = new ProcessBuilder(
                                "/usr/local/mysql-8.0.32-macos13-arm64/bin/mysqldump",
                                "-h" + dbHost,
                                "-P" + dbPort,
                                "-u" + dbUser,
                                "-p" + dbPassword,
                                dbName,
                                "--result-file=" + backupFile.getAbsolutePath()
                        );

                        // Execute the command
                        processBuilder.start();

                        JOptionPane.showMessageDialog(null, "Database backed up to " + backupFile.getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error backing up database: " + ex.getMessage());
                    }
                }
            }

        });
    }




    public static void main(String[] args){
        SystemAdminHome adminHome = new  SystemAdminHome(ID, username);
        adminHome.show();
    }
}
