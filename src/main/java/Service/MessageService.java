package Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

public class MessageService {
    
    //method to register a user
    public boolean registerUser(String username){
        //Use a UserAcctDAO object to call methods that figure out if the user already exists
        //Use a UserAcctDAo object to create a new account if the user doesn't exists

        //The registration will be successful if Account with that username does not already exist. 
        //If all these conditions are met, the response body should contain a JSON of the Account, 
        //including its account_id. The response status should be 200 OK, which is the default. 
        //The new account should be persisted to the database. If the registration is not successful, 
        //the response status should be 400. (Client error)
        return true;
        
    }
    //## 1: Our API should be able to process new User registrations.
    //As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
    //The body will contain a representation of a JSON Account, but will not contain an account_id.



    //method to login a user
    //## 2: Our API should be able to process User logins.
    //As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
    //The request body will contain a JSON representation of an Account, not containing an account_id. 
    //In the future, this action may generate a Session token to allow the user to securely use the site. 
    //We will not worry about this for now.


    //method to submit a new message by a specific user
    //## 3: Our API should be able to process the creation of new messages.
    //As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
    //The request body will contain a JSON representation of a message, which should be persisted to the database, 
    //but will not contain a message_id.
}
