package com.example.chandru.miniprojectchatbot;

/**
 * Created by Chandru on 12-03-2019.
 */

public class BaseMessage {
    String message;
    Integer sentBy;

    public BaseMessage(String message, Integer sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSentBy() {
        return sentBy;
    }

    public void setSentBy(Integer sentBy) {
        this.sentBy = sentBy;
    }
}
