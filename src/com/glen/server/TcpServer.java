package com.glen.server;


import com.glen.common.Request;
import com.glen.common.RequestType;
import com.glen.common.Response;
import com.glen.common.ResponseStatus;

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

    public static void main(String[] args) {
        try {
            TcpServer tcpServer = new TcpServer();
            tcpServer.service();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        //输出流缓冲区
        OutputStream outputStream = socket.getOutputStream();
        return new ObjectOutputStream(outputStream);
    }

    private ObjectInputStream getOis(Socket socket) throws IOException {
        //输入流缓冲区
        InputStream inputStream = socket.getInputStream();
        return new ObjectInputStream(inputStream);
    }

    class Handler implements Runnable {
        private Socket socket;

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
                        } else {//正确登录
                            response.setStatus(ResponseStatus.OK);
                            //发回id和昵称
                            response.setData("id", id);
                            response.setData("nickname", DataBuffer.userInfo.get(id));
                        }
                        oos.writeObject(response);
                        oos.flush();
                    } else if (requestType == RequestType.CHAT) {//私聊


                    } else if (requestType == RequestType.BOARD) {//群聊

                    }

                }

            } catch (EOFException eof) {
                //直接到finall，关闭连接
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

            } finally {
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

            socket = serverSocket.accept();
            //将服务器和客户端的通信交给线程池处理
            Handler handler = new Handler(socket);
            executorService.execute(handler);
        }
    }

}
