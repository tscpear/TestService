package com.service.apiTest.service.service;

import org.springframework.stereotype.Service;

@Service
public interface CreateTireDataService {

    void getTireTestData(String e, String order) throws Throwable;

}
