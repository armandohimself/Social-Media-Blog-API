package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public Account insertAccount(Account account) {
        // Establish a connection to the DB
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int rowsAffected = ps.executeUpdate();
            // Insert into DB - rowsAffected = a # for number of rows affected 0 to many.

            if(rowsAffected > 0) { // Ensures the insert was successful
                ResultSet pkeyResultSet = ps.getGeneratedKeys();
                // After insertion DB automtically assigns a SERIAL (or AUTO_INCREMENT) to account_id.
                // ps.getGeneratedKeys() fetches account_id value, allowing us to return a complete Account object

                if(pkeyResultSet.next()) {
                    int generated_account_id = (int) pkeyResultSet.getLong(1); 
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByUsername(Account account) {
        // Step 1) Get Establish Connection Pool to DB
        Connection connection = ConnectionUtil.getConnection();

        // Step 2) Wrap the try/catch clause because we are attempting to SQL
        try {
            // Step 3) Create SQL to Retrieve Account By Username
            String sql = "SELECT * FROM account WHERE username=?";

            // Step 4) Prepare Statement because SQL Injections
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());

            // Process ResultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                return new Account(account_id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByAccountId(int accountId) {
        // Step 1) Get Establish Connection Pool to DB
        Connection connection = ConnectionUtil.getConnection();

        // Step 2) Wrap the try/catch clause because we are attempting to SQL
        try {
            // Step 3) Create SQL to Retrieve Account By Username
            String sql = "SELECT * FROM account WHERE account_id=?";

            // Step 4) Prepare Statement because SQL Injections
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);

            // Process ResultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                return new Account(account_id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    // END
}