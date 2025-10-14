package hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Simple singleton DB connection manager.
 * Reads JDBC settings from /hospital/config/db.properties on the classpath.
 */
public class DBConnection {

    private static Connection connection = null;

    private DBConnection() { /* prevent instantiation */ }

    /**
     * Returns a shared Connection instance. Creates it if not already created.
     * Caller should NOT close the returned Connection (use closeConnection()).
     */
    public static synchronized Connection getConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException ignored) {}
        }

        Properties prop = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("hospital/config/db.properties")) {

            if (input == null) {
                System.err.println("DBConnection: cannot find hospital/config/db.properties on classpath");
                return null;
            }

            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            // Optional: explicitly load MySQL driver (modern JDBC usually loads automatically)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            }

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");

        } catch (IOException e) {
            System.err.println("Error reading db.properties: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Unable to establish DB connection: " + e.getMessage());
        }

        return connection;
    }

    /**
     * Close the shared connection.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing DB connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
