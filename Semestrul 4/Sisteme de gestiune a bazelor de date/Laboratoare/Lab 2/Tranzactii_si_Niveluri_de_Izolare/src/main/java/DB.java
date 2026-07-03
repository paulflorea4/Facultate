import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DB {

    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
                "jdbc:sqlserver://FLOREA\\SQLEXPRESS:1433;" +
                        "databaseName=SGBD_Lab2;" +
                        "encrypt=true;trustServerCertificate=true;"
        );

        config.setUsername("sgbd");
        config.setPassword("flo");

        config.setMaximumPoolSize(2);
        config.setMinimumIdle(2);

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}