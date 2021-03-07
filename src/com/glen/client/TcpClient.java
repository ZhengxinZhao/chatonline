package com.glen.client;

import com.glen.common.*;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class TcpClient {




    //连接到服务器
    public  void connection() {
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

    //登录
    public  void login(String id, String password) {
        Request request = new Request();
        request.setRequestType(RequestType.LOGIN);
        Map<String, Object> dataMap = request.getDataMap();
        dataMap.put("id", id);
        dataMap.put("password", password);
        sendRequest(request);
    }

    //登出
    public  void logout() {
        Request request = new Request();
        request.setRequestType(RequestType.LOGOUT);
        sendRequest(request);
    }

    public  void sendRequest(Request request) {
        try {
            DataBuffer.outputStream.writeObject(request);
            DataBuffer.outputStream.flush();
        } catch (IOException e) {
            System.out.println("fail to send request !");
            e.printStackTrace();
        }
    }

    public  void close() {
        try {
            if ((DataBuffer.clientSocket != null) && (!DataBuffer.clientSocket.isClosed()))
                DataBuffer.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startClientThread(){
        new Thread(new Handler()).start();
    }
    //获取在线用户
    public void getOnlineUser(){
        Request request = new Request();
        request.setRequestType(RequestType.GET);
        Map<String, Object> dataMap = request.getDataMap();
        dataMap.put("itemName", "onlineUser");
        sendRequest(request);
    }

    //发送信息
    public void sendMessage(RequestType toWhom, String targetUserId,Message message){
        Request request = new Request();
        request.setRequestType(toWhom);
        Map<String,Object> map = request.getDataMap();
        map.put("targetUserId",targetUserId);
        map.put("message",message);
        sendRequest(request);
    }

    //接收线程，主要与UI交互
    class Handler implements Runnable {

        @Override
        public void run() {
            Response response = null;
            try {
                while (DataBuffer.clientSocket.isConnected()) {

                    response = (Response) DataBuffer.inputStream.readObject();
                    RequestType type = response.getType();

                    System.out.println("获取了响应类型：" + type);
                    if (type == RequestType.LOGOUT) {
                        if (response.getStatus() == ResponseStatus.OK)
                            DataBuffer.currentUser = null;
                        else {
                            //
                            //System.out.println("请再次尝试登出");
                        }
                    }
                    else if (type == RequestType.CHAT) {
                        if(response.getStatus()==ResponseStatus.OK){
                            System.out.println("收到来自"+response.getData("fromUserId")+"的消息："+
                                    response.getData("message"));
                        }
                        else {
                            System.out.println("消息发送失败。。。。。");
                            System.out.println("错误信息："+response.getData("errorInfo"));
                        }
                    }
                    else if (type == RequestType.BOARD) {
                        if(response.getStatus()==ResponseStatus.OK){
                            System.out.println("收到来自"+response.getData("fromUserId")+"的消息："+
                                    response.getData("message"));
                        }
                    }
                    else if (type == RequestType.LOGIN) {
                        if (response.getStatus() == ResponseStatus.OK) {//登录成功

                            //生成当前用户
                            DataBuffer.currentUser = new User((String) response.getData("id"), (String) response.getData("nickname"));
                            System.out.println(DataBuffer.currentUser.getNickname() + "登录成功");
                            //获取在线用户
                            getOnlineUser();
                        } else if (response.getStatus() == ResponseStatus.ERROR) {
                            System.out.println((String) response.getData("text"));
                        }
                    }
                    else if(type==RequestType.GET){
                        if(response.getStatus()==ResponseStatus.OK){
                            DataBuffer.onlineUser =(Map<String, String>) response.getData("onlineUser");
                            System.out.println(DataBuffer.onlineUser);
                        }
                        else{
                            System.out.println("GET请求失败！");
                        }
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

