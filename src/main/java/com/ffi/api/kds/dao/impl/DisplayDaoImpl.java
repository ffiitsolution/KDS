package com.ffi.api.kds.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.DisplayDao;
import com.ffi.api.kds.service.KdsService;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class DisplayDaoImpl implements DisplayDao {
    @Value("${app.outletCode}")
    private String outletCode;
    private String linePos;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KdsService kdsService;

    public DisplayDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
            final SocketTriggerService socketTriggerService,
            final @Value("${app.line.pos}") String linePos,
            final KdsService kdsService) {
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
        this.kdsService = kdsService;
    }

    public List<Map<String, Object>> displayReadyToServe() {
        Date date = kdsService.getAppDate();
        String dateString = KdsService.dateformatDDMMMYYYY.format(date);

        String readyToServeQuery = "SELECT POS_CODE, KDS_NO, BILL_NO , ORDER_TYPE, TRANS_TYPE ,NOTES " +
        " FROM T_KDS_HEADER WHERE PICKUP_STATUS = 'SRV' AND DISPATCH_STATUS = 'DF' AND TRANS_DATE = '"+dateString+"'" +
        " AND ASSEMBLY_LINE_CODE = '" + linePos + "' AND OUTLET_CODE = '" + outletCode + "' AND ROWNUM < 11 "
        +" ORDER BY TRANS_DATE, TO_NUMBER(KDS_NO), BILL_NO ";
        
        
        return jdbcTemplate.query(readyToServeQuery, new HashMap<>(), new DynamicRowMapper());
    }

    public List<Map<String, Object>> displayWaitingToServe() {
        Date date = kdsService.getAppDate();
        String dateString = KdsService.dateformatDDMMMYYYY.format(date);
        
        String waitingToServe = "SELECT POS_CODE, KDS_NO, BILL_NO , ORDER_TYPE, TRANS_TYPE ,NOTES " +
        "FROM T_KDS_HEADER WHERE (PICKUP_STATUS NOT IN ('CLM', 'UCL') OR PICKUP_STATUS IS NULL) AND TRANS_DATE = '"+dateString+"'" +
        " AND ASSEMBLY_LINE_CODE = '" + linePos + "' AND OUTLET_CODE = '" + outletCode + "' AND ROWNUM < 10 "
        +" ORDER BY TRANS_DATE, TO_NUMBER(KDS_NO), BILL_NO ";

        return jdbcTemplate.query(waitingToServe, new HashMap<>(), new DynamicRowMapper());
    }

}
