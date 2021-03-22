package com.success.rpc.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/3/23 7:14
 * @Description
 * @Version
 */
public class HttpClientUtils {

    /**
     * 连接超时时间
     */
    private static final int connTimeout = 60 * 1000;
    /**
     * 等待响应时间
     */
    private static final int socketTimeout = 60 * 1000;

    private static CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();



    /**
     * 配置
     * @param httpRequestBase
     */
    private static void config(HttpRequestBase httpRequestBase) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connTimeout)
                .setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout).build();
        httpRequestBase.setConfig(requestConfig);
    }



    /**
     * post 表单格式数据
     * @param url
     * @param paramMap 键值对
     * @param headerMap 头部数据
     * @param chartset 字符集
     * @return java.lang.String
     */
    public static String postFormData(String url, Map<String, String> paramMap, Map<String, String> headerMap, String chartset) throws Exception{
        String result = "";
        CloseableHttpResponse resp = null;
        try{
            if(StringUtils.isBlank(chartset)){
                chartset = "utf-8";
            }
            CloseableHttpClient client = getHttpClient(url);
            HttpPost httpPost = new HttpPost(url);
            config(httpPost);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
            if(headerMap!=null && headerMap.size()>0){
                Iterator<String> iter = headerMap.keySet().iterator();
                while (iter.hasNext()){
                    String key = iter.next();
                    String value = headerMap.get(key);
                    httpPost.addHeader(key, value);
                }
            }
            List<NameValuePair> paramList = new ArrayList<>();
            if(paramMap!=null && paramMap.size()>0){
                Iterator<String> iter = paramMap.keySet().iterator();
                while (iter.hasNext()){
                    String key = iter.next();
                    String value = paramMap.get(key);
                    paramList.add(new BasicNameValuePair(key, value));
                }
            }
            HttpEntity entity = new UrlEncodedFormEntity(paramList, chartset);
            httpPost.setEntity(entity);
            resp = client.execute(httpPost);
            if(resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity he = resp.getEntity();
                result = EntityUtils.toString(he, chartset);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            try {
                if(resp!=null) {
                    resp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取HttpClient对象
     * @param
     * @return org.apache.http.impl.client.CloseableHttpClient
     */
    private static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.startsWith("http")?url.split("/")[2]:url.split("/")[0];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(200, 40, 100, hostname, port);
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     * @param
     * @return org.apache.http.impl.client.CloseableHttpClient
     */
    private static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            /**
             * 是否重试
             * @param exception
             * @param executionCount
             * @param context
             * @return
             */
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();
        return httpClient;
    }


}
