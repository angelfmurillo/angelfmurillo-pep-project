package DAO;

import java.sql.*;
import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public boolean usernameExists(String username){
        
        Connection connection = ConnectionUtil.getConnection();
        boolean usernameExists = false;

        try{
           String sql = "select count(*) from account where username = ?;";
           PreparedStatement ps = connection.prepareStatement(sql);
           ps.setString(1, username);
           ResultSet rs = ps.executeQuery();
           if (rs.next()){ 
               int count = rs.getInt(1); 
               if (count > 0) usernameExists = true;
           }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        return usernameExists;
    }

    public Account getAccount(String username, String password){

        Account acct = null;

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select account_id from account where username = ? and password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
        
            if(resultSet.next()){
                int acctId = resultSet.getInt("account_id");
                acct =  new Account(acctId, username, password);
            }
        }catch(SQLException e){ 
           System.out.println(e.getMessage()); 
        }
        
        return acct;
    }

    public Account registerUser (String username, String password){

        Account acct = null;
        int affectedRows;

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (username, password) values (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, username);
            ps.setString(2, password);
            affectedRows = ps.executeUpdate();
        
            if (affectedRows > 0){
              ResultSet pkeyResultSet = ps.getGeneratedKeys();
              if(pkeyResultSet.next()){
                 int generated_author_id = (int) pkeyResultSet.getLong(1);
                 acct = new Account(generated_author_id, username, password);
              }
            }
            
        }catch(SQLException e){ System.out.println(e.getMessage());}

        return acct;
    }
}
