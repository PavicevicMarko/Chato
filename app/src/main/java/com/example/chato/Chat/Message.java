package com.example.chato.Chat;

public class Message {

    String messageID,
            senderID,
            text;

    public Message(String messageID, String senderID, String text) {
        this.messageID= messageID;
        this.senderID= senderID;
        this.text= text;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getText() {
        return text;
    }

}
