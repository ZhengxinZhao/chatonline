package com.glen.common;

import java.io.Serializable;

public class Message implements Serializable {
    private String text;
    public Message(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                '}';
    }
}
