package com.endofmaster.weibo.basic;

import com.endofmaster.weibo.WeiboResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 通过用户授权获取的用户信息
 *
 * @author YQ.Huang
 * @author ZM.Wang
 */
public class WeiboAuthUserInfo extends WeiboResponse {

    @JsonProperty("screen_name")
    private String nickName;

    @JsonProperty("gender")
    private String sex;

    @JsonProperty("avatar_large")
    private String headImgUrl;

    public String getNickName() {
        return nickName;
    }

    public WeiboAuthUserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public WeiboAuthUserInfo setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public WeiboAuthUserInfo setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }
}
