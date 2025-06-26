package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {
    /**
    * SocialMediaController is responsible for defining all HTTP endpoints for the social media API.
    * It sets up routes to handle account registration, login, and operations on messages (create, read, 
    * update, delete). This controller delegates business logic to the AccountService and MessageService classes,
    * and returns appropriate HTTP responses based on user input and database interactions.
    * 
    * @return a Javalin app object which defines the behavior of the Javalin controller.
    */
    
    public Javalin startAPI() {
        
        Javalin app = Javalin.create();
        
        app.get("/accounts/{account_id}/messages", this::getMessagesByAcctHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
               
        return app;
    }

    private void getMessagesByAcctHandler(Context context){

    /**
     * Handles GET requests to retrieve all messages posted by a specific account.
     * Responds with a JSON list of messages for the given account_id.
    */
    
        MessageService msgService = new MessageService();
        int acctId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> allMsgs = msgService.getMessagesByAccountId(acctId); 
        context.json(allMsgs);
    }

    private void patchMessageByIdHandler(Context context){

     /**
     * Handles PATCH requests to update an existing message's text.
     * Validates the new message text and updates the message if valid.
     * Returns 200 with the updated message, or 400 on failure.
     */        

        Message msg = context.bodyAsClass(Message.class);
        String msgText = msg.getMessage_text();
        MessageService msgService = new MessageService();
        Message updatedMsg = new Message();
        int msgMaxLength = 255;
        int msgId = Integer.parseInt(context.pathParam("message_id"));
        

        if (msgText.isBlank() || (msgText.length() >= msgMaxLength) ){
            context.status(400);
            return;
        }

        updatedMsg = msgService.updateMessage(msgId, msgText);
        if (updatedMsg != null) 
           context.status(200).json(updatedMsg);
        else 
           context.status(400);
    }
    
    private void deleteMessageByIdHandler(Context context){

        /**
         * Handles DELETE requests to remove a message by its ID.
         * Returns the deleted message in the response body if found; otherwise, returns an empty body.
         */
          
        MessageService msgService = new MessageService();
        int msgId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMsg = msgService.deleteMessageById(msgId);
        
        if (deletedMsg == null ) context.status(200);  
        else context.status(200).json(deletedMsg);
    }

    private void getMessageByIdHandler(Context context){

        /**
        * Handles GET requests to retrieve a specific message by its ID.
        * Responds with the message in JSON format if found, or an empty body if not.
        */

        MessageService msgService = new MessageService();
        int msgId = Integer.parseInt(context.pathParam("message_id"));
        Message foundMsg = msgService.getMessageById(msgId);
        
        if (foundMsg == null ) context.status(200);  
        else context.status(200).json(foundMsg);
    }

    private void getMessagesHandler(Context context){

        /**
         * Handles GET requests to retrieve all messages from the database.
         * Responds with a JSON list of all messages.
        */

        MessageService msgService = new MessageService();
        List<Message> allMsgs = msgService.getAllMessages(); 
        context.status(200).json(allMsgs);
    }

    private void postMessagesHandler(Context context){

        /**
         * Handles POST requests to create a new message.
         * Validates the message content and adds it to the database.
         * Responds with the created message or 400 on failure.
         */

        Message userMessage = context.bodyAsClass(Message.class);
        String message = userMessage.getMessage_text();
        int postedById = userMessage.getPosted_by();
        long timePosted = userMessage.getTime_posted_epoch();
        int messageMaxLength = 255;
        Message addedMessage = new Message();
        MessageService msgService = new MessageService();

        if (message.isBlank() || (message.length() > messageMaxLength) ){
            context.status(400);
            return;
        }

        addedMessage = msgService.addMessage(message, postedById, timePosted);

        if (addedMessage != null)
           context.status(200).json(addedMessage);
        else 
           context.status(400);
    }


    private void loginHandler(Context ctx){

        /**
        * Handles POST requests to authenticate a user. Expects a username and password in the request body.
        * Responds with account data if authentication is successful, or 401 if not.
        */

        Account userAcct = ctx.bodyAsClass(Account.class);
        String username = userAcct.getUsername();
        String password = userAcct.getPassword();
        AccountService acctService = new AccountService();
        Account acctLoggedIn = new Account();

        acctLoggedIn = acctService.loginUser(username, password);
        if (acctLoggedIn != null) ctx.status(200).json(acctLoggedIn);
        else ctx.status(401);
      
    }

    private void registerAccountHandler(Context ctx){

        /**
         * Handles POST requests to register a new user account.
         * Validates the input and responds with the created account or 400 on validation failure or conflict.
         */

        Account userAcct = ctx.bodyAsClass(Account.class);
        String username = userAcct.getUsername();
        String password = userAcct.getPassword();
        AccountService acctService = new AccountService();
        Account addedAccount = new Account();
        int passwordMaxLength = 4;

        if (username.isBlank() || (password.length() < passwordMaxLength) ){
            ctx.status(400);
            return;
        }

        addedAccount = acctService.registerUser(username, password);
        if (addedAccount != null) 
           ctx.status(200).json(addedAccount);
        else 
           ctx.status(400);
    }
}
