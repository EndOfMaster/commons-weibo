package com.endofmaster.weibo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ZM.Wang
 */
public class WeiboUtils {

    public static String getBasicAuthorizeUrl(String appId, String redirectUrl, String state) throws UnsupportedEncodingException {
        return "https://api.weibo.com/oauth2/authorize?client_id=" + appId
                + "&redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8")
                + "&state=" + URLEncoder.encode(state, "UTF-8");
    }

    public static String getMobileAuthorizeUrl(String appId, String redirectUrl, String state) throws UnsupportedEncodingException {
        return "https://open.weibo.cn/oauth2/authorize?client_id=" + appId + "&display=mobile"
                + "&redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8")
                + "&state=" + URLEncoder.encode(state, "UTF-8");
    }
}