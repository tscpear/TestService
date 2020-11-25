package com.service.CreateTestData.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.CreateTestData.service.ProjectOne;
import com.service.apiTest.controller.domin.PutToken;
import com.service.apiTest.dom.entity.NewToken;
import com.service.apiTest.dom.mapper.NewTokenMapper;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.MyBaseChange;
import com.service.utils.MyObject.MyJsonPath;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.lang.model.element.TypeElement;
import java.math.BigInteger;
import java.util.*;

@Service
public class ProjectOneImpl implements ProjectOne {

    @Autowired
    private DoApiService doApiService;
    @Autowired
    private NewTokenMapper newTokenMapper;
    @Autowired
    private MyBaseChange b;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private ApiReportService apiReportService;


    private Map<String, String> newTokenList = new HashMap<>();
    private Map<String, String> newDataList = new HashMap<>();
    private List<String> accountValue = new ArrayList<>();
    //分仓当前胎号数组
    private JSONArray nowTireList = new JSONArray();

    @Override
    public void RarehouseAddTire(Integer tireId, Integer num, Integer environment, Integer projectId, boolean doTwo) throws Throwable {
        /**
         * @0登入账号
         */
        if (!doTwo) {
            accountValue.add("9.1.1");
            accountValue.add("1.1.1");
            accountValue.add("5.1.1");
            this.login(accountValue, projectId, environment);
        }
        //分仓当前数量
        Integer nowTireNum = 0;

        //当前分仓未发货的总数量
        Integer allTire = 0;
        //后台查询到的分仓未发货的数据
        JSONArray allTireList = new JSONArray();
        //获取后台查询需要的用例ID
        Integer testId3 = 0;
        //入库单id
        String rkId = "";
        //当前需要存放至货位
        Integer goodTireNum = num;
        //判断是否需要重新获取分仓库存的字段
        int doTimes = 1;
        if (doTwo) {
            doTimes = 2;
        }

        //处理轮胎id与轮胎名称的交互
        switch (tireId) {
            case 1:
                testId3 = 62;
                break;
            case 2:
                testId3 = 63;
                break;
            case 3:
                testId3 = 64;
                break;
            case 4:
                testId3 = 65;
                break;
            case 39:
                testId3 = 80;
        }
        for (int j = 0; j < doTimes; j++) {
            /**
             * @1当前分仓有多少商品数量
             */
            //修改数据
            Map<Integer, Map<String, Object>> map1 = new HashMap<>();
            Map<String, Object> map11 = new HashMap<>();
            map11.put("cdGoodsNum", tireId.toString());
            if (environment == 1) {
                map11.put("cdWarehouse", "[2432169439366169168]");
            } else {
                map11.put("cdWarehouse", "[7607186729016933137]");
            }

            map1.put(3, map11);
            DoTestData testData = doApiService.getTestData(environment, 59, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map1);
            String response = httpClientService.getResponse(testData).getResponse();
            //当前商品的分仓可用数量
            JSONArray nowTireNums = b.StringToArray(b.getValueFormJsonByPath(response, "$..canUseNum_v").toString());
            if (!StringUtils.isEmpty(nowTireNums) && nowTireNums.size() > 0) {
                nowTireNum = Integer.parseInt(nowTireNums.get(0).toString());
                //获取参数jcWarehouseId
                String jcWarehouseId = b.getValueFormJsonByPath(response, "$.rows[0].jcWarehouseId").toString();

                //获取参数 jcGoodsId
                String jcGoodsId = b.getValueFormJsonByPath(response, "$.rows[0].jcGoodsId").toString();


                /**
                 * @2获取当前该商品胎号数组
                 */
                map1.clear();
                map11.clear();
                map11.put("jcWarehouseId", jcWarehouseId);
                map11.put("jcGoodsId", jcGoodsId);
                map1.put(3, map11);
                testData = doApiService.getTestData(environment, 60, newTokenList, newDataList, 0, accountValue, projectId);
                testData = b.doTestDataChange(testData, map1);
                response = httpClientService.getResponse(testData).getResponse();
                String nowTireLists = b.getValueFormJsonByPath(response, "$..tyreNum").toString();
                //获取当前商品分仓已入库的商品
                nowTireList = b.StringToArray(nowTireLists);
            }
            if (goodTireNum >= nowTireNum) {
                goodTireNum = goodTireNum - nowTireNum;
                /**
                 * @3管理后台获取可用数量
                 */
                testData = doApiService.getTestData(environment, testId3, newTokenList, newDataList, 0, accountValue, projectId);
                response = httpClientService.getResponse(testData).getResponse();
                allTireList = b.StringToArray(b.getValueFormJsonByPath(response, "$..barcode").toString());


                /**
                 * @4创建一个入库单
                 */

                testData = doApiService.getTestData(environment, 66, newTokenList, newDataList, 0, accountValue, projectId);
                response = httpClientService.getResponse(testData).getResponse();
                rkId = b.getValueFormJsonByPath(response, "$.id").toString();
                /**
                 * 进入循环阶段
                 */
                //查询status的状态来判断该胎号是否有用
                Integer status = 0;
                for (int i = 0; i < goodTireNum; i++) {
                    String tireNo = allTireList.get(i).toString();
                    //判断胎号是否已经分仓入库
                    if (!nowTireList.contains(tireNo)) {
                        /**
                         * @5在入库单中查询该胎号是否可用
                         */
                        map1 = new HashMap<>();
                        map11 = new HashMap<>();
                        map11.put("id", rkId);
                        map11.put("tyreNum", tireNo);
                        map1.put(3, map11);
                        testData = doApiService.getTestData(environment, 67, newTokenList, newDataList, 0, accountValue, projectId);
                        testData = b.doTestDataChange(testData, map1);
                        response = httpClientService.getResponse(testData).getResponse();
                        status = Integer.parseInt(b.getValueFormJsonByPath(response, "$.status").toString());
                        if (status == 1) {
                            //获取分仓处的商品Id
                            String goodsId = b.getValueFormJsonByPath(response, "$.goodsId").toString();
                            //获取货位id
                            String storageId = b.getValueFormJsonByPath(response, "$.storageId").toString();
                            //获取detailTyreNumId
                            String detailTyreNumId = b.getValueFormJsonByPath(response, "$.id").toString();
                            /**
                             * @在入库单中将可用胎号固定到相应货位
                             */
                            map1.clear();
                            map11.clear();
                            map11.put("id", rkId);
                            map11.put("tyreNum", tireNo);
                            map11.put("goodsId", goodsId);
                            map11.put("goodsNum", storageId);
                            map11.put("storageId", storageId);
                            map11.put("detailTyreNumId", detailTyreNumId);
                            map1.put(3, map11);
                            testData = doApiService.getTestData(environment, 68, newTokenList, newDataList, 0, accountValue, projectId);
                            testData = b.doTestDataChange(testData, map1);
                            response = httpClientService.getResponse(testData).getResponse();
                            status = Integer.parseInt(b.getValueFormJsonByPath(response, "$.status").toString());
                            if (status == 0) {
                                goodTireNum++;
                            }
                        } else {
                            goodTireNum++;
                        }
                    } else {
                        goodTireNum++;
                    }
                }

                /**
                 * @提交出库单
                 */

                map1.clear();
                map11.clear();
                map11.put("id", rkId);
                map1.put(3, map11);
                testData = doApiService.getTestData(environment, 69, newTokenList, newDataList, 0, accountValue, projectId);
                testData = b.doTestDataChange(testData, map1);
                response = httpClientService.getResponse(testData).getResponse();

            } else {
                j++;
            }

        }


    }

