package com.endofmaster.weibo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class WeiboResponse {

    @JsonProperty("error_code")
    private int code;
    @JsonProperty("error")
    private String msg;

    public boolean successful() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public WeiboResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public WeiboResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }


    @Override
    public String toString() {
        return "WeiboResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
