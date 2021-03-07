package com.glen.server;

import java.io.IOException;

public class testServer {
    public static void main(String[] args) {
        try {
            TcpServer tcpServer = new TcpServer();
            tcpServer.service();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
