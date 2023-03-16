import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectivity {

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://\n" +
                    "smcse-stuproj00.city.ac.uk:3306\n" +
                    "/in2018g01");
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            return con;

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return null;
    }
}
