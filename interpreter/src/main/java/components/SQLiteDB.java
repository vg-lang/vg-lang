package components;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDB {
    private Connection connection;


    public SQLiteDB(String dbPath) throws SQLException {
        String url = "jdbc:sqlite:" + dbPath;
        this.connection = DriverManager.getConnection(url);
    }

    public int executeUpdate(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }
    public String executeQuery(String sql) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    sb.append(rs.getString(i));
                    if (i < columnCount) {
                        sb.append(", ");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


}
