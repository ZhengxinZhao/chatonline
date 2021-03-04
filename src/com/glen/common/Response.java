package com.glen.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    //响应状态
    private ResponseStatus status;
    // 响应的请求类型
    private RequestType type;
    //返回数据域
    private Map<String, Object> dataMap;

    public Response(){
        this.status = ResponseStatus.OK;
        this.dataMap = new HashMap<String, Object>();
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Object getData(String dataName){
        return this.dataMap.get(dataName);
    }

    public void setData(String dataName, Object data){
        this.dataMap.put(dataName, data);
    }


}
