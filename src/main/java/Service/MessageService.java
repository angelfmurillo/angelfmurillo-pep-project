package Service;

import java.util.List;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    //method to register a user
    
    //method to submit a new message by a specific user
    //## 3: Our API should be able to process the creation of new messages.
    //As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
    //The request body will contain a JSON representation of a message, which should be persisted to the database, 
    //but will not contain a message_id.

    public Message addMessage(String message, int postedBy, long timePosted){
        
        
        MessageDAO msgDAO= new MessageDAO();
        Message msg = null;

        msg = msgDAO.addMessage(message, postedBy, timePosted);
        
        return msg;
    }

    public List<Message> getAllMessages(){

        MessageDAO msgDAO= new MessageDAO();

        return msgDAO.getAllMessages();
    }


}
