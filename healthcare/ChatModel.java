package com.example.healthcare;

public class ChatModel {

    private String message;
    private String sender;

    public String getMessage()
    {
        return message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public ChatModel(String message, String sender)
    {
        this.message = message;
        this.sender = sender;
    }

}
