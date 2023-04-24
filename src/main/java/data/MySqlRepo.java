package data;

import domain.EncodedState;
import domain.Message;
import util.SpyMasterException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static data.JDBCConnection.getConnection;

public class MySqlRepo implements SpyMasterRepo {

    private static final Logger LOGGER = Logger.getLogger(MySqlRepo.class.getName());

    private static final String SELECT_MESSAGES_FROM_SENDER = "SELECT * FROM messages WHERE sender = ?;";
    private static final String SELECT_MESSAGES_FROM_RECEIVER = "SELECT * FROM messages WHERE receiver = ?;";
    private static final String UPDATE_MESSAGE = "UPDATE messages SET sender_encoded = ?, receiver_encoded = ?, content = ? WHERE sender = ? AND receiver = ?;";
    private static final String DELETE_MESSAGE = "DELETE FROM messages WHERE sender = ? AND receiver = ?;";

    private Message rs2Message(ResultSet rs) throws SQLException {
        String sender = rs.getString("sender");
        String receiver = rs.getString("receiver");
        String content = rs.getString("content");

        boolean senderEncoded = rs.getBoolean("sender_encoded");
        boolean receiverEncoded = rs.getBoolean("receiver_encoded");

        EncodedState encodedState;
        if (senderEncoded && receiverEncoded) encodedState = EncodedState.FULL_ENCODED;
        else if (senderEncoded && !receiverEncoded) encodedState = EncodedState.SENDER_ENCODED;
        else if (!senderEncoded && receiverEncoded) encodedState = EncodedState.RECEIVER_ENCODED;
        else encodedState = EncodedState.PLAIN_TEXT;

        return new Message(sender, receiver, encodedState, content);
    }

    @Override
    public List<Message> getMessages(boolean isSender, String username) {
        try {
            if (isSender) return getMessagesFromSender(username);
            else return getMessagesFromReceiver(username);
        }
        catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve messages", ex);
            throw new SpyMasterException("Failed to retrieve messages");
        }
    }

    @Override
    public void updateContent(String content, Message message) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_MESSAGE))
        {
            stmt.setBoolean(1, message.getEncodedState().isSenderEncoded());
            stmt.setBoolean(2, message.getEncodedState().isReceiverEncoded());
            stmt.setString(3, content);
            stmt.setString(4, message.getSender());
            stmt.setString(5, message.getReceiver());

            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update message", ex);
            throw new SpyMasterException("Failed to update message");
        }
    }

    @Override
    public void deleteMessage(Message message) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_MESSAGE))
        {
            stmt.setString(1, message.getSender());
            stmt.setString(2, message.getReceiver());

            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete message", ex);
            throw new SpyMasterException("Failed to delete message");
        }
    }

    private List<Message> getMessagesFromSender(String username) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_MESSAGES_FROM_SENDER, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            List<Message> messages = new ArrayList<>();

            while (rs.next()) {
                messages.add(rs2Message(rs));
            }

            return messages;
        }
    }

    private List<Message> getMessagesFromReceiver(String username) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_MESSAGES_FROM_RECEIVER, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            List<Message> messages = new ArrayList<>();

            while (rs.next()) {
                messages.add(rs2Message(rs));
            }

            return messages;
        }
    }
}
