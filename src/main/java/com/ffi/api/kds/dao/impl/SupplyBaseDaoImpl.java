package com.ffi.api.kds.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.SupplyBaseDao;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class SupplyBaseDaoImpl implements SupplyBaseDao {

        @Value("${app.outletCode}")
        private String outletCode;

        @Value("${app.charge.take.away.plu}")
        private String ctaPlu;

        private final NamedParameterJdbcTemplate jdbcTemplate;

        public SupplyBaseDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public List<Map<String, Object>> friedQueueOrder() {
                String friedQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM A "
                                + " LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE "
                                + " LEFT JOIN T_KDS_HEADER C ON C.BILL_NO = A.BILL_NO AND C.DAY_SEQ = A.DAY_SEQ AND "
                                + " C.POS_CODE = A.POS_CODE AND C.OUTLET_CODE = A.OUTLET_CODE "
                                + " AND C.TRANS_DATE = A.TRANS_DATE WHERE C.ASSEMBLY_STATUS = 'AQ' AND A.MENU_ITEM_CODE "
                                + " IN (SELECT code FROM M_GLOBAL mg WHERE VALUE = 11 AND COND = 'ITEM' AND STATUS = 'A') "
                                + " AND 0 < (SELECT COUNT(*) FROM T_KDS_ITEM_DETAIL WHERE A.BILL_NO = BILL_NO "
                                + " AND A.ITEM_SEQ = ITEM_SEQ "
                                + " AND A.DAY_SEQ = DAY_SEQ "
                                + " AND A.POS_CODE = C.POS_CODE "
                                + " AND A.TRANS_DATE = C.TRANS_DATE "
                                + " AND A.OUTLET_CODE = C.OUTLET_CODE "
                                + " AND ITEM_STATUS = 'P')";

                List<Map<String, Object>> friedResult = jdbcTemplate.query(friedQuery, new HashMap<>(),
                                new DynamicRowMapper());
                for (Map<String, Object> fried : friedResult) {
                        String itemDetailQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE WHERE "
                                        + " A.BILL_NO = :billNo "
                                        + " AND A.ITEM_SEQ = :itemSeq"
                                        + " AND A.DAY_SEQ = :daySeq "
                                        + " AND A.POS_CODE = :posCode "
                                        + " AND A.TRANS_DATE = :transDate "
                                        + " AND A.OUTLET_CODE = :outletCode "
                                        + " ORDER BY A.ITEM_DETAIL_SEQ";
                        List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                                        new MapSqlParameterSource()
                                                        .addValue("billNo", fried.get("billNo"))
                                                        .addValue("daySeq", fried.get("daySeq"))
                                                        .addValue("posCode", fried.get("posCode"))
                                                        .addValue("transDate", fried.get("transDate"))
                                                        .addValue("outletCode", fried.get("outletCode"))
                                                        .addValue("itemSeq", fried.get("itemSeq")),
                                        new DynamicRowMapper());
                        fried.put("itemDetails", itemDetailResult);
                }
                return friedResult;
        }

        @Override
        public List<Map<String, Object>> burgerQueueOrder() {
                String burgerQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM A "
                                + " LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE "
                                + " LEFT JOIN T_KDS_HEADER C ON C.BILL_NO = A.BILL_NO AND C.DAY_SEQ = A.DAY_SEQ AND "
                                + " C.POS_CODE = A.POS_CODE AND C.OUTLET_CODE = A.OUTLET_CODE "
                                + " AND C.TRANS_DATE = A.TRANS_DATE WHERE C.ASSEMBLY_STATUS = 'AQ' AND A.MENU_ITEM_CODE "
                                + " IN (SELECT code FROM M_GLOBAL mg WHERE VALUE = 12 AND COND = 'ITEM' AND STATUS = 'A') "
                                + " AND 0 < (SELECT COUNT(*) FROM T_KDS_ITEM_DETAIL WHERE A.BILL_NO = BILL_NO "
                                + " AND A.ITEM_SEQ = ITEM_SEQ "
                                + " AND A.DAY_SEQ = DAY_SEQ "
                                + " AND A.POS_CODE = C.POS_CODE "
                                + " AND A.TRANS_DATE = C.TRANS_DATE "
                                + " AND A.OUTLET_CODE = C.OUTLET_CODE "
                                + " AND ITEM_STATUS = 'P')";
                List<Map<String, Object>> burgerResult = jdbcTemplate.query(burgerQuery, new HashMap<>(),
                                new DynamicRowMapper());
                for (Map<String, Object> burger : burgerResult) {
                        String itemDetailQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE WHERE "
                                        + " A.BILL_NO = :billNo "
                                        + " AND A.ITEM_SEQ = :itemSeq"
                                        + " AND A.DAY_SEQ = :daySeq "
                                        + " AND A.POS_CODE = :posCode "
                                        + " AND A.TRANS_DATE = :transDate "
                                        + " AND A.OUTLET_CODE = :outletCode "
                                        + " ORDER BY A.ITEM_DETAIL_SEQ";
                        List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                                        new MapSqlParameterSource()
                                                        .addValue("billNo", burger.get("billNo"))
                                                        .addValue("daySeq", burger.get("daySeq"))
                                                        .addValue("posCode", burger.get("posCode"))
                                                        .addValue("transDate", burger.get("transDate"))
                                                        .addValue("outletCode", burger.get("outletCode"))
                                                        .addValue("itemSeq", burger.get("itemSeq")),
                                        new DynamicRowMapper());
                        burger.put("itemDetails", itemDetailResult);
                }
                return burgerResult;
        }

        @Override
        public List<Map<String, Object>> pastaQueueOrder() {
                String pastaQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM A "
                                + " LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE "
                                + " LEFT JOIN T_KDS_HEADER C ON C.BILL_NO = A.BILL_NO AND C.DAY_SEQ = A.DAY_SEQ AND "
                                + " C.POS_CODE = A.POS_CODE AND C.OUTLET_CODE = A.OUTLET_CODE "
                                + " AND C.TRANS_DATE = A.TRANS_DATE WHERE C.ASSEMBLY_STATUS LIKE '%Q' AND A.MENU_ITEM_CODE "
                                + " IN (SELECT code FROM M_GLOBAL mg WHERE VALUE = 13 AND COND = 'ITEM' AND STATUS = 'A') "
                                + " AND 0 < (SELECT COUNT(*) FROM T_KDS_ITEM_DETAIL WHERE A.BILL_NO = BILL_NO "
                                + " AND A.ITEM_SEQ = ITEM_SEQ "
                                + " AND A.DAY_SEQ = DAY_SEQ "
                                + " AND A.POS_CODE = C.POS_CODE "
                                + " AND A.TRANS_DATE = C.TRANS_DATE "
                                + " AND A.OUTLET_CODE = C.OUTLET_CODE "
                                + " AND ITEM_STATUS = 'P')";

                List<Map<String, Object>> pastaResult = jdbcTemplate.query(pastaQuery, new HashMap<>(),
                                new DynamicRowMapper());
                for (Map<String, Object> pasta : pastaResult) {
                        String itemDetailQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE WHERE "
                                        + " A.BILL_NO = :billNo "
                                        + " AND A.ITEM_SEQ = :itemSeq"
                                        + " AND A.DAY_SEQ = :daySeq "
                                        + " AND A.POS_CODE = :posCode "
                                        + " AND A.TRANS_DATE = :transDate "
                                        + " AND A.OUTLET_CODE = :outletCode "
                                        + " ORDER BY A.ITEM_DETAIL_SEQ";
                        List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                                        new MapSqlParameterSource()
                                                        .addValue("billNo", pasta.get("billNo"))
                                                        .addValue("daySeq", pasta.get("daySeq"))
                                                        .addValue("posCode", pasta.get("posCode"))
                                                        .addValue("transDate", pasta.get("transDate"))
                                                        .addValue("outletCode", pasta.get("outletCode"))
                                                        .addValue("itemSeq", pasta.get("itemSeq")),
                                        new DynamicRowMapper());
                        pasta.put("itemDetails", itemDetailResult);
                }
                return pastaResult;
        }

        @Override
        public PrepareItemSupplyBaseRequest doneItemSupplyBase(PrepareItemSupplyBaseRequest request) {
                String doneItemQuery = "UPDATE T_KDS_ITEM_DETAIL SET ITEM_FLOW = 'B', ITEM_STATUS  = ' ', "
                                + " DATE_UPD=:timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                                + " WHERE BILL_NO = :billNo AND POS_CODE =:posCode AND DAY_SEQ=:daySeq AND ITEM_SEQ=:itemSeq";

                Date timestamp = new Date();
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
                jdbcTemplate.update(doneItemQuery, new MapSqlParameterSource()
                                .addValue("billNo", request.getBillNo())
                                .addValue("posCode", request.getPosCode())
                                .addValue("daySeq", request.getDaySeq())
                                .addValue("itemSeq", request.getItemSeq())
                                .addValue("outletCode", outletCode)
                                .addValue("timeString", timeFormatter.format(timestamp))
                                .addValue("timestamp", timestamp));
                return request;
        }
}
