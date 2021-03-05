package com.glen.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    // 请求传送的数据类型
    private RequestType requestType;
    //请求域中的数据,name-value
    private Map<String, Object> dataMap;

    public Request(){
        this.dataMap = new HashMap<>();
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    public void setRequestType(RequestType type) {
        this.requestType = type;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

}
