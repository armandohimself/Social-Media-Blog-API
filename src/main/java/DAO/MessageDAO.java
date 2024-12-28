package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message insertMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Remember to include Statement.RETURN_GENERATED_KEYS
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet pkeyResultSet = ps.getGeneratedKeys();

                if(pkeyResultSet.next()) {
                    int generated_message_id = (int) pkeyResultSet.getLong(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return null;
    }
}