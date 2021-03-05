package com.glen.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class DataBuffer {
    public static ConcurrentHashMap<String, String> loginInfo ;//<id,password>
    public static ConcurrentHashMap<String, String> userInfo; //<id , nickname>
    public static ConcurrentHashMap<String, Socket> userSocket;//<id, socket>
    public static ConcurrentHashMap<String, String> onlineUser; //<id , nickname>
    public static ConcurrentHashMap<Socket, ObjectInputStream> oisCache;
    public static ConcurrentHashMap<Socket, ObjectOutputStream> oosCache;
    static {
        userSocket = new ConcurrentHashMap<>();
        loginInfo = new ConcurrentHashMap<>();
        userInfo = new ConcurrentHashMap<>();
        onlineUser = new ConcurrentHashMap<>();
        oisCache = new ConcurrentHashMap<>();
        oosCache = new ConcurrentHashMap<>();
        loginInfo.put("10999","1234");
        userInfo.put("10999","潇");
        loginInfo.put("58537","1234");
        userInfo.put("58537","正心");
    }

    private DataBuffer() {
    }
}
