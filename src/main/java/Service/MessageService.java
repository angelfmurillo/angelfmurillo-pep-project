package Service;

import java.util.List;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    /**
     * MessageService acts as the intermediary between the controller layer and the MessageDAO.
     * It provides business logic for creating, retrieving, updating, and deleting messages,
     * as well as retrieving messages by account ID or all messages in the system.
     */
    
    private static final MessageDAO msgDAO= new MessageDAO();
        
    public Message addMessage(String message, int postedBy, long timePosted){
           
        Message addedMsg = msgDAO.addMessage(message, postedBy, timePosted);
        return addedMsg;
    }

    public Message updateMessage(int messageId, String messageText){
            
        Message updatedMsg = msgDAO.updateMessage(messageId, messageText);
        return updatedMsg;
    }

    public Message getMessageById(int msgId){
        
        Message foundMsg = msgDAO.getMessageById(msgId); 
        return foundMsg;
    }

    public Message deleteMessageById(int msgId){
        
        Message deletedMessage = msgDAO.deleteMessageById(msgId);
        return deletedMessage;
    }

    public List<Message> getMessagesByAccountId(int accountId){

        List<Message> allMessagesByAcct = msgDAO.getMessagesByAccountId(accountId);
        return allMessagesByAcct;

    }
     
    public List<Message> getAllMessages(){

        List<Message> allMessages = msgDAO.getAllMessages();
        return allMessages;
    }
}
