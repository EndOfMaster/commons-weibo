package com.endofmaster.weibo.basic;


import com.endofmaster.weibo.*;

import java.io.UnsupportedEncodingException;

import static com.endofmaster.weibo.ReqDataType.FORM;

/**
 * @author ZM.Wang
 */
public class WeiboBasicApi {
    private final String appId;
    private final String appKey;
    private final String redirectUrl;
    private final WeiboHttpClient client;

    public WeiboBasicApi(String appId, String appKey, String redirectUrl, WeiboHttpClient client) {
        this.appId = appId;
        this.appKey = appKey;
        this.redirectUrl = redirectUrl;
        this.client = client;
    }

    public String getBaseAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return WeiboUtils.getBasicAuthorizeUrl(appId, redirectUrl, state);
    }

    public String getMobileAuthorizeUrl(String state) throws UnsupportedEncodingException {
        return WeiboUtils.getMobileAuthorizeUrl(appId, redirectUrl, state);
    }

    public WeiboOauth2AccessToken getOauth2AccessToken(String code) throws WeiboException {
        WeiboHttpRequest request = new WeiboHttpRequest("https://api.weibo.com/oauth2/access_token").withMethod("post").withDataType(FORM)
                .setArg("client_id", appId)
                .setArg("client_secret", appKey)
                .setArg("code", code)
                .setArg("redirect_uri", redirectUrl)
                .setArg("grant_type", "authorization_code");
        WeiboHttpResponse response = client.execute(request);
        return response.parse(WeiboOauth2AccessToken.class);
    }

    public WeiboAuthUserInfo getOauth2UserInfo(String uid, String oauth2AccessToken) throws WeiboException {
        WeiboHttpRequest request = new WeiboHttpRequest("https://api.weibo.com/2/users/show.json")
                .setArg("access_token", oauth2AccessToken)
                .setArg("uid", uid);
        WeiboHttpResponse response = client.execute(request);
        return response.parse(WeiboAuthUserInfo.class);
    }

    public String getAppId() {
        return appId;
    }

    public WeiboHttpClient getClient() {
        return client;
    }
}
