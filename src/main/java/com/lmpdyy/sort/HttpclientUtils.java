package com.lmpdyy.sort;
 
 
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
/**
 * @author 邓林妹
 * @date 2022/4/21
 */
public class HttpclientUtils {
 
    private static final Logger logger = LoggerFactory.getLogger(HttpclientUtils.class);
 
    /**
     * get 方式调用
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        logger.info("=====================HTTP GET 请求参数 url=" + url);
        InputStream is = null;
        String body = null;
        StringBuilder res = new StringBuilder();
        // 实例化CloseableHttpClient
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 添加URL和请求参数
            URIBuilder ub = new URIBuilder(url);
            // 使用get方法添加URL
            HttpGet get = new HttpGet(ub.build());
            // 设置请求超时时间
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).build();
            get.setConfig(config);
            //使用http调用远程，获取相应信息
            response = client.execute(get);
            // 获取响应状态码，可根据是否响应正常来判断是否需要进行下一步
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                return "请求失败 statusCode=" + statusCode;
            }
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 读取响应内容
            if (entity != null) {
                is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((body = br.readLine()) != null) {
                    res.append(body);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("=====================HTTP GET 请求响应参数 result=" + res.toString());
        return res.toString();
    }
 
    /**
     * 通过post方式调用http接口
     *
     * @param url       url路径
     * @param jsonParam json格式的参数
     * @return
     * @throws Exception
     */
    public static String post(String url, String jsonParam) {
        logger.info("=====================HTTP POST 请求参数 【url=" + url + ",jsonParam=" + jsonParam + "】");
        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        HttpClient httpClient = null;
        try {
            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();
            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            Header header = new BasicHeader("Accept-Encoding", null);
            httpPost.setHeader(header);
            // 设置报文和通讯格式
            StringEntity stringEntity = new StringEntity(jsonParam, HttpConstant.UTF8_ENCODE);
            stringEntity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            stringEntity.setContentType(HttpConstant.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            //log.info("请求{}接口的参数为{}", url, jsonParam);
            //执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            long endTime = System.currentTimeMillis();
 
            logger.info("HTTP POST 请求响应时间 httpResponse time=" + new java.util.Date(startTime - endTime));
        } catch (Exception e) {
            logger.info("http post 请求异常");
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                throw new RuntimeException("http请求释放资源异常");
            }
        }
        logger.info("=====================HTTP POST 请求响应参数 result=" + result);
        return result;
    }
}