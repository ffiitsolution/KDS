package com.ffi.api.kds.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.PickupDao;
import com.ffi.api.kds.dto.KdsHeaderRequest;
import com.ffi.api.kds.service.KdsService;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class PickupDaoImpl implements PickupDao {

    private String linePos;
    @Value("${app.outletCode}")
    private String outletCode;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SocketTriggerService socketTriggerService;

    public PickupDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
            final SocketTriggerService socketTriggerService,
            final @Value("${app.line.pos}") String linePos) {
        this.jdbcTemplate = jdbcTemplate;
        this.socketTriggerService = socketTriggerService;
        this.linePos = linePos;
    }

    @Override
    public List<Map<String, Object>> queuePickup() {
        String queuePickupQuery = "SELECT A.POS_CODE, A.DAY_SEQ, CASE " +
                " WHEN A.BILL_NO LIKE '%T%' " +
                " AND NVL(B.CUSTOMER_NAME, ' ') != ' ' THEN SUBSTR(A.BILL_NO, 0, 3) " +
                " WHEN A.BILL_NO LIKE '%T%' " +
                " AND NVL(B.CUSTOMER_NAME, ' ') = ' ' THEN SUBSTR(A.BILL_NO, 10, 5) " +
                " ELSE NVL(B.TABLE_NO, A.KDS_NO) " +
                " END AS KDS_NO, A.BILL_NO,A.ORDER_TYPE, A.ORDER_TYPE, A.PICKUP_STATUS, " +
                " A.ASSEMBLY_LINE_CODE, A.TRANS_TYPE, A.PICKUP_START_TIME, SYSDATE, " +
                " ((SYSDATE - A.START_TIME) * 24 * 60 * 60) AS PROCESS_TIME, " +
                " ((SYSDATE - A.PICKUP_START_TIME) * 24 * 60 * 60) AS PICKUP_TIME, " +
                " ((A.FINISH_TIME - A.START_TIME) * 24 * 60 * 60) AS KDS_TIME, " +
                " NVL(B.CUSTOMER_NAME, ' ') AS CUSTOMER_NAME, NVL(B.PRINT_NO, 0) AS NO_PRINT " +
                " FROM T_KDS_HEADER A LEFT JOIN T_KDS_NAME B ON " +
                " A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE " +
                " AND A.OUTLET_CODE = B.OUTLET_CODE WHERE " +
                " A.ASSEMBLY_LINE_CODE IN ('" + linePos + "') " +
                " AND A.OUTLET_CODE = '" + outletCode + "' AND ASSEMBLY_STATUS <> 'AQ' " +
                " AND (PICKUP_STATUS NOT IN ('CLM', 'UCL') OR PICKUP_STATUS IS NULL) " +
                " ORDER BY PICKUP_START_TIME ASC";

        return jdbcTemplate.query(queuePickupQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public KdsHeaderRequest servePickup(KdsHeaderRequest request) {
        String serveQuery = " UPDATE T_KDS_HEADER SET "
                + " PICKUP_STATUS = 'SRV', DISPATCH_STATUS = 'DF',"
                + " DISPATCH_END_TIME = :timestamp, PICKUP_START_TIME = :timestamp, "
                + " DATE_UPD = :timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                + "     WHERE BILL_NO = :billNo "
                + "     AND KDS_NO = :kdsNo "
                + "     AND POS_CODE = :posCode "
                + "     AND DAY_SEQ = :daySeq"
                + "     AND OUTLET_CODE = :outletCode AND (PICKUP_STATUS IS NULL OR PICKUP_STATUS = ' ')";
        Date timestamp = new Date();
        jdbcTemplate.update(serveQuery, new MapSqlParameterSource()
                .addValue("billNo", request.getBillNo())
                .addValue("kdsNo", request.getKdsNo())
                .addValue("posCode", request.getPosCode())
                .addValue("daySeq", request.getDaySeq())
                .addValue("outletCode", outletCode)
                .addValue("timeString", KdsService.timeformatHHmmss.format(timestamp))
                .addValue("timestamp", timestamp));
        return request;
    }

    @Override
    public KdsHeaderRequest claimPickup(KdsHeaderRequest request) {
        String claimQuery = " UPDATE T_KDS_HEADER SET "
                + " PICKUP_STATUS = 'CLM',"
                + " PICKUP_END_TIME = :timestamp, FINISH_TIME=:timestamp, "
                + " DATE_UPD = :timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                + "     WHERE BILL_NO = :billNo "
                + "     AND KDS_NO = :kdsNo "
                + "     AND POS_CODE = :posCode "
                + "     AND DAY_SEQ = :daySeq"
                + "     AND OUTLET_CODE = :outletCode AND PICKUP_STATUS='SRV'";
        Date timestamp = new Date();
        jdbcTemplate.update(claimQuery, new MapSqlParameterSource()
                .addValue("billNo", request.getBillNo())
                .addValue("kdsNo", request.getKdsNo())
                .addValue("posCode", request.getPosCode())
                .addValue("daySeq", request.getDaySeq())
                .addValue("outletCode", outletCode)
                .addValue("timeString", KdsService.timeformatHHmmss.format(timestamp))
                .addValue("timestamp", timestamp));
        return request;
    }

    @Override
    public KdsHeaderRequest unclaimPickup(KdsHeaderRequest request) {
        String unclaimQuery = " UPDATE T_KDS_HEADER SET "
                + " PICKUP_STATUS = 'UCL',"
                + " FINISH_TIME=:timestamp, "
                + " DATE_UPD = :timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                + "     WHERE BILL_NO = :billNo "
                + "     AND KDS_NO = :kdsNo "
                + "     AND POS_CODE = :posCode "
                + "     AND DAY_SEQ = :daySeq"
                + "     AND OUTLET_CODE = :outletCode AND PICKUP_STATUS='SRV'";
        Date timestamp = new Date();
        jdbcTemplate.update(unclaimQuery, new MapSqlParameterSource()
                .addValue("billNo", request.getBillNo())
                .addValue("kdsNo", request.getKdsNo())
                .addValue("posCode", request.getPosCode())
                .addValue("daySeq", request.getDaySeq())
                .addValue("outletCode", outletCode)
                .addValue("timeString", KdsService.timeformatHHmmss.format(timestamp))
                .addValue("timestamp", timestamp));
        return request;
    }
}
