package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TokenMapper {
    /**
     * 更新token
     * @param token
     */
    void updateToken(Token token);

    /**
     * 获取token
     */
    Token getData();
}
