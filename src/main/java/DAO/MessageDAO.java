package DAO;


import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;

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
    
}