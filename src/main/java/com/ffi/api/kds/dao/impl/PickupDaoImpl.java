package com.ffi.api.kds.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.PickupDao;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class PickupDaoImpl implements PickupDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PickupDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> queuePickup() {
        String queuePickupQuery = "SELECT A.POS_CODE, CASE " +
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
                " A.ASSEMBLY_LINE_CODE IN ('0') AND A.ORDER_TYPE IN ('PNP', 'ETA') " +
                " AND A.OUTLET_CODE = '0208' AND ASSEMBLY_STATUS <> 'AQ' " +
                " AND (PICKUP_STATUS NOT IN ('CLM', 'UCL') OR PICKUP_STATUS IS NULL) " +
                " ORDER BY PICKUP_START_TIME ASC";

        return jdbcTemplate.query(queuePickupQuery, new HashMap<>(), new DynamicRowMapper());
    }
}
