package com.endofmaster.weibo.basic;

import com.endofmaster.weibo.WeiboResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YQ.Huang
 */
public class WeiboOauth2AccessToken extends WeiboResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    private long uid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getUid() {
        return uid;
    }

    public WeiboOauth2AccessToken setUid(long uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public String toString() {
        return "WeiboOauth2AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", uid='" + uid + '\'' +
                '}';
    }
}
