package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    // Create instance of AccountDAO when AccountService object is created in Controller
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Helps mock behavior
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) {
        // Business logic to protect against certain conditions
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        // Pass the (transient account object AKA the data from POST) and get back a persisted account object
        Account createdAccount = this.accountDAO.insertAccount(account);
        return createdAccount;
    }

    public Account login(Account account) {
        // Grab what's in the DB based on the account's credentials
        Account existingAccount = this.accountDAO.getAccountByUsername(account);
        
        if (existingAccount != null) {
            // Check username & password match
            if (existingAccount.getUsername().equals(account.getUsername()) && 
                existingAccount.getPassword().equals(account.getPassword())) {
                return existingAccount;
            }
        }
        
        return null;
    }


}