package com.ffi.api.kds.service;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.KdsDao;

@Service
public class KdsService {
    private final KdsDao kdsDao;

    public KdsService(KdsDao kdsDao) {
        this.kdsDao = kdsDao;
    }
}
