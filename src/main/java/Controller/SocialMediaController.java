package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import static org.mockito.ArgumentMatchers.nullable;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);

        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/account/{account_id}/messages", this::getMessagesByAcctHandler);
        
        return app;
    }

    private void getMessageByIdHandler(Context context){

        /*
        As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
        The response body should contain a JSON representation of the message identified by the message_id. 
        It is expected for the response body to simply be empty if there is no such message. The response status 
        should always be 200, which is the default.


        */
        MessageService msgService = new MessageService();
        int msgId = Integer.parseInt(context.pathParam("message_id"));
        Message foundMsg = msgService.getMessageById(msgId);
        
        if (foundMsg == null ) context.status(200);  
        else context.status(200).json(foundMsg);
        
        
    }



    private void getMessagesHandler(Context context){

        /*
         As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
        The response body should contain a JSON representation of a list containing all messages retrieved 
        from the database. It is expected for the list to simply be empty if there are no messages. The response 
        status should always be 200, which is the default.
        */

        MessageService msgService = new MessageService();
        List<Message> allMsgs = msgService.getAllMessages(); 
        context.status(200).json(allMsgs);
    }

    private void loginHandler(Context ctx){

        //extract the username and password from the request
        Account userAcct = ctx.bodyAsClass(Account.class);
        String username = userAcct.getUsername();
        String password = userAcct.getPassword();
        AccountService acctService = new AccountService();
        Account acctLoggedIn = new Account();

        acctLoggedIn = acctService.loginUser(username, password);
        if (acctLoggedIn != null) ctx.status(200).json(acctLoggedIn);
        else ctx.status(401);

        
        /*
        As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
        The request body will contain a JSON representation of an Account, not containing an account_id. 
        In the future, this action may generate a Session token to allow the user to securely use the site. 
        We will not worry about this for now.

        - The login will be successful if and only if the username and password provided in the request body 
          JSON match a real account existing on the database. If successful, the response body should contain 
          a JSON of the account in the response body, including its account_id. The response status should be 
          200 OK, which is the default.
        - If the login is not successful, the response status should be 401. (Unauthorized)
       */
    }

    private void registerAccountHandler(Context ctx){

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

        //call the service object to process the acct registration
        //return success if the acct is created, otherwise return failure

        //The registration will be successful if and only if the username is not blank, the password 
        //is at least 4 characters long, and an Account with that username does not already exist. 
        //If all these conditions are met, the response body should contain a JSON of the Account, 
        //including its account_id. The response status should be 200 OK, which is the default. 
        //The new account should be persisted to the database. If the registration is not successful, 
        //the response status should be 400. (Client error)


    }
    
    private void postMessagesHandler(Context context){

        /*
         * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
         * The request body will contain a JSON representation of a message, which should be persisted to 
         * the database, but will not contain a message_id.

         - The creation of the message will be successful if and only if the message_text is not blank, 
           is not over 255 characters, and posted_by refers to a real, existing user. If successful, the response 
           body should contain a JSON of the message, including its message_id. The response status should be 200, 
           which is the default. The new message should be persisted to the database.
           If the creation of the message is not successful, the response status should be 400. (Client error)
         * 
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


    private void getMessagesByAcctHandler(Context ctx){

        /*
         As a user, I should be able to submit a GET request on the endpoint 
         GET localhost:8080/accounts/{account_id}/messages.

       - The response body should contain a JSON representation of a list containing all messages posted by a 
       - particular user, which is retrieved from the database. It is expected for the list to simply be empty 
       - if there are no messages. The response status should always be 200, which is the default. */

        String accountId = ctx.pathParam("acct_id");
        String an = "sdfsd";
    
    }
    
    private void patchMessageByIdHandler(Context ctx){
        String messageId = ctx.pathParam("message-id");
    }
    
    private void deleteMessageByIdHandler(Context ctx){
        String messageId = ctx.pathParam("message-id");
    }
       
            
}