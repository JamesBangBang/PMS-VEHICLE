package com.starnetsecurity.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2016-01-05.
 */
public class HttpRequestUtils {


    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);
    private static final String ENCODING = "UTF-8";
    public static final String aesKey = "75e82e251b7896be654f4080668b8e06";

    /**
     * 普通get请求
     *
     * @param targetUrl 目标链接地址
     * @param params    参数
     * @return
     */
    public static String httpRequests(String targetUrl, String... params) {
        String tempStr = null;
        HttpURLConnection url_con = null;
        try {
            URL url = new URL(targetUrl);
            StringBuffer bankXmlBuffer = new StringBuffer();
            //创建URL连接，提交到数据，获取返回结果
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "micromessenger");
            connection.setRequestProperty("devices", "iphone");
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            if (params != null && params.length > 0) {
                for (String p : params) {
                    out.println(p);
                }
            }
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                bankXmlBuffer.append(inputLine);
            }
            in.close();
            tempStr = bankXmlBuffer.toString();
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！", e);
        } finally {
            if (url_con != null)
                url_con.disconnect();
        }
        return tempStr;
    }

    public static String httpGetRequests(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String post(String url, Map<String, String> paramsMap) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);

            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList,ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("post request occur error:", ex);
            throw ex;
        } finally {
            try {
                client.close();
                response.close();
            } catch (Exception e) {
                logger.error("close request response occur error:", e);
            }
        }
        return responseText;
    }

    public static String aesPost(String url, Map<String, String> paramsMap) throws IOException {

        AesCipherService aesCipherService = new AesCipherService();
        Map<String,String> params = new HashedMap();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if(!StringUtils.isBlank(entry.getValue())){
                params.put(entry.getKey(),aesCipherService.encrypt(entry.getValue().getBytes(),Hex.decode(aesKey)).toHex());
            }
        }

        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);

            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList,ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("post request occur error:", ex);
            throw ex;
        } finally {
            try {
                client.close();
                response.close();
            } catch (Exception e) {
                logger.error("close request response occur error:", e);
            }
        }
        return responseText;
    }

    public static String postJson(String url, JSONObject params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(params.toString(),"UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            method.setEntity(stringEntity);
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("post request occur error:", ex);
            throw ex;
        } finally {
            try {
                client.close();
                response.close();
            } catch (Exception e) {
                logger.error("close request response occur error:", e);
            }
        }
        return responseText;
    }

    public static String postModelJson(String url, ModelMap params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(params.toString(),"UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            method.setEntity(stringEntity);
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("post request occur error:", ex);
            throw ex;
        } finally {
            try {
                client.close();
                response.close();
            } catch (Exception e) {
                logger.error("close request response occur error:", e);
            }
        }
        return responseText;
    }

    public static String aesPostJson(String url, JSONObject params) throws IOException {
        AesCipherService aesCipherService = new AesCipherService();
        String paramsStr = aesCipherService.encrypt(JSON.toJSONString(params, SerializerFeature.WriteMapNullValue).getBytes(),Hex.decode(aesKey)).toHex();


        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(paramsStr,"UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            method.setEntity(stringEntity);
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("post request occur error:", ex);
            throw ex;
        } finally {
            try {
                client.close();
                response.close();
            } catch (Exception e) {
                logger.error("close request response occur error:", e);
            }
        }
        return responseText;
    }

    public static String postFormData(String url, MultipartFile multipartFile) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String responseText = null;
        try {

            HttpPost httppost = new HttpPost(url);
            String fileName = multipartFile.getOriginalFilename();
            String[] suffix = fileName.split("\\.");
            String fileType;
            if(suffix.length > 1){
                fileType = suffix[suffix.length - 1];
            }else{
                fileType = "tmp";
            }
            ByteArrayBody byteArrayBody = new ByteArrayBody(multipartFile.getBytes(),fileType);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", byteArrayBody)
                    .build();
            httppost.setEntity(reqEntity);
            response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseText = EntityUtils.toString(resEntity);
            }
        } finally {
            response.close();
            httpclient.close();
        }
        return responseText;
    }

    public static void main(String[] argus){
        AesCipherService aesCipherService = new AesCipherService();

        String encodeKeyStr = "75e82e251b7896be654f4080668b8e06";

        String text = "123456789";
        System.out.println(text);

        text = aesCipherService.encrypt(text.getBytes(),Hex.decode(encodeKeyStr)).toHex();

        System.out.println(text);

        text = new String(aesCipherService.decrypt(Hex.decode(text),Hex.decode(encodeKeyStr)).getBytes());

        System.out.println(text);

    }

}
