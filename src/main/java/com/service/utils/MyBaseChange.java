package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.text.BreakIterator;
import java.util.*;

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
     * 去重
     */
    public List removeSame(List a) {

        HashSet h = new HashSet(a);
        a.clear();
        a.addAll(h);
        return a;
    }


    /**
     * org.json
     */
    public org.json.JSONObject oAddO(org.json.JSONObject o1, org.json.JSONObject o2) {
        Iterator<String> names = o2.keys();
        while (names.hasNext()) {
            String name = names.next();
            o1.put(name, o2.get(name));
        }
        return o1;
    }

    /**
     * String 去 /n/t
     */
    public String getJSONString(String s) {
        if (s.contains("\n")) {
            s = s.replaceAll("\n", "");
        }
        if (s.contains("\t")) {
            s = s.replaceAll("\t", "");
        }
        return s;
    }

    /**
     * JSONObject 转换成 JSONArray
     *
     * @param object
     * @param name
     * @param value
     * @return
     */
    public org.json.JSONArray oToA(org.json.JSONObject object, String name, String value) {
        org.json.JSONArray array = new org.json.JSONArray();
        Iterator<String> it = object.keys();
        while (it.hasNext()) {
            String key = it.next();
            org.json.JSONObject o = new org.json.JSONObject();
            o.put(name, key);
            o.put(value, object.get(key));
            array.put(o);
        }
        return array;
    }


    /**
     * 取“/”
     */
    public String removetest(String s) {

        s = s.replaceAll("\\\\", "");

        return s;
    }


    /**
     * 获取路径值
     */
    public Object getValueFormJsonByPath(String json, String path) {
        try {
            List<Object> values = JsonPath.read(json, path);
            return values.get(0);
        } catch (Exception e) {
            try {
                Boolean values = JsonPath.read(json, path);
                return values.toString();
            } catch (Exception e1) {
                return JsonPath.read(json, path).toString();

            }

        }
    }

    /**
     * Map<String,String>转JSONArray[{name,value}]
     */
    public org.json.JSONArray mapToArray(Map<String, String> map) {
        org.json.JSONArray array = new org.json.JSONArray();
        for(String key:map.keySet()){
            org.json.JSONObject object = new org.json.JSONObject();
            String value = map.get(key);
            object.put("name",key);
            object.put("value",value);
            array.put(object.toString());
        }
        return array;
    }
    /**
     * 更换Map<String,String>替换固定值，专门给登入接口用的
     */
    public Map<String,String> replaceValueOfMap(Map<String,String> map,String oldValue,String newValue){
        Map<String,String> target = new HashMap<>(map);
        for(String key:target.keySet()){
            if(oldValue.equals(target.get(key))){
                target.put(key,newValue);
                break;
            }
        }

        return target;
    }

}
