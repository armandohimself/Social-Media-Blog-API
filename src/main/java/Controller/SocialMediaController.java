package Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // POST - CREATE
            // Account
            app.post("/register", this::createAccount);
            app.post("/login", this::login);

            // Message
            app.post("/messages", this::createMessage);

        // GET - READ
            // Account
            app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountId);

            // Message
            app.get("/messages", this::getMessages);
            app.get("/messages/{message_id}", this::getMessageById);

        // PUT/PATCH - UPDATE
            // Account

            // Message
            app.patch("messages/{message_id}", this::updateMessageById);

        // DELETE - DELETE
            // Account 

            // Message
            app.delete("/messages/{message_id}", this::deleteMessageById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //! ACCOUNT
    private void createAccount(Context ctx) throws JsonProcessingException {
        // We want the ObjectMapper for the JSON
        ObjectMapper om = new ObjectMapper();

        // Because it's a POST we want to read what's in the body and encapsulate in the Account Model
        Account account = om.readValue(ctx.body(), Account.class);

        // Use the AccountService for the business logic to pass the account info from body to then pass to DAO
        Account createdAccount = accountService.createAccount(account);

        // Guard Clause - createdAccount didn't return null
        if (createdAccount != null) {
            ctx.json(createdAccount).status(200);
        } else {
            ctx.status(400); // Bad Request
        }
    }

    private void login(Context ctx) throws JsonProcessingException {
        // Create ObjectMapper to pull the JSON out from the ctx.body()
        ObjectMapper om = new ObjectMapper();

        // Grab body from Request and Map it based on Account Model
        Account userCredentials = om.readValue(ctx.body(), Account.class);

        // Send creds to AccountService to try and login
        Account matchedAccount = accountService.login(userCredentials);

        // Process the loginStatus return from the login Attempt
        if (matchedAccount != null) {
            ctx.json(matchedAccount).status(200);
        } else {
            ctx.status(401); // UNAUTHORIZED - Client does not have authorization or has given wrong credentials
        }
    }

    
    //! MESSAGE
    private void createMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        Message message = om.readValue(ctx.body(), Message.class);

        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            ctx.json(createdMessage).status(200);
        } else {
            ctx.status(400); // Client error
        }
    }

    private void getMessages(Context ctx) {
        ctx.json(messageService.getMessage()).status(200);
    }

    private void getMessageById(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageById = messageService.getMessageByMessageId(message_id);

        if (messageById == null) {
            ctx.status(200);
        } else {
            ctx.json(messageById).status(200);
        }
    }

    private void deleteMessageById(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessageById(message_id);

        if (deletedMessage == null) {
            ctx.status(200);
        } else {
            ctx.json(deletedMessage).status(200);
        }
    }

    private void updateMessageById(Context ctx) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        Message newMessage = om.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message updatedMessage = messageService.updateMessageById(newMessage, message_id);

        if(updatedMessage != null) {
            ctx.json(updatedMessage).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesByAccountId(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> allMessages = new ArrayList<>();
        allMessages = messageService.getAllMessagesByAccountId(account_id);

        if (allMessages == null) {
            ctx.result("[]").status(200);
        } else {
            ctx.json(allMessages).status(200);
        }
    }

// END
}