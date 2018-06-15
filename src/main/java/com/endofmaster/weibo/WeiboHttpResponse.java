package com.endofmaster.weibo;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.json.JsonUtils;
import com.endofmaster.commons.util.validate.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static com.endofmaster.weibo.WeiboHttpClient.MAPPER;

/**
 * @author YQ.Huang
 * @update ZM.Wang
 */
public class WeiboHttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(WeiboHttpResponse.class);

    private int statusCode;
    private String reasonPhrase;
    private String contentType;
    private InputStream body;

    public WeiboHttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    @SuppressWarnings("unchecked")
    public <T extends WeiboResponse> T parse(Class<T> tClass) throws WeiboException {
        try {
            if (statusCode >= 200 && statusCode < 300) {
                String paramsStr = StreamUtils.copyToString(body, Charset.forName("UTF-8"));
                logger.debug("微博请求返回paramsStr：" + paramsStr);
                String json = paramsStr;
                if (!JsonUtils.isJson(paramsStr)) {
                    if (paramsStr.contains("=") && paramsStr.contains("&")) {
                        Map<String, String> params = ParamUtils.parseKvString(paramsStr);
                        json = MAPPER.writeValueAsString(params);
                    } else {
                        json = getJson(paramsStr);
                    }
                }
                T result = MAPPER.readValue(json, tClass);
                if (!result.successful()) {
                    logger.error("微博错误码：" + result.getCode() + ",错误内容：" + result.getMsg());
                    throw new WeiboServerException(result.getMsg());
                }
                return result;
            } else {
                throw new WeiboServerException("Failed to parse body, invalid status code");
            }
        } catch (IOException e) {
            throw new WeiboServerException(e);
        }
    }

    private String getJson(String str) {
        int i = str.indexOf("{");
        int j = str.lastIndexOf("}") + 1;
        return str.substring(i, j);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }

}
