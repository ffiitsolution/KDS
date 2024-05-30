package com.ffi.api.kds.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.KdsDao;

@Service
public class KdsService {

    @Value("${app.outletCode}")
    private String outletCode;

    private final KdsDao kdsDao;
    public static SimpleDateFormat dateformatDDMMMYYYY = new SimpleDateFormat("dd MMM YYYY");
    public static SimpleDateFormat timeformatHHmmss = new SimpleDateFormat("HHmmss");

    public KdsService(KdsDao kdsDao) {
        this.kdsDao = kdsDao;
    }

    public Date getAppDate() {
        return this.kdsDao.getAppDate();
    }

    public List<Map<String, Object>> kdsTreshholdSetting() {
        return this.kdsDao.kdsTreshholdSetting();
    }
}
