package com.ffi.api.kds.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

import com.ffi.api.kds.dao.KdsDao;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class KdsDaoImpl implements KdsDao {
    @Value("${app.outletCode}")
    private String outletCode;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public KdsDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> queueOrder() {
        String queryHeader = "SELECT * FROM T_KDS_HEADER tkh WHERE TKH.ASSEMBLY_STATUS = 'AQ' AND OUTLET_CODE ="+outletCode+" ORDER BY KDS_NO";
        List<Map<String, Object>> headerResults = jdbcTemplate.query(queryHeader, new HashMap(), new DynamicRowMapper());
        for (Map<String, Object> header : headerResults) {
            String billno = header.get("billNo").toString();
            BigDecimal dayseq = (BigDecimal) header.get("daySeq");
            String poscode = header.get("posCode").toString();
            Date date = (Date) header.get("transDate");

            Map<String, Object> queryItemMap = new HashMap<>();
            queryItemMap.put("billNo", billno);
            queryItemMap.put("daySeq", dayseq);
            queryItemMap.put("posCode", poscode);
            queryItemMap.put("transDate", date);
            queryItemMap.put("outletCode", outletCode);
            
            String queryItem = "SELECT * FROM T_KDS_ITEM WHERE BILL_NO = :billNo AND DAY_SEQ = :daySeq AND POS_CODE = :posCode AND OUTLET_CODE = :outletCode AND TRANS_DATE = :transDate ORDER BY ITEM_SEQ ASC";
            List<Map<String, Object>> itemResults = jdbcTemplate.query(queryItem, queryItemMap, new DynamicRowMapper());
            for (Map<String,Object> item : itemResults) {

                Map<String, Object> queryItemDetailMap = new HashMap<>();
                queryItemDetailMap.put("billNo", billno);
                queryItemDetailMap.put("daySeq", dayseq);
                queryItemDetailMap.put("posCode", poscode);
                queryItemDetailMap.put("transDate", date);
                queryItemDetailMap.put("outletCode", outletCode);
                queryItemDetailMap.put("menuItemCode", item.get("menuItemCode"));
                
                String queryItemDetail = "SELECT * FROM T_KDS_ITEM_DETAIL WHERE "
                +" MENU_ITEM_CODE = :menuItemCode "
                +" AND BILL_NO = :billNo "
                +" AND DAY_SEQ = :daySeq "
                +" AND POS_CODE = :posCode "
                +" AND TRANS_DATE = :transDate "
                +" AND OUTLET_CODE = :outletCode"
                +" AND ITEM_SEQ = 1 ORDER BY ITEM_DETAIL_SEQ";
                List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(queryItemDetail, queryItemDetailMap, new DynamicRowMapper());
                item.put("itemDetails", itemDetailResult);
            }

            header.put("items", itemResults);
        }
        return headerResults;
    }
}
