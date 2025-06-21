package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import Model.Account;
import Service.AccountService;

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

    private void registerAccountHandler(Context ctx){

        Account userAcct = ctx.bodyAsClass(Account.class);
        String username = userAcct.getUsername();
        String password = userAcct.getPassword();
        AccountService acctService = new AccountService();
        Account addedAccount = new Account();

        if (username.isBlank() || password.length() < 4 ){
            ctx.status(400);
            return;
        }

        if (acctService.usernameExists(username)){
            ctx.status(400);
        }

        addedAccount = acctService.registerUser(username, password);
        if (addedAccount != null) ctx.status(200).json(addedAccount);
        else ctx.status(400);

        

        //call the service object to process the acct registration
        //return success if the acct is created, otherwise return failure

        //The registration will be successful if and only if the username is not blank, the password 
        //is at least 4 characters long, and an Account with that username does not already exist. 
        //If all these conditions are met, the response body should contain a JSON of the Account, 
        //including its account_id. The response status should be 200 OK, which is the default. 
        //The new account should be persisted to the database. If the registration is not successful, 
        //the response status should be 400. (Client error)


    }
    
    private void getMessagesByAcctHandler(Context ctx){
        String accountId = ctx.pathParam("acct-id");
    }
    
    private void patchMessageByIdHandler(Context ctx){
        String messageId = ctx.pathParam("message-id");
    }
    
    private void deleteMessageByIdHandler(Context ctx){
        String messageId = ctx.pathParam("message-id");
    }
    
    private void getMessageByIdHandler(Context ctx){
        String messageId = ctx.pathParam("message-id");
    }


    private void postMessagesHandler(Context context){
    }

    private void getMessagesHandler(Context context){


    }

    private void loginHandler(Context context){
        
    }
    
}