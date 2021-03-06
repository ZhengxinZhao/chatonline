package com.glen.client;

import com.glen.client.Ui.ChatFrame;
import com.glen.client.Ui.FriendListPanel;
import com.glen.client.Ui.LoginFrame;
import com.glen.common.*;

import java.io.*;
import java.net.Socket;
import java.util.Map;


public class TcpClient {

    ChatFrame chatFrame;
    LoginFrame loginFrame;
    FriendListPanel friendListPanel;

    public void setFriendListPanel(FriendListPanel friendListPanel) {
        this.friendListPanel = friendListPanel;
    }


    public void setChatFrame(ChatFrame chatFrame){
        this.chatFrame = chatFrame;
    }
    public void setLoginFrame(LoginFrame loginFrame){
        this.loginFrame = loginFrame;
    }
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
                            String fromUserId = (String) response.getData("fromUserId");
                            Message message = (Message) response.getData("message");
                            chatFrame.showMessage(message,fromUserId,false);
                            System.out.println("收到来自"+fromUserId+"的消息："+message);
                        }
                        else {
                            System.out.println("消息发送失败。。。。。");
                            System.out.println("错误信息："+response.getData("errorInfo"));
                        }
                    }
                    else if (type == RequestType.FSEND) {
                        if(response.getStatus()==ResponseStatus.OK){
                            String fromUserId = (String) response.getData("fromUserId");
                            Message message = (Message) response.getData("message");
                            chatFrame.showMessage(message,fromUserId,true);
                            System.out.println("收到来自"+fromUserId+"的消息："+message);
                        }
                    }
                    else if (type == RequestType.LOGIN) {
                        if (response.getStatus() == ResponseStatus.OK) {//登录成功
                            //生成当前用户
                            DataBuffer.currentUser = new User((String) response.getData("id"), (String) response.getData("nickname"));
                            System.out.println(DataBuffer.currentUser.getNickname() + "登录成功");
                            //通知UI
                            loginFrame.onLoginSucceed();
                            //获取在线用户
                            getOnlineUser();
                        } else if (response.getStatus() == ResponseStatus.ERROR) {
                            String errorInfo = (String) response.getData("text");
                            System.out.println(errorInfo);
                            //通知UI
                            loginFrame.onLoginFailed( errorInfo);
                        }
                    }
                    else if(type==RequestType.GET){
                        if(response.getStatus()==ResponseStatus.OK){
                            DataBuffer.onlineUser = (Map<String, String>) response.getData("onlineUser");
                            System.out.println(DataBuffer.onlineUser);
                            //通知UI
                            friendListPanel.onOnlineUserChange();
                        }
                        else{
                            System.out.println("GET请求失败！");
                        }
                    }else if(type==RequestType.INFORM){//来自服务器的通知
                        String informName = (String) response.getData("informName");
                        if(informName.equals("loginTip")){
                            String id = (String) response.getData("id") ;
                            String nickName = (String)response.getData("nickName") ;
                            DataBuffer.onlineUser.put(id,nickName);
                            if(friendListPanel!=null)
                                friendListPanel.onOnlineUserChange();
                        }
                        else if(informName.equals("logoutTip")){
                            String id = (String) response.getData("id") ;
                            DataBuffer.onlineUser.remove(id);
                            if(friendListPanel!=null)
                                friendListPanel.onOnlineUserChange();
                        }
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

