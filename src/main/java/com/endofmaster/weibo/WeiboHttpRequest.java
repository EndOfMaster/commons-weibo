package com.endofmaster.weibo;

import java.util.*;

import static com.endofmaster.weibo.ReqDataType.JSON;

/**
 * @author YQ.Huang
 */
public class WeiboHttpRequest {

    private final List<Arg> args = new ArrayList<>();
    private final Map<String, String> headers = new LinkedHashMap<>();

    private String method = "GET";
    private String dataType = JSON;
    private String url;

    public WeiboHttpRequest(String url) {
        this.url = url;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public WeiboHttpRequest withHeader(String name, String value) {
        setHeader(name, value);
        return this;
    }

    public WeiboHttpRequest withArg(String key, Object value) {
        if (value != null) {
            args.add(new Arg(key, value));
        }
        return this;
    }

    /**
     * Unlike {@link #withArg(String, Object)}, overrides the existing value
     */
    public WeiboHttpRequest setArg(String key, Object value) {
        for (Arg e: args) {
            if (e.key.equals(key)) {
                e.value = value;
                return this;
            }
        }
        return withArg(key, value);
    }

    public WeiboHttpRequest withMethod(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public WeiboHttpRequest withDataType(String dataType) {
        this.dataType = dataType.toUpperCase();
        return this;
    }

    public List<Arg> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public static class Arg {
        String key;
        Object value;

        private Arg(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
