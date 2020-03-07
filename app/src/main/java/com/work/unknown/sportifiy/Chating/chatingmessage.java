package com.work.unknown.sportifiy.Chating;

/**
 * Created by unknown on 2/25/2018.
 */

public class chatingmessage {
    String messageText;
    String from;
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public chatingmessage()
    {

    }
    public chatingmessage(String messageText, String from) {
        this.messageText = messageText;
        this.from = from;
    }
}
