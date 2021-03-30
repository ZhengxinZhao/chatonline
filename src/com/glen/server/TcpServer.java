package com.glen.server;


import com.glen.common.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private int port = 8980;//默认监听端口
    private ServerSocket serverSocket;//服务器socket
    //创建动态线程池，适合小并发量，容易出现OutOfMemoryError
    private ExecutorService executorService = Executors.newCachedThreadPool();


    public TcpServer() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("服务器启动监听...端口:" + port);
    }

    public TcpServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        System.out.println("服务器启动监听...端口:" + port);
    }

    private ObjectOutputStream getOos(Socket socket) throws IOException {
        ObjectOutputStream oos = DataBuffer.oosCache.get(socket);
        if (oos != null) return oos;
        System.out.println("新生成oos");
        //若未初始,new
        //输出流缓冲区
        OutputStream outputStream = socket.getOutputStream();
        oos = new ObjectOutputStream(outputStream);
        DataBuffer.oosCache.put(socket, oos);
        return oos;
    }

    private ObjectInputStream getOis(Socket socket) throws IOException {
        ObjectInputStream ois = DataBuffer.oisCache.get(socket);
        if (ois != null) return ois;
        //若未初始,new
        //输入流缓冲区
        InputStream inputStream = socket.getInputStream();
        ois = new ObjectInputStream(inputStream);
        DataBuffer.oisCache.put(socket, ois);
        return ois;
    }

    private void addOnlineUser(String id, String nickname, Socket socket) {
        DataBuffer.userSocket.put(id, socket);
        DataBuffer.onlineUser.put(id, nickname);
    }

    private void deleteOnlineUser(String id) {
        Socket s = DataBuffer.userSocket.get(id);
        DataBuffer.userSocket.remove(id);
        DataBuffer.onlineUser.remove(id);
        if(s!=null){
            DataBuffer.oosCache.remove(s);
            DataBuffer.oisCache.remove(s);
        }


    }

    class Handler implements Runnable {
        private Socket socket;
        private String id;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("与客户端建立连接：" + socket.getInetAddress());
            try {
                ObjectInputStream ois = getOis(socket);
                ObjectOutputStream oos = getOos(socket);

                while (true) {
                    //阻塞
                    Request request = (Request) ois.readObject();
                    //响应包
                    Response response = new Response();

                    RequestType requestType = request.getRequestType();
                    Map<String, Object> dataMap = request.getDataMap();

                    if (requestType == RequestType.LOGIN) {//登录请求
                        response.setType(RequestType.LOGIN);
                        String id = (String) dataMap.get("id");
                        String password = DataBuffer.loginInfo.get(id);
                        if (password == null) {//未注册
                            response.setStatus(ResponseStatus.ERROR);
                            response.setData("text", "请先注册！");
                        } else if (!password.equals(dataMap.get("password"))) {//密码错误
                            response.setStatus(ResponseStatus.ERROR);
                            response.setData("text", "用户名或密码错误！");
                        } else if (DataBuffer.userSocket.get(id) != null) {
                            response.setStatus(ResponseStatus.ERROR);
                            response.setData("text", "账号已在别处登录！");
                        } else {//正确登录
                            this.id = id;
                            addOnlineUser(id, DataBuffer.userInfo.get(id), socket);
                            //通知其他用户
                            Response response1 = new Response();
                            response1.setType(RequestType.INFORM);
                            response1.setStatus(ResponseStatus.OK);
                            response1.setData("informName", "loginTip");
                            response1.setData("id", this.id);
                            response1.setData("nickName",DataBuffer.userInfo.get(this.id));
                            for (Map.Entry<String, Socket> m : DataBuffer.userSocket.entrySet()
                            ) {
                                Socket socket = m.getValue();
                                ObjectOutputStream oos2 = getOos(socket);
                                oos2.writeObject(response1);
                                oos2.flush();
                            }

                            System.out.println(DataBuffer.userSocket);
                            response.clearData();
                            response.setType(RequestType.LOGIN);

                            response.setStatus(ResponseStatus.OK);
                            //发回id和昵称
                            response.setData("id", id);
                            response.setData("nickname", DataBuffer.userInfo.get(id));
                        }

                        oos.writeObject(response);
                        oos.flush();
                    } else if (requestType == RequestType.LOGOUT) {//下线请求
                        System.out.println("收到登出请求");
                        //从在线表中剔除该id用户

                        deleteOnlineUser(id);
                        //告知客户端已登出
                        response.setType(RequestType.LOGOUT);
                        response.setStatus(ResponseStatus.OK);
                        oos.writeObject(response);
                        oos.flush();
                        //通知其他用户
                        Response response1 = new Response();
                        response1.setType(RequestType.INFORM);
                        response1.setStatus(ResponseStatus.OK);
                        response1.setData("informName", "logoutTip");
                        response1.setData("id", this.id);
                        for (Map.Entry<String, Socket> m : DataBuffer.userSocket.entrySet()
                        ) {
                            Socket socket = m.getValue();
                            ObjectOutputStream oos2 = getOos(socket);
                            oos2.writeObject(response1);
                            oos2.flush();
                        }

                    } else if (requestType == RequestType.CHAT) {//私聊
                        String targetId = (String) dataMap.get("targetUserId");
                        Message message = (Message) dataMap.get("message");
                        Socket targetSocket = DataBuffer.userSocket.get(targetId);
                        response.setType(requestType);
                        if (targetSocket == null) {
                            response.setStatus(ResponseStatus.ERROR);
                            response.setData("errorInfo", "该用户已下线");
                            oos.writeObject(response);
                            oos.flush();
                        } else {
                            response.setStatus(ResponseStatus.OK);
                            response.setData("fromUserId", this.id);
                            response.setData("message", message);
                            ObjectOutputStream oos2 = getOos(targetSocket);
                            oos2.writeObject(response);
                            oos2.flush();

                        }

                    } else if (requestType == RequestType.FSEND) {//群聊
                        Message message = (Message) dataMap.get("message");
                        //set response
                        response.setType(requestType);
                        response.setStatus(ResponseStatus.OK);
                        response.setData("fromUserId", this.id);
                        response.setData("message", message);
                        //send
                        for (Map.Entry<String, Socket> m : DataBuffer.userSocket.entrySet()
                        ) {
                            Socket socket = m.getValue();
                            ObjectOutputStream oos2 = getOos(socket);
                            oos2.writeObject(response);
                            oos2.flush();
                        }
                    } else if (requestType == RequestType.GET) {//请求资源
                        response.setType(requestType);
                        response.setStatus(ResponseStatus.OK);
                        if ("onlineUser".equals(request.getDataMap().get("itemName")))
                            response.setData("onlineUser", DataBuffer.onlineUser);
                        oos.writeObject(response);
                        oos.flush();
                    }

                }

            } catch (EOFException eof) {
                //直接到finally，关闭连接
                System.out.println(this.id + "连接断开");
            } catch (IOException | ClassNotFoundException e) {
                if (e instanceof IOException) {
                    System.out.println(this.id + "连接断开");
                } else {
                    e.printStackTrace();
                }


            } finally {
                //若连接异常断开,从在线表中剔除该id用户
                if (id != null)
                    deleteOnlineUser(id);
                //关闭socket
                if (socket != null) {
                    try {
                        if (!socket.isClosed())
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //可以同时与多用户建立通信连接
    public void service() throws IOException {
        Socket socket = null;
        while (true) {
            //等待客户端连接，阻塞
            socket = serverSocket.accept();
            //将服务器和客户端的通信交给线程池处理
            Handler handler = new Handler(socket);
            executorService.execute(handler);
        }
    }

}
