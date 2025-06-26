package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {

    public Message addMessage(String message, int postedBy, long timePosted){

        Message msg = null;
        String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?);";
        
        try (
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            )
        {
            
            ps.setInt(1, postedBy);
            ps.setString(2, message);
            ps.setLong(3, timePosted);

            int affectedRows = ps.executeUpdate();
        
            if (affectedRows > 0){
              ResultSet pkeyResultSet = ps.getGeneratedKeys();
              if(pkeyResultSet.next()){
                 int generated_msg_id = (int) pkeyResultSet.getLong(1);
                 msg = new Message(generated_msg_id, postedBy, message, timePosted);
              }
            }
        }
        catch(SQLException e){ System.out.println(e.getMessage());}

        return msg;
    }

    public Message updateMessage(int messageId, String messageText){

        Message msg = getMessageById(messageId);
        int affectedRows;
        String sql = "update message set message_text = ? where message_id = ?;";
              
        if (!(msg == null)){
        
            try(
                Connection connection = ConnectionUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {   
              
              ps.setString(1, messageText);
              ps.setInt(2, messageId);
            
              affectedRows = ps.executeUpdate();
        
              if (affectedRows == 1){
                msg.setMessage_text(messageText);
              }
        
            }catch(SQLException e){ System.out.println(e.getMessage());}
        }

        return msg;
    }

    public Message getMessageById(int msgId){

        Message msg = null;
        String sql = "select * from message where message_id = ?;";
            
        try(
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
           )
        {
            
            ps.setInt(1, msgId);
            try (ResultSet resultSet = ps.executeQuery())
            {
        
               if(resultSet.next()){
                 int postedBy = resultSet.getInt("posted_by");
                 String messageText = resultSet.getString("message_text");
                 long timePosted = resultSet.getLong("time_posted_epoch");
                 msg =  new Message(msgId, postedBy, messageText, timePosted);
               }
            }
        
        }catch(SQLException e){ System.out.println(e.getMessage()); }
        
        return msg;
    }

    public Message deleteMessageById(int msgId){

        int affectedRows = 0;
        Message msg = getMessageById(msgId);
        String sql = "delete from message where message_id = ?;";
              

        if (!(msg == null)){
            try (
                Connection connection = ConnectionUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                )
            {
              ps.setInt(1, msgId);
              affectedRows = ps.executeUpdate();
            
            }catch(SQLException e){ System.out.println(e.getMessage());}
        
            if (affectedRows == 1)
              return msg;
        }
        
        return msg;
    }

    public List<Message> getMessagesByAccountId(int accountId){

        List<Message> allMessages = new ArrayList<Message>();
        String sql = "select * from message where posted_by = ?;";
            
        try (
             Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
            )
        {
            ps.setInt(1, accountId);
            
            try (ResultSet resultSet = ps.executeQuery()){;
        
              while (resultSet.next()){
                  int msgId = resultSet.getInt("message_id");
                  String message = resultSet.getString("message_text");
                  long timePosted = resultSet.getLong("time_posted_epoch");
                  allMessages.add(new Message(msgId, accountId, message, timePosted));
              }
            }

        }catch(SQLException e){ 
           System.out.println(e.getMessage()); 
        }
        
        return allMessages;
    }   

    public List<Message> getAllMessages(){

        List<Message> allMessages = new ArrayList<Message>();
        String sql = "select * from message;";
            
        try
           ( 
             Connection connection = ConnectionUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql);
           ) 
        {
             while (resultSet.next()){
                int msgId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String message = resultSet.getString("message_text");
                long timePosted = resultSet.getLong("time_posted_epoch");
                allMessages.add(new Message(msgId, postedBy, message, timePosted));
            }

        }catch(SQLException e){ 
            System.out.println(e.getMessage());
        }
        
        return allMessages;
    }   
}