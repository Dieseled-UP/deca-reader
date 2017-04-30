package connect;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by denis on 30/04/17
 */
class DBConn {

    // declare variables
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://52.16.53.121/database";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static Connection con = null;
    private static PreparedStatement statement = null;

    /**
     * Method to make a connection to the database
     *
     * @return Connection con
     */
    private static Connection getConnection() {

        System.setProperty(DB_DRIVER, "");

        try {

            // Open connection
            con = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

            // Tell user if successful
            System.out.println("Connection successful...");

        } catch (Exception e) {

            // Tell user if unsuccessful
            System.out.println("Database Connection Unsuccessful... Fail");
        }

        return con;
    }

    /**
     * Method to take a string and make it a SQL PreparedStatement
     *
     * @param statementIn String query
     * @return PreparedStatement statement
     */
    static PreparedStatement pStatement(String statementIn) {

        try {

            statement = (PreparedStatement) getConnection().prepareStatement(statementIn);
            return statement;

        } catch (SQLException e) {

            System.out.println("Please check SQL syntax");
            return statement;
        }
    }

    /**
     * Method to close the connection
     *
     * @throws SQLException if connection cannot close
     */
    static void finish() throws SQLException {

        con.close();
    }

}
