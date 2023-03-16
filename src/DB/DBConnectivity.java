package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectivity {

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://smcse-stuproj00.city.ac.uk:3306" +
                    "/in2018g01");
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("Connection success");

            return con;

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return con;
    }
}
