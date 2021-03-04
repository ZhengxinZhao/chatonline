package com.glen.client;

import com.glen.common.*;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class TcpClient {


    public static void main(String[] args) {

        connection();

        Scanner scanner = new Scanner(System.in);
        String id ;
        String password;
        //test
        if (true) {
            id = scanner.next();
            password = scanner.next();
            login(id, password);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        close();

    }

    //连接到服务器
    public static void connection() {
        String ip = DataBuffer.SERVER_IP;
        int port = DataBuffer.SERVER_PORT;
        try {
            DataBuffer.clientSocket = new Socket(ip, port);
            DataBuffer.outputStream = new ObjectOutputStream(DataBuffer.clientSocket.getOutputStream());
            DataBuffer.inputStream = new ObjectInputStream(DataBuffer.clientSocket.getInputStream());
            System.out.println("连接服务器成功!");
        } catch (IOException e) {
            System.out.println("连接服务器失败,请检查!");
            e.printStackTrace();
        }
    }

    //在clientThread开启之前使用
    public static boolean login(String id, String password) {
        Request request = new Request();
        request.setRequestType(RequestType.LOGIN);
        Map<String, Object> dataMap = request.getDataMap();
        dataMap.put("id", id);
        dataMap.put("password", password);

        //响应结果
        Response response = null;
        try {
            sendRequest(request);
            response = (Response) DataBuffer.inputStream.readObject();
            if (response.getStatus() == ResponseStatus.OK) {//登录成功

                //生成当前用户
                DataBuffer.currentUser = new User((String) response.getData("id"),(String) response.getData("nickname"));
                System.out.println(DataBuffer.currentUser.getNickname() + "登录成功");
                return true;

            } else if(response.getStatus() == ResponseStatus.ERROR){
                System.out.println((String) response.getData("text"));
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("fail to login!");
            e.printStackTrace();
        }
        return false;
    }

    public static void sendRequest(Request request) {
        try {
            DataBuffer.outputStream.writeObject(request);
            DataBuffer.outputStream.flush();
        } catch (IOException e) {
            System.out.println("fail to send request !");
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            if ((DataBuffer.clientSocket != null) && (!DataBuffer.clientSocket.isClosed()))
                DataBuffer.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//接收线程，主要与UI交互
    class clientThread extends Thread {

        @Override
        public void run() {
            Response response = null;
            try {
                while (DataBuffer.clientSocket.isConnected()) {

                    response = (Response) DataBuffer.inputStream.readObject();
                    RequestType type = response.getType();

                    System.out.println("获取了响应内容：" + type);
                    if (type == RequestType.LOGIN) {
                        //不应该在这处理,出现在这里说明发生错误
                    }else if(type == RequestType.LOGOUT){

                    }else if(type == RequestType.BOARD){

                    }else if(type == RequestType.CHAT){

                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

