package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class MyBaseChange {

    @Autowired
    private MyVerification v;

    public JSONObject StringToJson(String s) {
        if(StringUtils.isEmpty(s)){
            return JSONObject.parseObject("{}");
        }
        if(v.isJsonObject(s)){
            return  JSONObject.parseObject(s);
        }else{
            return null;
        }

    }


    public JSONArray StringToArray(String s) {
        if(StringUtils.isEmpty(s)){
            return  JSONArray.parseArray("[]");
        }
        if(v.isJSONArray(s)){
            return  JSONArray.parseArray(s);
        }else{
            return null;
        }
    }

    public JSONArray StringToAO(String s) {
        JSONArray arrays =  this.StringToArray(s);
        JSONArray a = new JSONArray();
        for(Object array : arrays){
            JSONObject object = this.StringToJson(array.toString());
            a.add(object);
        }
        return a;
    }

}
