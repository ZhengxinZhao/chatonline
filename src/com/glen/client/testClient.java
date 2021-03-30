package com.glen.client;
import com.glen.common.Message;
import com.glen.common.RequestType;

import java.util.Scanner;

public class testClient {
    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient();
        tcpClient.connection();
        tcpClient.startClientThread();
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true){
            command = scanner.nextLine();
            if(command.equals("login")){
                System.out.println("输入用户id和密码：");
                String id=scanner.nextLine();
                String password=scanner.nextLine();
                tcpClient.login(id, password);
            }
            else if(command.equals("logout")){
                tcpClient.logout();
            }
            else if(command.equals("get ou")){
                tcpClient.getOnlineUser();
            }
            else if(command.equals("chat")){
                System.out.println("输入目标id和文本内容：");
                String id=scanner.nextLine();
                String text=scanner.nextLine();
                tcpClient.sendMessage(RequestType.CHAT,id,new Message(text));
            }
            else if(command.equals("FSEND")){
                System.out.println("输入发送文本内容：");
                String text=scanner.nextLine();
                tcpClient.sendMessage(RequestType.FSEND,null, new Message(text));
            }
            else{
                System.out.println("指令错误");
            }
        }


    }
}
