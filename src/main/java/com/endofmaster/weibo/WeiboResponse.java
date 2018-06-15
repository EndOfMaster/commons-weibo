package com.endofmaster.weibo;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class WeiboResponse {

    private int code;
    private int ret;
    private String msg;

    public boolean successful() {
        return code == 0 || ret >= 0;
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

    public int getRet() {
        return ret;
    }

    public WeiboResponse setRet(int ret) {
        this.ret = ret;
        return this;
    }

    @Override
    public String toString() {
        return "WeiboResponse{" +
                "code=" + code +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                '}';
    }
}
