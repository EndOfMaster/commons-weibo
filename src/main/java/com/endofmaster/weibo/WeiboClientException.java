package com.endofmaster.weibo;

/**
 * @author YQ.Huang
 */
public class WeiboClientException extends WeiboException {

    public WeiboClientException(String message) {
        super(message);
    }

    public WeiboClientException(Exception e){
        super(e);
    }

}
