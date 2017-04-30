package connect;

import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;

/**
 * Created by denis on 30/04/17
 */
public class Query {

    private static String sql = null;
    private static ResultSet result;
    private static PreparedStatement statement;

    public static boolean postReads(String readOne, String readTwo, String readThree) {

        try {

            sql = "INSERT INTO dieseled_wp.deca_read (anchor_one, anchor_two, anchor_three) VALUES (?, ?, ?)";
            statement = DBConn.pStatement(sql);

            statement.setString(1, readOne);
            statement.setString(2, readTwo);
            statement.setString(3, readThree);

            int done = statement.executeUpdate();

            if (done == 1) {

                return true;
            }

            // Close the connection
            DBConn.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
