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

    public Account registerUser (String username, String password){

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (username, password) values (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_author_id, username, password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
