package org.example;

import java.util.HashSet;
import java.util.Set;

public class DatabaseConnector {

    public Set<String> getSubscribers() {
        Set<String> subs = new HashSet<>();
        subs.add("email1");
        subs.add("email2");
        return subs;
    }

    public void setDatabase(String databaseName) {}

    public boolean checkDatabase(String databaseName) {
        String changedDatabaseName = createDatabaseName(databaseName);
        return check(changedDatabaseName);
    }

    public String createDatabaseName(String name) {
        return "Changed" + name;
    }

    public boolean check(String name) {
        return name.contains("Changed");
    }

    public String addSubscriber(String user) {
        return user;
    }

    public void write(String ln) {
        System.out.println("some " + ln);
    }
}
