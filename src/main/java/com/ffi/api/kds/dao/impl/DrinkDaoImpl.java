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

import com.ffi.api.kds.dao.DrinkDao;
import com.ffi.api.kds.dto.DoneDrinkRequest;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class DrinkDaoImpl implements DrinkDao {

    private String linePos;
    @Value("${app.outletCode}")
    private String outletCode;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DrinkDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
            final SocketTriggerService socketTriggerService,
            final @Value("${app.line.pos}") String linePos) {
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
    }

    @Override
    public List<Map<String, Object>> bibQueueOrder() {
        String bibQuery = "SELECT MG.DESCRIPTION, AA.* FROM ( "
                + " SELECT C.POS_CODE, A.KDS_NO, C.BILL_NO, C.TRANS_TYPE, C.MENU_ITEM_CODE, SUM(C.ITEM_QTY) ITEM_QTY, C.DATE_UPD DETAIL_DATE_UPD  "
                + " FROM T_KDS_HEADER A JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE "
                + "     AND A.POS_CODE = B.POS_CODE AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + "     AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'D' "
                + "     AND ITEM_STATUS <> 'F' GROUP BY A.START_TIME, C.POS_CODE, "
                + "     A.KDS_NO, C.BILL_NO,C.TRANS_TYPE, C.MENU_ITEM_CODE , C.DATE_UPD "
                + " ORDER BY TO_NUMBER(A.KDS_NO), A.START_TIME,C.MENU_ITEM_CODE ASC) AA "
                + " JOIN M_GLOBAL MG ON	MG.CODE = AA.MENU_ITEM_CODE AND MG.COND = 'ITEM'";
        return jdbcTemplate.query(bibQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> iceCreamQueueOrder() {
        String iceCreamQuery = "SELECT MG.DESCRIPTION, AA.* FROM ( "
                + " SELECT C.POS_CODE, A.KDS_NO, C.BILL_NO, C.TRANS_TYPE, C.MENU_ITEM_CODE, SUM(C.ITEM_QTY) ITEM_QTY, C.DATE_UPD DETAIL_DATE_UPD  "
                + " FROM T_KDS_HEADER A JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE "
                + "     AND A.POS_CODE = B.POS_CODE AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + "     AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'I' "
                + "     AND ITEM_STATUS <> 'F' GROUP BY A.START_TIME, C.POS_CODE, "
                + "     A.KDS_NO, C.BILL_NO,C.TRANS_TYPE, C.MENU_ITEM_CODE , C.DATE_UPD "
                + " ORDER BY TO_NUMBER(A.KDS_NO), A.START_TIME, C.MENU_ITEM_CODE ASC) AA "
                + " JOIN M_GLOBAL MG ON	MG.CODE = AA.MENU_ITEM_CODE AND MG.COND = 'ITEM'";
        return jdbcTemplate.query(iceCreamQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> otherQueueOrder() {
        String otherQuery = "SELECT MG.DESCRIPTION, AA.* FROM ( "
                + " SELECT C.POS_CODE, A.KDS_NO, C.BILL_NO, C.TRANS_TYPE, C.MENU_ITEM_CODE, SUM(C.ITEM_QTY) ITEM_QTY, C.DATE_UPD DETAIL_DATE_UPD  "
                + " FROM T_KDS_HEADER A JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE "
                + "     AND A.POS_CODE = B.POS_CODE AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + "     AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'O' "
                + "     AND ITEM_STATUS <> 'F' GROUP BY A.START_TIME, C.POS_CODE, "
                + "     A.KDS_NO, C.BILL_NO,C.TRANS_TYPE, C.MENU_ITEM_CODE , C.DATE_UPD "
                + " ORDER BY TO_NUMBER(A.KDS_NO), A.START_TIME, C.MENU_ITEM_CODE ASC) AA "
                + " JOIN M_GLOBAL MG ON	MG.CODE = AA.MENU_ITEM_CODE AND MG.COND = 'ITEM'";
        return jdbcTemplate.query(otherQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public DoneDrinkRequest doneDrink(DoneDrinkRequest request) {
        String doneItemQuery = "UPDATE T_KDS_ITEM_DETAIL SET ITEM_STATUS='F', "
                + " DATE_UPD=:timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                + " WHERE BILL_NO =:billNo AND POS_CODE=:posCode "
                + " AND MENU_ITEM_CODE =:menuItemCode AND TRANS_TYPE=:transType AND OUTLET_CODE=:outletCode "
                + " AND ITEM_STATUS <> 'F'";

        Date timestamp = new Date();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
        int rowsUpdated = jdbcTemplate.update(doneItemQuery, new MapSqlParameterSource()
                .addValue("billNo", request.getBillNo())
                .addValue("posCode", request.getPosCode())
                .addValue("transType", request.getTransType())
                .addValue("outletCode", outletCode)
                .addValue("menuItemCode", request.getMenuItemCode())
                .addValue("timeString", timeFormatter.format(timestamp))
                .addValue("timestamp", timestamp));

        if (rowsUpdated == 0) {
            System.err.format("LOG : Update data failed for Bill No: %s ", request.getBillNo());
        }
        return request;
    }

}
