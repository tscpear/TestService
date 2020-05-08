package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TokenMapper {
    void updateToken(Token token);
}
