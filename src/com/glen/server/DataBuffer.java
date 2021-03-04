package com.glen.server;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class DataBuffer {
    public static ConcurrentHashMap<String, String> loginInfo ;//<id,password>
    public static ConcurrentHashMap<String, String> userInfo; //<id , nickname>
    public static ConcurrentHashMap<String, Socket> userSocket;
    static {
        userSocket = new ConcurrentHashMap<>();
        loginInfo = new ConcurrentHashMap<>();
        userInfo = new ConcurrentHashMap<>();
        loginInfo.put("10999","1234");
        userInfo.put("10999","潇");
        loginInfo.put("58537","1234");
        userInfo.put("58537","正心");
    }

    private DataBuffer() {
    }
}
