package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.service.utils.MyObject.MyJsonPath;
import com.service.utils.test.dom.DoTestData;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.text.BreakIterator;
import java.util.*;

@Service
public class MyBaseChange {

    @Autowired
    private MyVerification v;

    public JSONObject   StringToJson(String s) {
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
     * 替换json的路径值
     *
     * @param json
     * @param path
     * @param value
     * @return
     */
    public String replaceJsonPath(String json, String path, Object value) {
        DocumentContext ext = JsonPath.parse(json);
        JsonPath p = JsonPath.compile(path);

        ext.set(p, value);
        return ext.jsonString();
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

        MyJsonPath myJsonPath = new MyJsonPath();

//        try {
//            List<Object> values = JsonPath.read(json, path);
//            myJsonPath.setType(1);
//            myJsonPath.setValue(values);
//            return myJsonPath;
//        } catch (Exception e) {
//            try {
//                Boolean values = JsonPath.read(json, path);
//                myJsonPath.setType(2);
//                myJsonPath.setValue(values.toString());
//                return myJsonPath;
//            } catch (Exception e1) {
//                String values = JsonPath.read(json, path).toString();
//                myJsonPath.setType(2);
//                myJsonPath.setValue(values);
//                return myJsonPath;
//            }
//        }
        return JsonPath.read(json, path);
    }

    /**
     * Map<String,String>转JSONArray[{name,value}]
     */
    public org.json.JSONArray mapToArray(Map<String, Object> map) {
        org.json.JSONArray array = new org.json.JSONArray();
        for (String key : map.keySet()) {
            org.json.JSONObject object = new org.json.JSONObject();
            Object value = map.get(key);
            object.put("name", key);
            object.put("value", value);
            array.put(object.toString());
        }
        return array;
    }


    /**
     * 将array转化为map
     *
     * @param array
     * @return
     */
    public Map<String, Object> arrayToMap(org.json.JSONArray array) {
        Map<String, Object> map = new HashMap<>();
        for (Object o : array) {
            JSONObject object = StringToJson(o.toString());
            map.put(object.get("name").toString(), object.get("value"));
        }
        return map;
    }

    /**
     * 更换Map<String,String>替换固定值，专门给登入接口用的
     */
    public Map<String, Object> replaceValueOfMap(Map<String, Object> map, String oldValue, String newValue) {
        Map<String, Object> target = new HashMap<>(map);
        for (String key : target.keySet()) {
            if (oldValue.equals(target.get(key))) {
                target.put(key, newValue);
                break;
            }
        }

        return target;
    }

    /**
     * 空值转换
     */
    public String SToN(String s) {
        if ("null".equals(s)) {
            return null;
        }
        return s;
    }

    /**
     * 对DoTestData的对象的数据替换
     */
    public DoTestData doTestDataChange(DoTestData doTestData, Map<Integer, Map<String, Object>> key) {
        for (Integer type : key.keySet()) {
            Map<String, Object> value = key.get(type);
            switch (type) {
                //替换接口末尾参数
                case 1:
                    doTestData.setApiParam(value.get("apiParam").toString());
                    break;
                //替换header参数
                case 2:
                    org.json.JSONArray headerParam = doTestData.getHeaderParam();
                    Map<String, Object> headerParamMap = arrayToMap(headerParam);
                    for (String keyValue : value.keySet()) {
                        headerParamMap.put(keyValue, value.get(keyValue).toString());
                    }
                    headerParam = mapToArray(headerParamMap);
                    doTestData.setHeaderParam(headerParam);
                    break;
                //替换webformParam
                case 3:
                    org.json.JSONArray webformParam = doTestData.getWebformParam();
                    Map<String, Object> webformParamMap = arrayToMap(webformParam);
                    for (String keyValue : value.keySet()) {
                        webformParamMap.put(keyValue, value.get(keyValue));
                    }
                    webformParam = mapToArray(webformParamMap);
                    doTestData.setWebformParam(webformParam);
                    break;
                //替换json参数
                case 4:
                    String bodyParam = doTestData.getBodyParam();
                    for (String keyValue : value.keySet()) {
                        bodyParam = replaceJsonPath(bodyParam, keyValue, value.get(keyValue));
                    }
                    doTestData.setBodyParam(bodyParam);
                    break;
            }

        }
        return doTestData;
    }


    /**
     * String转list
     */
    public List<Integer> StringToListOfInt(String s){
        List<Integer> list = new ArrayList<>();
        JSONArray array  = this.StringToArray(s);
        for(Object a : array){
            list.add(Integer.parseInt(a.toString()));
        }
        return list;
    }

}