    @Override
    public void CompleteCKOrder(String orderSn, Integer projectId, Integer environment, boolean doTwo) throws Throwable {
        /**
         * @0登入账号
         */
        if (!doTwo) {
            accountValue.add("9.1.1");
            accountValue.add("1.1.1");
            accountValue.add("5.1.1");
            this.login(accountValue, projectId, environment);
        }
        /**
         * @0.5判断订单已经同步到分仓，同时获取订单出库单ID
         */
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("cdNumbers", orderSn);
        map.put(3, maps);
        DoTestData testData = doApiService.getTestData(environment, 71, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        String response = httpClientService.getResponse(testData).getResponse();
        //查看订单是否同步
        Integer total = Integer.parseInt(b.getValueFormJsonByPath(response, "$.total").toString());
        if (total <= 0) {
            return;
        }
        //获取出库单id
        String ckId = b.getValueFormJsonByPath(response, "$.rows[0].id").toString();
        /**
         * @1判断订单已经同步到分仓，同时获取订单出库单ID
         */

        map.clear();
        maps.clear();
        maps.put("cdNumber", orderSn);
        map.put(3, maps);
        testData = doApiService.getTestData(environment, 72, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        //获取规格总类
        Integer kinds = Integer.parseInt(b.getValueFormJsonByPath(response, "$.total").toString());
        String responses = response;
        for (int k = 0; k < kinds; k++) {
            //获取商品id
            Integer goodNum = Integer.parseInt(b.getValueFormJsonByPath(responses, "$.rows[" + k + "].goodsNum").toString());
            //获取商品开单数量
            Integer billNum = Integer.parseInt(b.getValueFormJsonByPath(responses, "$.rows[" + k + "].billNum").toString());
            //获取商品出库数量
            Integer realNum = Integer.parseInt(b.getValueFormJsonByPath(responses, "$.rows[" + k + "].realNum").toString());
            //获取需要出库的数量
            Integer num = billNum - realNum;
            /**
             * @2执行分仓入库同时获取可出库胎
             */
            this.RarehouseAddTire(goodNum, num, environment, projectId, true);

            /**
             * @3获取分仓出库子订单的id
             */
            map.clear();
            maps.clear();

            maps.put("id", ckId);
            maps.put("numbers", orderSn);
            map.put(3, maps);
            testData = doApiService.getTestData(environment, 73, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map);
            response = httpClientService.getResponse(testData).getResponse();

            //获取子订单id
            String ckOrderSon = b.getValueFormJsonByPath(response, "$.datas[" + k + "].id").toString();
            /**
             * @3.5固定货位
             */
            map.clear();
            maps.clear();
            maps.put("id", ckOrderSon);
            map.put(3, maps);
            testData = doApiService.getTestData(environment, 78, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map);
            response = httpClientService.getResponse(testData).getResponse();
            /**
             * @进行将胎号导入到子订单中
             */
            for (int i = 0; i < num; i++) {
                if (nowTireList.size() <= i) {
                    break;
                }
                String tireNo = nowTireList.get(i).toString();
                map.clear();
                maps.clear();
                maps.put("id", ckOrderSon);
                maps.put("tyreNum", tireNo);
                map.put(3, maps);
                testData = doApiService.getTestData(environment, 74, newTokenList, newDataList, 0, accountValue, projectId);
                testData = b.doTestDataChange(testData, map);
                response = httpClientService.getResponse(testData).getResponse();
                Integer status = Integer.parseInt(b.getValueFormJsonByPath(response, "$.status").toString());
                if (status != 1) {
                    num++;
                }
            }

            /**
             * @4提交出库单
             */
            map.clear();
            maps.clear();
            maps.put("id", ckId);
            map.put(3, maps);
            testData = doApiService.getTestData(environment, 75, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map);
            response = httpClientService.getResponse(testData).getResponse();
        }
    }

    @Override
    public void CompleteStoreOrder(String orderSn, Integer projectId, Integer environment) throws Throwable {

        /**
         * @0登入账号
         */
        accountValue.add("9.1.1");
        accountValue.add("1.1.1");
        accountValue.add("5.1.1");
        this.login(accountValue, projectId, environment);

        /**
         * @出库单出库
         */
        this.CompleteCKOrder(orderSn, projectId, environment, true);
        /**
         * @管理后台查询订单编号获取参数
         */
        Thread.sleep(5000);
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("orderNum", orderSn);
        map.put(3, maps);
        DoTestData testData = doApiService.getTestData(environment, 76, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        String response = httpClientService.getResponse(testData).getResponse();
        //获取门店名称数据
        String storeName = b.getValueFormJsonByPath(response, "$.data.list[0].storeName").toString();
        //获取门店手机号数据
        String storePhone = b.getValueFormJsonByPath(response, "$.data.list[0].storePhone").toString();
        //获取所有胎号数据
        Object barcode = b.getValueFormJsonByPath(response, "$..barcode");
        String storeUserId = b.getValueFormJsonByPath(response, "$.data.list[0].storeUserId").toString();
        /**
         * @门店一键入库
         */
        map.clear();
        maps.clear();
        maps.put("$..storeName", storeName);
        maps.put("$..storePhone", storePhone);
        maps.put("$..barcodes", barcode);
        maps.put("$..storeUserId", storeUserId);

        map.put(4, maps);
        testData = doApiService.getTestData(environment, 79, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();

    }

    @Override
    public void CompleteDriverOrder(String orderSn, Integer projectId, Integer environment) throws Throwable {
        /**
         * @0登入账号
         */
        accountValue.add("1.1.1");
        accountValue.add("6.2.1");
        this.login(accountValue, projectId, environment);
        /**
         * @1获取司机订单信息，商品名称、门店手机号
         */
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("$.orderSn", orderSn);
        map.put(4, maps);
        DoTestData testData = doApiService.getTestData(environment, 82, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        String response = httpClientService.getResponse(testData).getResponse();
        Integer storeUserId = Integer.parseInt(b.getValueFormJsonByPath(response, "$.data.list[0].storeUserId").toString());
        Object titles = b.getValueFormJsonByPath(response, "$.data.list[0]..title").toString();
        Object quantitys = b.getValueFormJsonByPath(response, "$.data.list[0]..quantity").toString();
        JSONArray titlesArray = b.StringToArray(titles.toString());
        JSONArray quantitysArray = b.StringToArray(quantitys.toString());
        /**
         * @2查询到可出库的胎号
         */
        map.clear();
        maps.clear();
        maps.put("storeUserId", storeUserId);
        map.put(3, maps);
        testData = doApiService.getTestData(environment, 83, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        Object itemTitle = b.getValueFormJsonByPath(response, "$.data.list..itemTitle").toString();
        Object serialNums = b.getValueFormJsonByPath(response, "$.data.list..serialNums").toString();
        JSONArray itemTitleArray = b.StringToArray(itemTitle.toString());
        JSONArray serialNumsArray = b.StringToArray(serialNums.toString());


        /**
         * @2.9获取barcodes值
         */
        List<List<String>> bbs = new ArrayList<>();
        List<String> barcodes = new ArrayList<>();
        for (int i = 0; i < titlesArray.size(); i++) {
            for (int j = 0; j < itemTitleArray.size(); j++) {
                if (titlesArray.get(i).equals(itemTitleArray.get(j))) {
                    List<String> bs = new ArrayList<>();
                    Integer quantity = Integer.parseInt(quantitysArray.get(i).toString());
                    List<String> serialNum = Arrays.asList(serialNumsArray.get(j).toString().split(","));
                    for (int k = 0; k < quantity; k++) {
                        barcodes.add(serialNum.get(k).split("\\(")[0]);
                        bs.add(serialNum.get(k).split("\\(")[0]);

                    }
                    bbs.add(bs);
                    break;
                }
            }
        }

        /**
         * @3门店一键出库
         */
        map.clear();
        maps.clear();
        maps.put("storeUserId", storeUserId);
        maps.put("barcodes", barcodes);
        map.put(4, maps);
        testData = doApiService.getTestData(environment, 84, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        /**
         * @4获取出库单号
         */
        map.clear();
        maps.clear();
        maps.put("storeUserId", storeUserId);
        map.put(3, maps);
        testData = doApiService.getTestData(environment, 85, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        String outSn = b.getValueFormJsonByPath(response, "$.data.list[0].sn").toString();


        /**
         * @5获取出库单中的轮胎list信息
         */
        map.clear();
        maps.clear();
        maps.put("sn", "https://zhilun.co/a/" + outSn);
        map.put(3, maps);
        testData = doApiService.getTestData(environment, 86, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        String tyreInfoList = b.getValueFormJsonByPath(response, "$.data.tyreInfoList").toString();
        JSONArray tyreInfoListArray = b.StringToAO(tyreInfoList);
        /**
         * @6唯一司机端领取质保卡
         */
        map.clear();
        maps.clear();
        maps.put("$.tyreInfoList", tyreInfoListArray);
        map.put(4, maps);
        testData = doApiService.getTestData(environment, 87, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        httpClientService.getResponse(testData).getResponse();

        /**
         * @7管理后台出库关联
         */

        for (int i = 0; i < bbs.size(); i++) {
            List<String> bs = bbs.get(i);
            map.clear();
            maps.clear();
            maps.put("$.serialNums", bs.toString().split("\\[")[1].split("]")[0].replaceAll(",", ";").replaceAll(" ", ""));
            maps.put("$.orderSn", orderSn);
            map.put(4, maps);
            testData = doApiService.getTestData(environment, 89, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map);
            httpClientService.getResponse(testData).getResponse();
        }


    }


    /**
     * 完成理赔单，并获取理赔抵扣券
     */
    @Override
    public String getVoucher(String phone, Integer projectId, Integer environment, Integer type) throws Throwable {
        /**
         * @0登入账号
         */
        accountValue.add("3.1.1");
        accountValue.add("1.1.1");
        accountValue.add("2.1.1");
        this.login(accountValue, projectId, environment);


        /**
         * @1获取司机账号对应存在的质保卡编号
         */
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("driverPhone", phone);
        map.put(3, maps);
        DoTestData testData = doApiService.getTestData(environment, 91, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        String response = httpClientService.getResponse(testData).getResponse();
        String zsn = "";
        try {
            zsn = b.getValueFormJsonByPath(response, "$.data.list[0].sn").toString();
        } catch (Exception e) {
            new Throwable("该司机端没有线上质保卡");
            return null;
        }
        String carHeadPhoto = "";
        try{
            carHeadPhoto = b.getValueFormJsonByPath(response, "$.data.list[0].carHeadPhoto").toString();
        }catch (Exception e){

        }

        String carLicense = b.getValueFormJsonByPath(response, "$.data.list[0].carLicense").toString();
        String driverName = b.getValueFormJsonByPath(response, "$.data.list[0].driverName").toString();

        /**
         * @2更新为全国理赔
         */
        map.clear();
        maps.clear();
        if(carHeadPhoto.equals("")){

        }else {
            maps.put("$.carHeadPhoto", carHeadPhoto);
        }

        maps.put("$.driverName", driverName);
        maps.put("$.driverPhone", phone);
        maps.put("$.sn", zsn);
        maps.put("$.nationalClaims", true);
        maps.put("$.carLicense", carLicense);
        map.put(4, maps);
        testData = doApiService.getTestData(environment, 92, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        httpClientService.getResponse(testData).getResponse();

        /**
         * @3门店发起理赔
         */
        map.clear();
        maps.clear();
        maps.put("$.sn", zsn);
        map.put(4, maps);
        testData = doApiService.getTestData(environment, 90, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        String lpsn = b.getValueFormJsonByPath(response, "$.data.sn").toString();

        if (type == -1 || type == -2) {
            return lpsn;
        }
        this.lpsh(lpsn, type, environment, projectId);
        return null;

    }

    @Override
    public void lpsh(String lpsn, Integer environment, Integer type, Integer projectId) throws Throwable {

        /**
         * @4我来处理
         */
        String response = "";
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("apiParam", lpsn);
        map.put(1, maps);
        DoTestData testData = doApiService.getTestData(environment, 94, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        httpClientService.getResponse(testData).getResponse();

        /**
         * @5获取理赔单详情信息
         */
        map.clear();
        maps.clear();
        maps.put("apiParam", lpsn);
        map.put(1, maps);
        testData = doApiService.getTestData(environment, 95, newTokenList, newDataList, 0, accountValue, projectId);
        testData = b.doTestDataChange(testData, map);
        response = httpClientService.getResponse(testData).getResponse();
        try {
            Integer lpid = Integer.parseInt(b.getValueFormJsonByPath(response, "$.data.id").toString());
            /**
             * @6审核
             */
            boolean isThree = false;
            if (type == 1 || type == 7) {
                isThree = true;
            }
            map.clear();
            maps.clear();
            maps.put("$.id", lpid);
            maps.put("$.isThreeguranteesDefects", isThree);
            map.put(4, maps);
            testData = doApiService.getTestData(environment, 96, newTokenList, newDataList, 0, accountValue, projectId);
            testData = b.doTestDataChange(testData, map);
            httpClientService.getResponse(testData).getResponse();
        } catch (Exception e){

        }finally {
            /**
             * 作废理赔单
             */
            if (type == 9) {
                map.clear();
                maps.clear();
                maps.put("$.claimsSn", lpsn);
                map.put(4, maps);
                testData = doApiService.getTestData(environment, 97, newTokenList, newDataList, 0, accountValue, projectId);
                testData = b.doTestDataChange(testData, map);
                httpClientService.getResponse(testData).getResponse();
            }
        }


    }


    /**
     * 登入认证
     */
    public void login(List<String> accountValue, Integer projectId, Integer environment) {
        PutToken putToken = new PutToken();
        putToken.setEnvironment(environment);
        putToken.setAccountValue(accountValue);
        apiReportService.accountLogin(putToken, projectId);
        for (String accounts : accountValue) {
            NewToken newToken = new NewToken();
            Integer deviceId = Integer.parseInt(accounts.split("\\.")[0]);
            Integer deviceType = Integer.parseInt(accounts.split("\\.")[1]);
            Integer acountId = Integer.parseInt(accounts.split("\\.")[2]);
            newToken.setEnvironment(environment);
            newToken.setDeviceId(deviceId);
            newToken.setDeviceType(deviceType);
            newToken.setProjectId(projectId);
            newToken.setAccountId(acountId);
            String newName = deviceId + "." + deviceType;
            String tokenValue = newTokenMapper.getToken(newToken);
            if (StringUtils.isEmpty(tokenValue)) {
                tokenValue = newTokenMapper.getCookie(newToken);
            }
            String data = newTokenMapper.getData(newToken);
            newTokenList.put(newName, tokenValue);
            newDataList.put(newName, data);
        }
    }

}


