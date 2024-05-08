package com.ffi.api.kds.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.KdsDao;

@Service
public class KdsService {

    @Value("${app.outletCode}")
    private String outletCode;

    private final KdsDao kdsDao;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public static SimpleDateFormat dateformatDDMMMYYYY = new SimpleDateFormat("dd MMM YYYY");
    public static SimpleDateFormat timeformatHHmmss = new SimpleDateFormat("HHmmss");

    public KdsService(KdsDao kdsDao, final NamedParameterJdbcTemplate jdbcTemplate) {
        this.kdsDao = kdsDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Date getAppDate() {
        String transDateQuery = "SELECT TRANS_DATE  FROM M_OUTLET mo  WHERE OUTLET_CODE  = '" + outletCode
                + "' AND STATUS  = 'A'";
        return jdbcTemplate.queryForObject(transDateQuery, new HashMap<>(), Date.class);
    }
}
