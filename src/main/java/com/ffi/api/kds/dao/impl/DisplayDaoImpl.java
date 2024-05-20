package com.ffi.api.kds.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.DisplayDao;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class DisplayDaoImpl implements DisplayDao {
    @Value("${app.outletCode}")
    private String outletCode;
    private String linePos;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DisplayDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
            final SocketTriggerService socketTriggerService,
            final @Value("${app.line.pos}") String linePos) {
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
    }

    public List<Map<String, Object>> displayReadyToServe() {
        String readyToServeQuery = "SELECT POS_CODE, KDS_NO, BILL_NO , ORDER_TYPE, TRANS_TYPE ,NOTES " +
                "FROM T_KDS_HEADER WHERE ASSEMBLY_STATUS = 'AF' AND DISPATCH_STATUS = 'DP'" +
                " AND ASSEMBLY_LINE_CODE = '" + linePos + "' AND OUTLET_CODE = '" + outletCode + "'";
        return jdbcTemplate.query(readyToServeQuery, new HashMap<>(), new DynamicRowMapper());
    }

    public List<Map<String, Object>> displayWaitingToServe() {
        String waitingToServe = "SELECT POS_CODE, KDS_NO, BILL_NO , ORDER_TYPE, TRANS_TYPE ,NOTES " +
                " FROM T_KDS_HEADER WHERE PICKUP_STATUS = 'SRV', DISPATCH_STATUS = 'DF' " +
                " AND ASSEMBLY_LINE_CODE = '" + linePos + "' AND OUTLET_CODE = '" + outletCode + "'";

        return jdbcTemplate.query(waitingToServe, new HashMap<>(), new DynamicRowMapper());
    }

}
