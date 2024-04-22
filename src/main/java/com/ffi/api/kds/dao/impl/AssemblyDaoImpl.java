package com.ffi.api.kds.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.AssemblyDao;
import com.ffi.api.kds.dto.DoneAssemblyRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.util.DynamicRowMapper;

@Repository
public class AssemblyDaoImpl implements AssemblyDao {

    @Value("${app.outletCode}")
    private String outletCode;

    @Value("${app.charge.take.away.plu}")
    private String ctaPlu;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AssemblyDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> queueOrder() {
        String headerQuery = "SELECT * FROM T_KDS_HEADER tkh WHERE TKH.ASSEMBLY_STATUS = 'AQ' AND OUTLET_CODE ="
                + outletCode + " ORDER BY KDS_NO";
        List<Map<String, Object>> headerResults = jdbcTemplate.query(headerQuery, new HashMap<>(),
                new DynamicRowMapper());
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

            String itemQuery = "SELECT A.*, B.ITEM_DESCRIPTION, CASE WHEN A.MENU_ITEM_CODE IN (SELECT code FROM M_GLOBAL mg "
                        + " WHERE VALUE IN (11, 12, 13) AND COND = 'ITEM' AND STATUS = 'A') THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
                        + " FROM T_KDS_ITEM A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE WHERE A.BILL_NO = :billNo AND "
                        + " A.DAY_SEQ = :daySeq AND A.POS_CODE = :posCode AND A.OUTLET_CODE = :outletCode AND A.TRANS_DATE = :transDate "
                        + " ORDER BY A.ITEM_SEQ ASC";
            List<Map<String, Object>> itemResults = jdbcTemplate.query(itemQuery, queryItemMap, new DynamicRowMapper());
            for (Map<String, Object> item : itemResults) {

                Map<String, Object> queryItemDetailMap = new HashMap<>();
                queryItemDetailMap.put("billNo", billno);
                queryItemDetailMap.put("daySeq", dayseq);
                queryItemDetailMap.put("posCode", poscode);
                queryItemDetailMap.put("transDate", date);
                queryItemDetailMap.put("outletCode", outletCode);
                queryItemDetailMap.put("itemSeq", item.get("itemSeq"));
                queryItemDetailMap.put("menuItemCode", item.get("menuItemCode"));

                String itemDetailQuery = "SELECT A.*, B.ITEM_DESCRIPTION FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_ITEM B ON A.MENU_ITEM_CODE = B.ITEM_CODE WHERE "
                        + " A.BILL_NO = :billNo "
                        + " AND A.ITEM_SEQ = :itemSeq"
                        + " AND A.DAY_SEQ = :daySeq "
                        + " AND A.POS_CODE = :posCode "
                        + " AND A.TRANS_DATE = :transDate "
                        + " AND A.OUTLET_CODE = :outletCode "
                        + " ORDER BY A.ITEM_DETAIL_SEQ";
                List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery, queryItemDetailMap,
                        new DynamicRowMapper());
                item.put("itemDetails", itemDetailResult);
            }

            header.put("items", itemResults);
        }
        return headerResults;
    }

    @Override
    public DoneAssemblyRequest doneAssembly(DoneAssemblyRequest kds) {
        String doneAssemblyQuery = " UPDATE T_KDS_HEADER SET "
                + " ASSEMBLY_STATUS = 'AF', ASSEMBLY_END_TIME = :timestamp, "
                + " DISPATCH_STATUS = 'DP', DISPATCH_START_TIME = :timestamp, "
                + " PICKUP_STATUS='SRV', PICKUP_START_TIME = :timestamp "
                + "     WHERE BILL_NO = :billNo "
                + "     AND KDS_NO = :kdsNo "
                + "     AND POS_CODE = :posCode "
                + "     AND DAY_SEQ = :daySeq"
                + "     AND OUTLET_CODE = :outletCode"
                + "     AND ASSEMBLY_STATUS = 'AQ'";

        jdbcTemplate.update(doneAssemblyQuery, new MapSqlParameterSource()
                .addValue("billNo", kds.getBillNo())
                .addValue("kdsNo", kds.getKdsNo())
                .addValue("posCode", kds.getPosCode())
                .addValue("daySeq", kds.getDaySeq())
                .addValue("outletCode", outletCode)
                .addValue("timestamp", new Date()));
        return kds;
    }

    @Override
    public PrepareItemSupplyBaseRequest prepareItemSupplyBase(PrepareItemSupplyBaseRequest e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prepareItemSupplyBase'");
    }
}
