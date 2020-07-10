package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.entity.NewToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface NewTokenMapper {
    /**
     * 判断账号是否存在
     * @param newToken
     * @return
     */
    Integer countOfAccount(NewToken newToken);

    /**
     * 更新token
     * @param newToken
     */
    void updateToken(NewToken newToken);

    /**
     * 插入账号
     * @param newToken
     */
    void insertAccount(NewToken newToken);

    /**
     * 获取token
     * @param newToken
     * @return
     */
    String getToken(NewToken newToken);

    /**
     * 获取data
     * @param newToken
     * @return
     */
    String getData(NewToken newToken);
}
