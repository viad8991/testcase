package ru.test.service;

import ru.test.dto.OkvedLookupResult;

public interface OkvedService {

    OkvedLookupResult findOkvedByPhone(String phoneNumber);

}
