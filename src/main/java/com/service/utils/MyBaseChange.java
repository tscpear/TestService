package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Service
public class MyBaseChange {

    @Autowired
    private MyVerification v;

    public JSONObject StringToJson(String s) {
        if (StringUtils.isEmpty(s)) {
            return JSONObject.parseObject("{}");
        }
        if (v.isJsonObject(s)) {
            return JSONObject.parseObject(s);
        } else {
            return null;
        }

    }


    public JSONArray StringToArray(String s) {
        if (StringUtils.isEmpty(s)) {
            return JSONArray.parseArray("[]");
        }
        if (v.isJSONArray(s)) {
            return JSONArray.parseArray(s);
        } else {
            return null;
        }
    }

    public JSONArray StringToAO(String s) {
        JSONArray arrays = this.StringToArray(s);
        JSONArray a = new JSONArray();
        for (Object array : arrays) {
            JSONObject object = this.StringToJson(array.toString());
            a.add(object);
        }
        return a;
    }

    /**
     * 去重
     */
    public JSONArray removeSame(JSONArray a) {
        org.json.JSONArray newA = new org.json.JSONArray(a);
        List list = newA.toList();
        HashSet set = new HashSet(list);
        org.json.JSONArray s = new org.json.JSONArray(set);
        JSONArray c = JSONArray.parseArray(s.toString());
        return c;
    }

    /**
     * org.json
     */
    public org.json.JSONObject oAddO(org.json.JSONObject o1, org.json.JSONObject o2) {
        Iterator<String> names = o2.keys();
        while (names.hasNext()) {
            String name = names.next();
            o1.put(name,o2.get(name));
        }
        return o1;
    }
}
