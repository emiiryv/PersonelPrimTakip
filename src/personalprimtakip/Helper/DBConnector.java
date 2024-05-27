package personalprimtakip.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {
    private static Connection connect;


    public Connection connectDB(){
        try {
            this.connect = DriverManager.getConnection(Config.DB_URL,Config.DB_USERNAME,Config.DB_PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return this.connect;
    }

    public static Connection getInstance(){
        DBConnector db = new DBConnector();
        return db.connectDB();
    }
}
