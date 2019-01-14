package bolao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author thayna
 */
public class ConnectionFactory {
    /**
     * Connect to a sample database
     * @return
     */
    public static Connection connect() {
        String url = "jdbc:sqlite:gameManager.sqlite";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
