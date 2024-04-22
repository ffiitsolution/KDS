package com.ffi.api.kds.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.SupplyBaseDao;
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
    public List<Map<String, Object>> burgerQueueOrder() {
        String friedQuery = "SELECT A.*, B.ITEM_DESCRIPTION, CASE WHEN A.MENU_ITEM_CODE IN (SELECT code FROM M_GLOBAL mg "
        + " WHERE VALUE = 11 AND COND = 'ITEM' AND STATUS = 'A') THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
        + " FROM T_KDS_ITEM A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE";
        return jdbcTemplate.query(friedQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> friedQueueOrder() {
        String friedQuery = "SELECT A.*, B.ITEM_DESCRIPTION, CASE WHEN A.MENU_ITEM_CODE IN (SELECT code FROM M_GLOBAL mg "
        + " WHERE VALUE = 12 AND COND = 'ITEM' AND STATUS = 'A') THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
        + " FROM T_KDS_ITEM A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE";
        return jdbcTemplate.query(friedQuery, new HashMap<>(), new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> pastaQueueOrder() {
        String friedQuery = "SELECT A.*, B.ITEM_DESCRIPTION, CASE WHEN A.MENU_ITEM_CODE IN (SELECT code FROM M_GLOBAL mg "
        + " WHERE VALUE = 13 AND COND = 'ITEM' AND STATUS = 'A') THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
        + " FROM T_KDS_ITEM A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE";
        return jdbcTemplate.query(friedQuery, new HashMap<>(), new DynamicRowMapper());
    }
    
}
