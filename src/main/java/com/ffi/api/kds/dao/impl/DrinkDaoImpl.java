package com.ffi.api.kds.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.DrinkDao;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class DrinkDaoImpl implements DrinkDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DrinkDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> bibQueueOrder() {
        String bibQuery = "SELECT * FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 31 AND D.STATUS =  'A'"
                + " WHERE A.ITEM_STATUS = ' ' ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
        return jdbcTemplate.query(bibQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> iceCreamQueueOrder() {
        String iceCreamQuery = "SELECT * FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 32 AND D.STATUS =  'A'"
                + " WHERE A.ITEM_STATUS = ' ' ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
        return jdbcTemplate.query(iceCreamQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> otherQueueOrder() {
        String otherQuery = "SELECT * FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_ITEM B ON A.BILL_NO = B.BILL_NO AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.TRANS_DATE = B.TRANS_DATE AND A.ITEM_SEQ = B.ITEM_SEQ "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 33 AND D.STATUS =  'A'"
                + " WHERE A.ITEM_STATUS = ' ' ORDER BY to_number(C.KDS_NO), A.ITEM_SEQ , A.ITEM_DETAIL_SEQ";
        return jdbcTemplate.query(otherQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public PrepareItemSupplyBaseRequest doneDrink(PrepareItemSupplyBaseRequest itemKds) {
        String doneQuery = "";
        return null;
    }

}
