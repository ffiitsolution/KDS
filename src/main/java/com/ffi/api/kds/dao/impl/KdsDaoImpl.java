package com.ffi.api.kds.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.KdsDao;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class KdsDaoImpl implements KdsDao {
    private String linePos;
    @Value("${app.outletCode}")
    private String outletCode;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public KdsDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
            final SocketTriggerService socketTriggerService,
            final @Value("${app.line.pos}") String linePos) {
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
    }

    @Override
    public Date getAppDate() {
        String transDateQuery = "SELECT TRANS_DATE  FROM M_OUTLET mo  WHERE OUTLET_CODE  = '" + outletCode
                + "' AND STATUS  = 'A'";
        return jdbcTemplate.queryForObject(transDateQuery, new HashMap<>(), Date.class);
    }

    @Override
    public List<Map<String, Object>> kdsTreshholdSetting() {
        String query = "SELECT CODE, DESCRIPTION , VALUE FROM M_GLOBAL mg WHERE COND = 'KDS' AND STATUS = 'A'";
        return this.jdbcTemplate.query(query, new DynamicRowMapper());
    }
}
