package com.glen.client;

import com.glen.client.Ui.ChatFrame;
import com.glen.client.Ui.LoginFrame;

//启动类
public class ClientApp {

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient();
        tcpClient.connection();
        tcpClient.startClientThread();
        LoginFrame loginFrame = new LoginFrame(tcpClient);
        tcpClient.setLoginFrame(loginFrame);
    }


}
