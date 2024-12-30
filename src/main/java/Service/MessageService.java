package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Account;
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

    public List<Message> getMessage() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByMessageId(int message_id) {
        return messageDAO.getMessageByMessageId(message_id);
    }

    public Message deleteMessageById(int message_id) {
        Message deletedMessage = messageDAO.deleteMessageById(message_id);

        return deletedMessage;
    }

    public Message updateMessageById(Message newMessage, int message_id) {

        // Guard Clause - Check if exists
        Message existingMessage = getMessageByMessageId(message_id);
        System.out.println("Does a message exist: " + existingMessage);

        if (existingMessage == null || 
            newMessage.getMessage_text().isBlank() || 
            newMessage.getMessage_text().length() > 255) {
            
            return null;
        } else {
            // Update posted_by and time_posted_epoch in newMessage
            newMessage.setPosted_by(existingMessage.getPosted_by());
            newMessage.setTime_posted_epoch(existingMessage.getTime_posted_epoch());

            return messageDAO.updateMessageById(newMessage, message_id);
        }
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        boolean existingAccount = accountService.checkExistingAccountById(account_id);

        if (existingAccount) {
            return messageDAO.getAllMessagesByAccountId(account_id);
        } else {
            return null;
        }
    }
    //END
}