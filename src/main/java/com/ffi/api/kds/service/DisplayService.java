package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.DisplayDao;

@Service
public class DisplayService {

    private final DisplayDao displayDao;

    public DisplayService(DisplayDao displayDao) {
        this.displayDao = displayDao;
    }

    public List<Map<String, Object>> displayReadyToServe() {
        return this.displayDao.displayReadyToServe();
    }

    public List<Map<String, Object>> displayWaitingToServe() {
        return this.displayDao.displayWaitingToServe();
    }
}
