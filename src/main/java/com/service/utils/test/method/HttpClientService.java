package com.service.utils.test.method;

import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import org.springframework.stereotype.Service;

@Service
public interface HttpClientService {

    /**
     * 生成get请求
     */
    ResponseData getResponse(DoTestData data);

}
