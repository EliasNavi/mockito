package org.example;

import java.util.Set;

import static org.example.MessageService.getString;

public class LetterSender {
    private DatabaseConnector databaseConnector;
    private MessageService messageService;

    public LetterSender(
        DatabaseConnector databaseConnector,
        MessageService messageService
    ) {
        this.databaseConnector = databaseConnector;
        this.messageService = messageService;
    }

    public void sendMessage(String subj) {
        Set<String> subscribers = databaseConnector.getSubscribers();
        if (numberOfSubs() == 0) {
            throw new Error();
        }
        messageService.sendMessage(subj, subscribers);

        String databaseName = "new";
        databaseConnector.setDatabase(databaseName);
    }

    public int numberOfSubs() {
        return databaseConnector.getSubscribers().size();
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void systemStatus(String status) {
        messageService.checkStatus(status);
    }

    private String getName(String fakeArg) {
        return "LetterSender";
    }

    public void addUser(String userName) {
        databaseConnector.addSubscriber(userName);
    }

    public void getStatic(String staticString) {
        getString(staticString);
    }
}
