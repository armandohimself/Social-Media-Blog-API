package Service;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    AccountService accountService;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    public Message createMessage(Message message) {
        // Guard Clause
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() 
            || message.getMessage_text().length() > 255) {
            return null;
        }

        // Check posted_by refers to a real, existing user
        boolean isAccount = accountService.checkExistingAccountById(message.getPosted_by());

        if(isAccount) {
            Message createdMessage = messageDAO.insertMessage(message);
            return createdMessage;
        }
    return null;
    }
}