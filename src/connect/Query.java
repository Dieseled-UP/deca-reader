package connect;

import com.mysql.jdbc.PreparedStatement;

/**
 * Created by denis on 30/04/17
 */
public class Query {

    public static boolean postReads(double readOne, double readTwo, double readThree) {

        try {

            String sql = "INSERT INTO masters.deca_night (anchor_one, anchor_two, anchor_three) VALUES (?, ?, ?)";
            PreparedStatement statement = DBConn.pStatement(sql);

            statement.setDouble(1, readOne);
            statement.setDouble(2, readTwo);
            statement.setDouble(3, readThree);

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
