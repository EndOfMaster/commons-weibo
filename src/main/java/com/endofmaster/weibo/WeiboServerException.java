package com.endofmaster.weibo;

/**
 * @author YQ.Huang
 */
public class WeiboServerException extends WeiboException {

    public WeiboServerException(String message) {
        super(message);
    }

    public WeiboServerException(Exception e) {
        super(e);
    }
}
