package com.service.apiTest.service.impl;

import com.service.apiTest.dom.entity.NewToken;
import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.dom.mapper.NewTokenMapper;
import com.service.apiTest.dom.mapper.TokenMapper;
import com.service.apiTest.service.service.ApiReportService;
import com.service.apiTest.service.service.CreateTireDataService;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateTireDataServiceImpl implements CreateTireDataService {

    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private DoApiService doApiService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private MyBaseChange b;
    @Autowired
    private ApiReportService apiReportService;
    @Autowired
    private NewTokenMapper newTokenMapper;


    @Override
    public void getTireTestData(Integer environment, String orderSn) throws Throwable {
        NewToken newToken = new NewToken();
        newToken.setProjectId(1);
        newToken.setDeviceType(1);
        newToken.setAccountId(1);
        newToken.setEnvironment(environment);
        this.test13(environment);
        Token token = tokenMapper.getData();
        String oderId = this.test1(environment, orderSn, token);
        JSONObject outData = this.test2(environment, token, oderId, orderSn);
        JSONArray outDataList = new JSONArray(outData.get("datas").toString());
        String outOrderSn = outData.get("id").toString();
        for (Object datas : outDataList) {
            JSONObject data = new JSONObject(datas.toString());
            Integer goodsNum = Integer.parseInt(data.get("goodsNum").toString());
            Integer realNum = Integer.parseInt(data.get("realNum").toString());
            Integer billNum = Integer.parseInt(data.get("billNum").toString());
            String outOrderSnSmall = data.get("id").toString();
            Integer goodsOfNum = billNum - realNum;
            Integer testId;
            if (goodsNum == 1) {
                testId = 44;
            } else if (goodsNum == 2) {
                testId = 45;
            } else if (goodsNum == 3) {
                testId = 46;
            } else {
                testId = 47;
            }
            List<String> barcodeList = this.test3(testId, environment, token, goodsOfNum);
            for (int i = 0; i < goodsOfNum; i++) {
                String intoOrderSn = this.test4(environment, token);
                JSONObject barcodeData = this.test5(environment, token, intoOrderSn, barcodeList.get(i));
                if (barcodeData.get("status").toString().equals("1")) {
                    Boolean submit = this.test6(environment, token, barcodeData, intoOrderSn);
                    if (!submit) {
                        goodsOfNum++;
                        continue;
                    }
                } else {
                    if (!barcodeData.get("statusMsg").equals("该条码已有入库记录，不可执行此入库操作")) {
                        goodsOfNum++;
                        continue;
                    }
                }


                this.test7(environment, token, intoOrderSn);
                this.test8(environment, token, outOrderSnSmall);
                this.test9(environment, token, barcodeList.get(i), outOrderSnSmall);
            }
            this.test10(environment, token, outOrderSn);

            JSONArray tireArray = this.test11(environment, token, orderSn);
            this.test12(environment, token, tireArray);

        }
    }


    public JSONObject doTest(Integer environment, Integer testId, Token token) {
        DoTestData doTestData = doApiService.getTestData(environment, testId, null, null, 0, null, 1);
        ResponseData responseData = httpClientService.getResponse(doTestData);
        return new JSONObject(responseData.getResponse());

    }

    /**
     * 进入出库单列表，获取id
     */
    public String test1(Integer environment, String orderSn, Token token) throws Throwable {
        JSONArray array = new JSONArray(this.doTest(environment, 29, null).get("datas").toString());
        String id;
        for (Object a : array) {
            JSONObject object = new JSONObject(a.toString());
            if (object.get("numbers").toString().equals(orderSn)) {
                return object.get("id").toString();
            }
        }
        throw new Throwable("订单没有同步");
    }

    /**
     * 获取出库单详情
     *
     * @param environment
     * @param token
     * @param oderId
     * @param orderSn
     */
    public JSONObject test2(Integer environment, Token token, String oderId, String orderSn) {
        System.err.println("test2");
        DoTestData doTestData = doApiService.getTestData(environment, 33, null, null, 0, null, 0);
        JSONArray array = new JSONArray();
        JSONObject id = new JSONObject();
        id.put("name", "id");
        id.put("value", oderId);
        JSONObject orderNum = new JSONObject();
        orderNum.put("name", "numbers");
        orderNum.put("value", orderSn);
        array.put(id);
        array.put(orderNum);
        doTestData.setWebformParam(array);
        ResponseData responseData = httpClientService.getResponse(doTestData);
        JSONObject object = new JSONObject(responseData.getResponse());
        return object;
    }

    /**
     * 获取过量的可入的胎号
     *
     * @param testId
     * @param environment
     * @param token
     * @param goodsOfNum
     * @return
     */
    public List<String> test3(Integer testId, Integer environment, Token token, Integer goodsOfNum) {
        System.err.println("test3");
        JSONObject data1 = new JSONObject(this.doTest(environment, testId, null).get("data").toString());
        JSONArray list = new JSONArray(data1.get("list").toString());
        List<String> barcodeList = new ArrayList<>();
        for (int i = 0; i < goodsOfNum + 10; i++) {
            JSONObject data2 = new JSONObject(list.get(i).toString());
            barcodeList.add(data2.get("barcode").toString());
        }
        return barcodeList;
    }

    /**
     * 创建一个入库单号
     *
     * @param environment
     * @param token
     * @return
     */
    public String test4(Integer environment, Token token) {
        System.err.println("test4");
        JSONObject data = this.doTest(environment, 48, token);
        return data.get("id").toString();
    }

    /**
     * 获取一个胎号的信息
     *
     * @param environment
     * @param token
     * @param intoOrderSn
     * @param barcode
     */
    public JSONObject test5(Integer environment, Token token, String intoOrderSn, String barcode) {
        System.err.println("test5");
        DoTestData doTestData = doApiService.getTestData(environment, 49, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", intoOrderSn);
        webformParam.put("tyreNum", barcode);
        webformParam.put("writeType", 1);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        return new JSONObject(httpClientService.getResponse(doTestData).getResponse());
    }

    /**
     * 确定这个胎号入库
     *
     * @param environment
     * @param token
     * @param barcodeData
     * @param intoOrderSn
     */
    public boolean test6(Integer environment, Token token, JSONObject barcodeData, String intoOrderSn) {
        System.err.println("test6");
        DoTestData doTestData = doApiService.getTestData(environment, 50, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", intoOrderSn);
        webformParam.put("detailTyreNumId", barcodeData.get("id"));
        if (environment.equals("uat")) {
            webformParam.put("storageId", "791457896385233765");
        } else {
            webformParam.put("storageId", "6090182775123692228");
        }
        webformParam.put("goodsId", barcodeData.get("goodsId"));
        webformParam.put("goodsNum", barcodeData.get("goodsNum"));
        webformParam.put("tyreNum", barcodeData.get("tyreNum"));
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        JSONObject data = new JSONObject(httpClientService.getResponse(doTestData).getResponse());
        System.out.println(data);
        if (data.get("status").toString().equals("1")) {
            return true;
        } else {
            if (data.get("statusMsg").toString().equals("该条码已经被指定货位。")) {
                return true;
            } else {
                return false;
            }

        }
    }

    /**
     * 提交入库单
     *
     * @param environment
     * @param token
     * @param intoOrderSn
     */
    public void test7(Integer environment, Token token, String intoOrderSn) {
        System.err.println("test7");
        DoTestData doTestData = doApiService.getTestData(environment, 51, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", intoOrderSn);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        ResponseData data = httpClientService.getResponse(doTestData);
        System.out.println(data);
    }

    /**
     * 查看出库单订单详情
     *
     * @param environment
     * @param token
     * @param outOrderSn
     */
    public void test8(Integer environment, Token token, String outOrderSn) {
        System.err.println("test8");
        DoTestData doTestData = doApiService.getTestData(environment, 43, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", outOrderSn);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        ResponseData data = httpClientService.getResponse(doTestData);
        System.out.println(data);
    }

    /**
     * 对出库单添加一个胎号
     *
     * @param environment
     * @param token
     * @param barcode
     * @param outOrderSnSmall
     */
    public void test9(Integer environment, Token token, String barcode, String outOrderSnSmall) {
        System.err.println("test9");
        DoTestData doTestData = doApiService.getTestData(environment, 52, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", outOrderSnSmall);
        webformParam.put("tyreNum", barcode);
        webformParam.put("writeType", 1);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        ResponseData data = httpClientService.getResponse(doTestData);
        System.out.println(data);
    }

    /**
     * 提交出库单
     *
     * @param environment
     * @param token
     * @param outOrderSn
     */
    public void test10(Integer environment, Token token, String outOrderSn) throws InterruptedException {
        System.err.println("test10");
        DoTestData doTestData = doApiService.getTestData(environment, 53, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("id", outOrderSn);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        ResponseData data = httpClientService.getResponse(doTestData);
        System.out.println(data);
        Thread.sleep(5000);

    }

    /**
     * 查询对应订单号的分仓未出库的胎号
     *
     * @param environment
     * @param token
     * @param oderSn
     * @return
     */
    public JSONArray test11(Integer environment, Token token, String oderSn) throws InterruptedException {
        System.err.println("test11");
        System.err.println();
        DoTestData doTestData = doApiService.getTestData(environment, 54, null, null, 1, null, 1);
        JSONObject webformParam = new JSONObject();
        webformParam.put("pageNum", 1);
        webformParam.put("pageSize", 60);
        webformParam.put("status", "INIT");
        webformParam.put("orderNum", oderSn);
        doTestData.setWebformParam(b.oToA(webformParam, "name", "value"));
        JSONObject data = new JSONObject(httpClientService.getResponse(doTestData).getResponse());
        JSONObject datas = new JSONObject(data.get("data").toString());
        return new JSONArray(datas.get("list").toString());
    }

    /**
     * 门店一键出库
     *
     * @param environment
     * @param token
     * @param array
     */
    public void test12(Integer environment, Token token, JSONArray array) {
        System.err.println("test12");
        DoTestData doTestData = doApiService.getTestData(environment, 55, null, null, 1, null, 1);
        JSONObject param = new JSONObject();
        String storePhone = "";
        Integer storeUserId = 0;
        if (array.length() > 0) {
            JSONObject o = new JSONObject(array.get(0).toString());
            storePhone = o.get("storePhone").toString();
            storeUserId = Integer.parseInt(o.get("storeUserId").toString());
        }
        JSONArray array1 = new JSONArray();
        for (Object o : array) {
            JSONObject s = new JSONObject(o.toString());
            array1.put(s.get("barcode").toString());
        }
        param.put("barcodes", array1);
        param.put("storeName", "-");
        param.put("storePhone", storePhone);
        param.put("storeUserId", storeUserId);
        doTestData.setBodyParam(param.toString());
        ResponseData data = httpClientService.getResponse(doTestData);
        System.out.println(data);

    }

    public void test13(Integer environment) throws Throwable {
        com.alibaba.fastjson.JSONArray array = new com.alibaba.fastjson.JSONArray();
        array.add(55);
        array.add(49);
        apiReportService.putToken(array, environment);
    }

}


