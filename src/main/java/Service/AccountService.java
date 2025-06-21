package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    

    public boolean usernameExists(String username){
        
        AccountDAO actDAO = new AccountDAO();

        return actDAO.usernameExists(username);

    }

    //method to register a user
    public Account registerUser(String username, String password){
        //Use a UserAcctDAO object to call methods that figure out if the user already exists
        
        AccountDAO actDAO= new AccountDAO();
        Account act = null;

        if (!actDAO.usernameExists(username)){ 
            act = actDAO.registerUser(username, password);
        }

        return act;

        //Use a UserAcctDAo object to create a new account if the user doesn't exists

        //The registration will be successful if Account with that username does not already exist. 
        //If all these conditions are met, the response body should contain a JSON of the Account, 
        //including its account_id. The response status should be 200 OK, which is the default. 
        //The new account should be persisted to the database. If the registration is not successful, 
        //the response status should be 400. (Client error)
                
    }
}
