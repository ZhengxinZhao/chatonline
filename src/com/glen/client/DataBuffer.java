package com.glen.client;

import com.glen.common.User;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class DataBuffer {
    // 当前客户端的用户信息
    public static User currentUser;
    // 当前客户端连接到服务器的套节字
    public static Socket clientSocket;
    // 当前客户端连接到服务器的输出流
    public static ObjectOutputStream outputStream;
    // 当前客户端连接到服务器的输入流
    public static ObjectInputStream inputStream;
    //服务器地址
    public static final String SERVER_IP = "127.0.0.1";
    //服务器端口号
    public static final int SERVER_PORT = 8980;

    private DataBuffer() {
    }
}
