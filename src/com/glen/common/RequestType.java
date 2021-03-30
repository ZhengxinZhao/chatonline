package com.glen.common;

public enum RequestType {
    //私聊
    CHAT,
    //客户端登录
    LOGIN,
    //客户端登出
    LOGOUT,
    // 群发
    FSEND,
    // 从服务器获取信息
    GET,
    //服务器主动发出的通知
    INFORM
}
