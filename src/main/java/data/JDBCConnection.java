package data;

import util.Config;
import util.Crypto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    
    private JDBCConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.get("db.url"),
                Crypto.getInstance().decrypt( Config.get("db.usr") ),
                Crypto.getInstance().decrypt( Config.get("db.pwd") )
        );
    }

}
