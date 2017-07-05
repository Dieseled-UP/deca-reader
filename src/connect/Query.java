package connect;

import com.mysql.jdbc.PreparedStatement;

/**
 * Created by denis on 30/04/17
 */
public class Query {

    public static void postReads(double readOne, double readTwo, double readThree) {

        try {

            String sql = "INSERT INTO ratchet.deca_read (anchor_zero, anchor_one, anchor_two) VALUES (?, ?, ?)";
            PreparedStatement statement = DBConn.pStatement(sql);

            statement.setDouble(1, readOne);
            statement.setDouble(2, readTwo);
            statement.setDouble(3, readThree);

            statement.executeUpdate();

            // Close the connection
            DBConn.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
