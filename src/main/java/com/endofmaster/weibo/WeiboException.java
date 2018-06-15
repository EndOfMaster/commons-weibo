package com.endofmaster.weibo;

/**
 * @author ZM.Wang
 */
public class WeiboException extends RuntimeException {

    public WeiboException(String errorMsg) {
        super(errorMsg);
    }

    public WeiboException(Throwable e) {
        super(e);
    }
}
