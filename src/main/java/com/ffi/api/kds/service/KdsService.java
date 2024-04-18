package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.KdsDao;

@Service
public class KdsService {
    private final KdsDao kdsDao;

    public KdsService(KdsDao kdsDao) {
        this.kdsDao = kdsDao;
    }

    public List<Map<String, Object>> queueOrder() {
        return kdsDao.queueOrder();
    }
}
