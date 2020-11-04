package com.service.CreateTestData.service;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProjectOne {
    /**
     * 分仓收入胎号
     */
    void RarehouseAddTire(Integer tireId, Integer num, Integer environment,  Integer projectId,boolean doTwo);

    /**
     * 完成出库单
     * @param orderSn
     * @param projectId
     * @param environment
     */
    void CompleteCKOrder(String orderSn,Integer projectId,Integer environment,boolean doTwo);

    /**
     * 完成门店订单
     * @param orderSn
     * @param projectId
     * @param environment
     */
    void CompleteStoreOrder(String orderSn,Integer projectId,Integer environment) throws InterruptedException;

    void CompleteDriverOrder(String orderSn,Integer projectId,Integer environment);

    String getVoucher(String sn, Integer projectId, Integer environment,Integer type);


    /**
     * 理赔审核
     */
    void lpsh(String lpsn,Integer environment,Integer type,Integer projectId);
}
