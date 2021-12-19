package com.example.a2family.Classes;

import java.sql.Timestamp;


public class Message {
    private String messageUserID;
    private String messageUserName;
    private String messageText;
    private String timestamp;

    public Message() {
    }

    public Message(String messageUserID, String messageUserName, String messageText, String timestamp) {
        this.messageUserID = messageUserID;
        this.messageUserName = messageUserName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getMessageUserID() {
        return messageUserID;
    }

    public String getMessageUserName() {
        return messageUserName;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessageUserID(String messageUserID) {
        this.messageUserID = messageUserID;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageUserID='" + messageUserID + '\'' +
                ", messageUserName='" + messageUserName + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
