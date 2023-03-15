import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectivity {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://\n" +
                    "smcse-stuproj00.city.ac.uk:3306\n" +
                    "/in2018g01");

            return con;

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        finally {
            return con;
        }
    }
}
