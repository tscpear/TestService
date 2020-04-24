package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class MyVerification {
    /**
     * 验证 是否可以转化为 JSONObject
     */
    public boolean isJsonObject(String s){
        try {
            JSONObject obj = new JSONObject(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 验证是否为 JsonArray
     */


    public boolean isJSONArray(String s){
        try{
            JSONArray array = JSONArray.parseArray(s);
            return  true;
        }catch (Exception e){
            return false;
        }
    }

}
