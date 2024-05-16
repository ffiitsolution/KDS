package com.ffi.api.kds.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.SupplyBaseDao;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class SupplyBaseDaoImpl implements SupplyBaseDao {

        private String linePos;

        // private String orderType;

        @Value("${app.outletCode}")
        private String outletCode;

        @Value("${app.charge.take.away.plu}")
        private String ctaPlu;

        private final NamedParameterJdbcTemplate jdbcTemplate;
        private final SocketTriggerService socketTriggerService;

        public SupplyBaseDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
                        final SocketTriggerService socketTriggerService,
                        final @Value("${app.line.pos}") String linePos) {
                this.jdbcTemplate = jdbcTemplate;
                this.socketTriggerService = socketTriggerService;
                this.linePos = linePos;
                // if (Objects.equals("0", linePos)) {
                // orderType = "ETA";
                // } else if (Objects.equals("3", linePos)) {
                // orderType = "DRT";
                // }
        }

        @Override
        public List<Map<String, Object>> friedQueueOrder() {
                String friedQuery = "SELECT C.KDS_NO, A.ITEM_QTY, A.BILL_NO, A.POS_CODE, A.DAY_SEQ, A.ITEM_SEQ, A.ITEM_DETAIL_SEQ, A.TRANS_DATE, D.DESCRIPTION, A.TRANS_TYPE, A.DATE_UPD DETAIL_DATE_UPD "
                                + " FROM T_KDS_ITEM_DETAIL A "
                                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 11 AND D.STATUS =  'A' "
                                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode
                                + "' AND C.ASSEMBLY_LINE_CODE='" + linePos + "' "
                                + " ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
                return jdbcTemplate.query(friedQuery, new HashMap<>(),
                                new DynamicRowMapper());
        }

        @Override
        public List<Map<String, Object>> burgerQueueOrder() {
                String pastaQuery = "SELECT C.KDS_NO, A.ITEM_QTY, A.BILL_NO, A.POS_CODE, A.DAY_SEQ, A.ITEM_SEQ, A.ITEM_DETAIL_SEQ, A.TRANS_DATE, D.DESCRIPTION, A.TRANS_TYPE, A.DATE_UPD DETAIL_DATE_UPD "
                                + " FROM T_KDS_ITEM_DETAIL A "
                                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 12 AND D.STATUS = 'A' "
                                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode
                                + "' AND C.ASSEMBLY_LINE_CODE='" + linePos + "' "
                                + " ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
                return jdbcTemplate.query(pastaQuery, new HashMap<>(),
                                new DynamicRowMapper());
        }

        @Override
        public List<Map<String, Object>> pastaQueueOrder() {
                String pastaQuery = "SELECT C.KDS_NO, A.ITEM_QTY, A.BILL_NO, A.POS_CODE, A.DAY_SEQ, A.ITEM_SEQ, A.ITEM_DETAIL_SEQ, A.TRANS_DATE, D.DESCRIPTION, A.TRANS_TYPE, A.DATE_UPD DETAIL_DATE_UPD "
                                + " FROM T_KDS_ITEM_DETAIL A "
                                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 13 AND D.STATUS =  'A'"
                                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode
                                + "' AND C.ASSEMBLY_LINE_CODE='" + linePos + "' "
                                + " ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
                return jdbcTemplate.query(pastaQuery, new HashMap<>(),
                                new DynamicRowMapper());
        }

        @Override
        public PrepareItemSupplyBaseRequest doneItemSupplyBase(PrepareItemSupplyBaseRequest request) {
                String doneItemQuery = "UPDATE T_KDS_ITEM_DETAIL SET ITEM_FLOW = 'B', ITEM_STATUS  = ' ', "
                                + " DATE_UPD=:timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                                + " WHERE BILL_NO = :billNo AND POS_CODE =:posCode AND DAY_SEQ=:daySeq "
                                + " AND ITEM_SEQ=:itemSeq AND ITEM_DETAIL_SEQ = :itemDetailSeq AND TRANS_DATE=:transDate";

                Date timestamp = new Date();
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
                jdbcTemplate.update(doneItemQuery, new MapSqlParameterSource()
                                .addValue("billNo", request.getBillNo())
                                .addValue("posCode", request.getPosCode())
                                .addValue("daySeq", request.getDaySeq())
                                .addValue("itemSeq", request.getItemSeq())
                                .addValue("itemDetailSeq", request.getItemDetailSeq())
                                .addValue("transDate", request.getTransDate())
                                .addValue("outletCode", outletCode)
                                .addValue("timeString", timeFormatter.format(timestamp))
                                .addValue("timestamp", timestamp));
                this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
                return request;
        }
}
