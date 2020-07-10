package com.service.apiTest.service.service;

import org.springframework.stereotype.Service;

@Service
public interface CreateTireDataService {

    void getTireTestData(Integer e, String order) throws Throwable;

}
