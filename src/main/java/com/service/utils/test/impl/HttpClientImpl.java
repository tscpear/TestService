package com.service.utils.test.impl;

import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class HttpClientImpl implements HttpClientService {


    @Override
    public ResponseData getResponse(DoTestData data) {
        ResponseData responeData = new ResponseData();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String path = this.getRaw(data.getHost(), data.getApiPath(), data.getApiParam(), data.getWebformParam());
        responeData.setPath(path);
        responeData.setBodyParam(data.getBodyParam());

        try {
            if (data.getApiMethod().equals("1")) {
                responeData.setApiMethod("GET");
                HttpGet get = this.getGet(data.getAuthorization(), path, data.getHeaderParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                get.setConfig(requestConfig);
                response = httpclient.execute(get);
            } else if (data.getApiMethod().equals("2")) {
                responeData.setApiMethod("POST");
                HttpPost post = this.getPost(data.getAuthorization(), path, data.getHeaderParam(), data.getBodyParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                post.setConfig(requestConfig);
                response = httpclient.execute(post);

            } else {
                responeData.setApiMethod("PUT");
                HttpPut put = this.getPut(data.getAuthorization(), path, data.getHeaderParam(), data.getBodyParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                put.setConfig(requestConfig);
                response = httpclient.execute(put);
            }
            HttpEntity httpEntity = response.getEntity();
            responeData.setResponse(EntityUtils.toString(httpEntity, "utf-8"));
            responeData.setStatus(response.getStatusLine().getStatusCode() + "");
            responeData.setHeaderParam(httpEntity.toString());
            try {
                org.apache.http.Header[] headers = response.getHeaders("Set-Cookie");
                for (org.apache.http.Header header : headers) {
                    String cookies = header.getValue();
                    responeData.setCookie(cookies.split("JSESSIONID=")[1].split(";")[0]);
                }
            } catch (Exception e) {
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭所有资源连接
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responeData;
    }


    /**
     * 合成接口路径
     */
    public String getRaw(String host, String apiPath, String apiParam, JSONArray webformParam) {
        StringBuffer raw = new StringBuffer();
        raw.append(host).append(apiPath);
        if (!StringUtils.isEmpty(apiParam)) {
            raw.append("/").append(apiParam);
        }

        if (!StringUtils.isEmpty(webformParam)) {
            JSONArray webform = webformParam;
            StringBuffer key ;
            for (int i = 0; i < webform.length(); i++) {
                key = new StringBuffer();
                JSONObject form = new JSONObject(webform.get(i).toString());
                key.append(form.get("name").toString()).append("=").append(form.get("value").toString());
                if (i == 0) {
                    raw.append("?").append(key);
                } else {
                    raw.append("&").append(key);
                }
            }
        }
        return raw.toString();
    }


    /**
     * 发起get请求
     */
    public HttpGet getGet(String authorization, String path, JSONArray headerParam) {
        HttpGet get = new HttpGet(path);
        if (!StringUtils.isEmpty(authorization)) {
            get.setHeader("Authorization", authorization);
        }
        if (!StringUtils.isEmpty(headerParam) && headerParam.length()>0) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param);
                get.setHeader(o.get("name").toString(), o.get("value").toString());
            }
        }
        return get;
    }


    /**
     * 发起post请求
     */
    public HttpPost getPost(String authorization, String path, JSONArray headerParam, String bodyParam) {
        HttpPost post = new HttpPost(path);
        if (!StringUtils.isEmpty(authorization)) {
            post.setHeader("Authorization", authorization);
        }
        if (!StringUtils.isEmpty(headerParam)) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param);
                post.setHeader(o.get("name").toString(), o.get("value").toString());
            }
        }

        if (!StringUtils.isEmpty(bodyParam)) {
            post.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(bodyParam, "utf-8");
            post.setEntity(entity);
        }
        return post;
    }

    /**
     * 发起put请求
     */
    public HttpPut getPut(String authorization, String path, JSONArray headerParam, String bodyParam) {
        HttpPut put = new HttpPut(path);
        if (!StringUtils.isEmpty(authorization)) {
            put.setHeader("Content-Type", "application/json");
            put.setHeader("Authorization", authorization);
        }
        if (!StringUtils.isEmpty(headerParam)) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param);
                put.setHeader(o.get("name").toString(), o.get("value").toString());
            }
        }
        if (!StringUtils.isEmpty(bodyParam)) {
            StringEntity entity = new StringEntity(bodyParam, "utf-8");
            put.setEntity(entity);
        }
        return put;
    }


}
