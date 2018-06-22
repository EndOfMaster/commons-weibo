package com.endofmaster.weibo;

import com.endofmaster.commons.util.StreamUtils;
import com.endofmaster.commons.util.p12cert.P12Cert;
import com.endofmaster.commons.util.p12cert.P12CertUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.endofmaster.weibo.ReqDataType.JSON;
import static com.endofmaster.weibo.ReqDataType.MULTIPART;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA;

/**
 * @author ZM.Wang
 */
public class WeiboHttpClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setVisibility(new VisibilityChecker.Std(NONE, NONE, NONE, NONE, ANY));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final HttpClient httpClient;

    public WeiboHttpClient() {
        this(null, null);
    }

    public WeiboHttpClient(final InputStream cert, final String certPassword) {
        this(3000, 3000, cert, certPassword);
    }

    public WeiboHttpClient(final int connectTimeout, final int socketTimeout, final InputStream cert, final String certPassword) {
        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .build();
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            if (cert != null) {
                SSLConnectionSocketFactory sslSocketFactory = buildSsl(cert, certPassword);
                httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
            }
            httpClient = httpClientBuilder.setDefaultRequestConfig(config).build();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new WeiboException(e);
        }
    }

    public WeiboHttpResponse execute(WeiboHttpRequest weiboHttpRequest) throws WeiboException {
        HttpUriRequest httpRequest = buildHttpUriRequest(weiboHttpRequest);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            WeiboHttpResponse weiboHttpResponse = new WeiboHttpResponse(httpResponse.getStatusLine().getStatusCode());
            weiboHttpResponse.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
            weiboHttpResponse.setBody(httpResponse.getEntity().getContent());
            return weiboHttpResponse;
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    private HttpUriRequest buildHttpUriRequest(WeiboHttpRequest weiboHttpRequest) throws WeiboException {
        RequestBuilder requestBuilder = RequestBuilder.create(weiboHttpRequest.getMethod())
                .setUri(weiboHttpRequest.getUrl());

        for (String headerKey: weiboHttpRequest.getHeaders().keySet()) {
            requestBuilder.addHeader(headerKey, weiboHttpRequest.getHeaders().get(headerKey));
        }

        if ("POST".equalsIgnoreCase(weiboHttpRequest.getMethod()) || "PUT".equalsIgnoreCase(weiboHttpRequest.getMethod())) {
            Map<String, Object> map = new HashMap<>();
            for (WeiboHttpRequest.Arg arg: weiboHttpRequest.getArgs()) {
                map.put(arg.key, arg.value);
            }
            if (JSON.equalsIgnoreCase(weiboHttpRequest.getDataType())) {
                try {
                    String json = MAPPER.writeValueAsString(map);
                    StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                    requestBuilder.setEntity(entity);
                } catch (JsonProcessingException e) {
                    throw new WeiboException(e);
                }
            } else if (MULTIPART.equalsIgnoreCase(weiboHttpRequest.getDataType())) {
                try {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    for (String key: map.keySet()) {
                        Object value = map.get(key);
                        if (value instanceof String) {
                            builder.addTextBody(key, value.toString());
                        } else {
                            byte[] data = StreamUtils.copyToByteArray((InputStream) value);
                            builder.addBinaryBody(key, data, MULTIPART_FORM_DATA, key);
                        }
                    }
                    builder.setMode(HttpMultipartMode.RFC6532);
                    requestBuilder.setEntity(builder.build());
                } catch (IOException e) {
                    throw new WeiboException(e);
                }
            } else {
                //目前最后一种就是普通form了
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    for (String key: map.keySet()) {
                        String value = map.get(key).toString();
                        nameValuePairs.add(new BasicNameValuePair(key, value));
                    }
                    HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                    requestBuilder.setEntity(httpEntity);
                } catch (UnsupportedEncodingException e) {
                    throw new WeiboException(e);
                }

            }
        } else {
            for (WeiboHttpRequest.Arg arg: weiboHttpRequest.getArgs()) {
                requestBuilder.addParameter(arg.key, arg.value.toString());
            }
        }
        return requestBuilder.build();
    }

    private SSLConnectionSocketFactory buildSsl(InputStream cert, String certPassword) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        P12Cert p12Cert = P12CertUtils.load(cert, certPassword.toCharArray());
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(p12Cert.getKeyStore(), certPassword.toCharArray()).build();
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

}
