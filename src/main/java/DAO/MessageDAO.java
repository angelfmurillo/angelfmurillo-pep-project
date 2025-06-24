package DAO;


import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {

    public Message addMessage(String message, int postedBy, long timePosted){

        Message msg = null;
        int affectedRows;

        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, postedBy);
            ps.setString(2, message);
            ps.setLong(3, timePosted);

            affectedRows = ps.executeUpdate();
        
            if (affectedRows > 0){
              ResultSet pkeyResultSet = ps.getGeneratedKeys();
              if(pkeyResultSet.next()){
                 int generated_msg_id = (int) pkeyResultSet.getLong(1);
                 msg = new Message(generated_msg_id, postedBy, message, timePosted);
              }
            }
        
        }catch(SQLException e){ System.out.println(e.getMessage());}

        return msg;
    }

    public Message updateMessage(int messageId, String messageText){

        Message msg = null;
        int affectedRows;

        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "update message set message_text = ? where message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, messageText);
            ps.setInt(2, messageId);
            
            affectedRows = ps.executeUpdate();
        
            if (affectedRows > 0){
              ResultSet resultSet = ps.getGeneratedKeys();
              if(resultSet.next()){
                 int msg_id = (int) resultSet.getLong(1);
                 msg = new Message(messageId, postedBy, message, timePosted);
              }
            }
        
        }catch(SQLException e){ System.out.println(e.getMessage());}

        return msg;


    }


    public Message getMessageById(int msgId){

        Message msg = null;
        int postedBy;
        String messageText;
        Long timePosted;

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message where message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msgId);
            ResultSet resultSet = ps.executeQuery();
        
            if(resultSet.next()){
                postedBy = resultSet.getInt("posted_by");
                messageText = resultSet.getString("message_text");
                timePosted = resultSet.getLong("time_posted_epoch");
                msg =  new Message(msgId, postedBy, messageText, timePosted);
            }
        }catch(SQLException e){ 
           System.out.println(e.getMessage()); 
        }
        
        return msg;
    }

    public Message deleteMessageById(int msgId){

        int affectedRows = 0;
        Message msg = getMessageById(msgId);


        if (!(msg == null)){
            Connection connection = ConnectionUtil.getConnection();
            try {
              String sql = "delete from message where message_id = ?;";
              PreparedStatement ps = connection.prepareStatement(sql);
              ps.setInt(1, msgId);
              affectedRows = ps.executeUpdate();
            
            }catch(SQLException e){ System.out.println(e.getMessage());}
        
            if (affectedRows == 1)
              return msg;
        }
        
        return msg;
    }


    public List<Message> getAllMessages(){

        int msgId, postedBy;
        String message;
        long timePosted;
        List<Message> allMessages = new ArrayList<Message>();
        
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from message;";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
        
            while (resultSet.next()){
                msgId = resultSet.getInt("message_id");
                postedBy = resultSet.getInt("posted_by");
                message = resultSet.getString("message_text");
                timePosted = resultSet.getLong("time_posted_epoch");
                allMessages.add(new Message(msgId, postedBy, message, timePosted));
            }

        }catch(SQLException e){ 
           System.out.println(e.getMessage()); 
        }
        
        return allMessages;
    }   
}