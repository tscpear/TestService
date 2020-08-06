package com.service.utils;

import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.JsonPath;
import com.service.utils.MyObject.MyJsonPath;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;


@Component
public class MyVerification {
    @Autowired
    private MyBaseChange b;

    /**
     * 验证 是否可以转化为 JSONObject
     */
    public boolean isJsonObject(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 验证是否为 JsonArray
     */

    public boolean isJSONArray(String s) {
        try {
            JSONArray array = JSONArray.parseArray(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断两个JSON的结构是否一致
     */
    public boolean isSameJSONObject(String s1, String s2) {
        JSONObject o1 = new JSONObject(b.getJSONString(s1));
        JSONObject o2 = new JSONObject(b.getJSONString(s2));
        if (o1.length() != o2.length()) {
            return false;
        }
        Iterator it = o1.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            if (!o2.has(key)) {
                return false;
            }
            String value1 = o1.get(key).toString();
            String value2 = o2.get(key).toString();


            if (!StringUtils.isEmpty(value1) && value1 != "null") {

                if (this.isJsonObject(value1)) {
                    if (!this.isJsonObject(value2)) {
                        return false;
                    }
                    if (!this.isSameJSONObject(value1, value2)) {
                        return false;
                    }
                }
                if (isJSONArray(value1)) {
                    if (!this.isJSONArray(value2)) {
                        return false;
                    }
                    JSONArray array1 = JSONArray.parseArray(value1);
                    JSONArray array2 = JSONArray.parseArray(value2);
                    if (array1.size() > 0) {
                        if (array2.size() > 0) {
                            String ao1 = array1.get(0).toString();
                            String ao2 = array2.get(0).toString();
                            if (this.isJsonObject(ao1)) {
                                if (this.isJsonObject(ao2)) {
                                    if (!this.isSameJSONObject(ao1, ao2)) {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }

                    }


                }

            }


        }
        return true;
    }

    /**
     * 判断路径与值是否一致
     */
    public boolean pathAndValue(String path, String value, String json) {
        MyJsonPath jsonPath = new MyJsonPath();
        Integer type = jsonPath.getType();
        if(type == 1){




        }



        return false;
    }

}
