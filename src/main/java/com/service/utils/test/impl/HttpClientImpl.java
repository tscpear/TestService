package com.service.utils.test.impl;

import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.HttpClientService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpClientImpl implements HttpClientService {

    @Autowired
    private MyBaseChange b;


    @Override
    public ResponseData getResponse(DoTestData data) {
        ResponseData responeData = new ResponseData();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String path = this.getRaw(data.getApiMethod(), data.getHost(), data.getApiPath(), data.getApiParam(), data.getWebformParam());
        responeData.setApiPath(path);
        responeData.setBodyParam(data.getBodyParam());
        responeData.setWebformParam(data.getWebformParam() + "");
        try {
            if (data.getApiMethod()==1) {
                responeData.setApiMethod("GET");
                HttpGet get = this.getGet(data.getAuthorization(), path, data.getHeaderParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                get.setConfig(requestConfig);
                response = httpclient.execute(get);
                JSONArray headerParam = new JSONArray();
                for (Header header : get.getAllHeaders()) {
                    JSONObject o = new JSONObject();
                    o.put("name", header.getName());
                    o.put("value", header.getValue());
                    headerParam.put(o);

                }
                responeData.setHeaderParam(headerParam.toString());
            } else if (data.getApiMethod()==2) {
                responeData.setApiMethod("POST");
                HttpPost post = this.getPost(data.getAuthorization(), path, data.getHeaderParam(), data.getBodyParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                post.setConfig(requestConfig);
                JSONArray webform;
                if (!StringUtils.isEmpty(data.getWebformParam()) && data.getWebformParam().length() >= 1) {
                    webform = data.getWebformParam();
                    List<NameValuePair> parame = new ArrayList<>();
                    Object a = webform.get(0);
                    for (Object forms : webform) {
                        JSONObject form = new JSONObject(forms.toString());
                        String  value = "null";
                        try {
                            value = form.get("value").toString();
                        }catch (Exception e){

                        }
                        parame.add(new BasicNameValuePair(form.get("name").toString(), b.SToN(value)));
                    }
                    HttpEntity entityParam = new UrlEncodedFormEntity(parame, "utf-8");
                    post.setEntity(entityParam);
                }
                if (!StringUtils.isEmpty(data.getBodyParam())) {
                    StringEntity entity = new StringEntity(data.getBodyParam(), "utf-8");
                    post.setEntity(entity);
                }
                response = httpclient.execute(post);
                JSONArray headerParam = new JSONArray();
                for (Header header : post.getAllHeaders()) {
                    JSONObject o = new JSONObject();
                    o.put("name", header.getName());
                    o.put("value", header.getValue());
                    headerParam.put(o);

                }
                responeData.setHeaderParam(headerParam.toString());
                try {
                    org.apache.http.Header[] headers = response.getHeaders("Set-Cookie");
                    for (org.apache.http.Header header : headers) {
                        String cookies = header.getValue();
                        responeData.setCookie(cookies.split("JSESSIONID=")[1].split(";")[0]);
                    }
                } catch (Exception e) {
                }

            } else {
                responeData.setApiMethod("PUT");
                HttpPut put = this.getPut(data.getAuthorization(), path, data.getHeaderParam(), data.getBodyParam());
                RequestConfig requestConfig = RequestConfig.custom()
                        .setSocketTimeout(2000).setConnectTimeout(2000).build();
                put.setConfig(requestConfig);


                JSONArray webform;
                if (!StringUtils.isEmpty(data.getWebformParam()) && data.getWebformParam().length() >= 1) {
                    webform = data.getWebformParam();
                    List<NameValuePair> parame = new ArrayList<>();

                    for (Object forms : webform) {
                        JSONObject form = new JSONObject(forms.toString());
                        parame.add(new BasicNameValuePair(form.get("name").toString(), b.SToN(form.get("value").toString())));
                    }
                    HttpEntity entityParam = new UrlEncodedFormEntity(parame, "utf-8");
                    put.setEntity(entityParam);
                }
                if (!StringUtils.isEmpty(data.getBodyParam())) {
                    StringEntity entity = new StringEntity(data.getBodyParam(), "utf-8");
                    put.setEntity(entity);
                }
                response = httpclient.execute(put);
                JSONArray headerParam = new JSONArray();
                for (Header header : put.getAllHeaders()) {
                    JSONObject o = new JSONObject();
                    o.put("name", header.getName());
                    o.put("value", header.getValue());
                    headerParam.put(o);

                }
                responeData.setHeaderParam(headerParam.toString());
            }
            HttpEntity httpEntity = response.getEntity();

            String responseValue =EntityUtils.toString(httpEntity, "utf-8") ;
            responeData.setResponse(responseValue);
            responeData.setStatus(response.getStatusLine().getStatusCode() + "");
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
    public String getRaw(Integer method, String host, String apiPath, String apiParam, JSONArray webformParam) {
        StringBuffer raw = new StringBuffer();
        raw.append(host).append(apiPath);
        if (!StringUtils.isEmpty(apiParam)) {
            raw.append("/").append(apiParam);
        }

        if (!StringUtils.isEmpty(webformParam) && method==1) {
            JSONArray webform = webformParam;
            StringBuffer key;
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
        if (!StringUtils.isEmpty(authorization) && !(authorization.contains("null"))) {
            if (authorization.contains("bearer") || authorization.contains("Basic")) {
                get.setHeader("Authorization", authorization);
            } else {
                get.setHeader("cookie", ("JSESSIONID=" + authorization));
                get.setHeader("Cookie2", "$Version=1");
            }
        }
        if (!StringUtils.isEmpty(headerParam) && headerParam.length() > 0) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param.toString());
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
        if (!StringUtils.isEmpty(authorization) && !(authorization.contains("null"))) {
            if (authorization.contains("bearer") || authorization.contains("Basic")) {
                post.setHeader("Authorization", authorization);
            } else {
                post.setHeader("cookie", ("JSESSIONID=" + authorization));
                post.setHeader("Cookie2", "$Version=1");
            }
        }
        if (!StringUtils.isEmpty(bodyParam)) {
            StringEntity entity = new StringEntity(bodyParam, "utf-8");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);
        }
        if (!StringUtils.isEmpty(headerParam) && headerParam.length() > 0) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param.toString());
                post.setHeader(o.get("name").toString(), o.get("value").toString());
            }
        }

        return post;
    }

    /**
     * 发起put请求
     */
    public HttpPut getPut(String authorization, String path, JSONArray headerParam, String bodyParam) {
        HttpPut put = new HttpPut(path);
        if (!StringUtils.isEmpty(authorization) && !(authorization.contains("null"))) {
            if (authorization.contains("bearer") || authorization.contains("Basic")) {
                put.setHeader("Authorization", authorization);
            } else {
                put.setHeader("cookie", ("JSESSIONID=" + authorization));
                put.setHeader("Cookie2", "$Version=1");
            }
        }
        if (!StringUtils.isEmpty(bodyParam)) {
            put.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(bodyParam, "utf-8");
            put.setEntity(entity);
        }
        if (!StringUtils.isEmpty(headerParam) && headerParam.length() > 0) {
            for (Object param : headerParam) {
                JSONObject o = new JSONObject(param.toString());
                put.setHeader(o.get("name").toString(), o.get("value").toString());
            }
        }

        return put;
    }


}
