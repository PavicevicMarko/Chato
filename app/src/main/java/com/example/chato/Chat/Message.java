package com.example.chato.Chat;

import java.util.ArrayList;

public class Message {

    String messageID,
            senderID,
            text;

    ArrayList<String> mediaUrlList;

    public Message(String messageID, String senderID, String text,
                   ArrayList<String> mediaUrlList) {
        this.messageID= messageID;
        this.senderID= senderID;
        this.text= text;
        this.mediaUrlList = mediaUrlList;
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

    public ArrayList<String> getMediaUrlList() {
        return mediaUrlList;
    }
}
