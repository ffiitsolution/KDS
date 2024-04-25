package com.ffi.api.kds.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
                                + outletCode + " ORDER BY TO_NUMBER(KDS_NO)";
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

                        String itemQuery = "SELECT A.*, B.DESCRIPTION, CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 "
                                        + " END AS PREPARE_MENU_FLAG FROM T_KDS_ITEM A LEFT JOIN M_GLOBAL B ON A.MENU_ITEM_CODE = B.CODE AND B.COND = 'ITEM' AND B.STATUS = 'A'"
                                        + " WHERE A.BILL_NO = :billNo AND A.DAY_SEQ = :daySeq AND A.POS_CODE = :posCode "
                                        + " AND A.OUTLET_CODE = :outletCode AND A.TRANS_DATE = :transDate "
                                        + " ORDER BY A.ITEM_SEQ ASC";
                        List<Map<String, Object>> itemResults = jdbcTemplate.query(itemQuery, queryItemMap,
                                        new DynamicRowMapper());
                        for (Map<String, Object> item : itemResults) {

                                Map<String, Object> queryItemDetailMap = new HashMap<>();
                                queryItemDetailMap.put("billNo", billno);
                                queryItemDetailMap.put("daySeq", dayseq);
                                queryItemDetailMap.put("posCode", poscode);
                                queryItemDetailMap.put("transDate", date);
                                queryItemDetailMap.put("outletCode", outletCode);
                                queryItemDetailMap.put("itemSeq", item.get("itemSeq"));
                                queryItemDetailMap.put("menuItemCode", item.get("menuItemCode"));

                                String itemDetailQuery = "SELECT A.*, B.DESCRIPTION, CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
                                                + " FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_GLOBAL B ON A.MENU_ITEM_CODE = B.CODE AND B.COND = 'ITEM' AND B.STATUS = 'A' WHERE "
                                                + " A.BILL_NO = :billNo "
                                                + " AND A.ITEM_SEQ = :itemSeq "
                                                + " AND A.DAY_SEQ = :daySeq "
                                                + " AND A.POS_CODE = :posCode "
                                                + " AND A.TRANS_DATE = :transDate "
                                                + " AND A.OUTLET_CODE = :outletCode "
                                                + " ORDER BY A.ITEM_DETAIL_SEQ";
                                List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                                                queryItemDetailMap,
                                                new DynamicRowMapper());

                                String menuItemCode = (String) item.get("menuItemCode");
                                OptionalInt indexOpt = IntStream.range(0, itemDetailResult.size())
                                                .filter(i -> menuItemCode.equals(itemDetailResult.get(i).get("menuItemCode")))
                                                .findFirst();
                                if (indexOpt.isPresent() && itemDetailResult.size() == 1) {
                                        item.put("itemDetails", new ArrayList<>());
                                        item.putAll(itemDetailResult.get(0));
                                } else {
                                        item.put("itemDetails", itemDetailResult);
                                }
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
                                + " PICKUP_STATUS='SRV', PICKUP_START_TIME = :timestamp, "
                                + " DATE_UPD = :timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                                + "     WHERE BILL_NO = :billNo "
                                + "     AND KDS_NO = :kdsNo "
                                + "     AND POS_CODE = :posCode "
                                + "     AND DAY_SEQ = :daySeq"
                                + "     AND OUTLET_CODE = :outletCode"
                                + "     AND ASSEMBLY_STATUS = 'AQ'";

                Date timestamp = new Date();
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
                jdbcTemplate.update(doneAssemblyQuery, new MapSqlParameterSource()
                                .addValue("billNo", kds.getBillNo())
                                .addValue("kdsNo", kds.getKdsNo())
                                .addValue("posCode", kds.getPosCode())
                                .addValue("daySeq", kds.getDaySeq())
                                .addValue("outletCode", outletCode)
                                .addValue("timeString", timeFormatter.format(timestamp))
                                .addValue("timestamp", timestamp));
                return kds;
        }

        @Override
        public PrepareItemSupplyBaseRequest prepareItemSupplyBase(PrepareItemSupplyBaseRequest request) {
                String bumpQuery = "UPDATE T_KDS_ITEM_DETAIL SET ITEM_FLOW = 'B', ITEM_STATUS  = 'P', "
                                + " DATE_UPD=:timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                                + " WHERE BILL_NO = :billNo AND POS_CODE =:posCode AND DAY_SEQ=:daySeq "
                                + " AND ITEM_SEQ=:itemSeq AND ITEM_DETAIL_SEQ=:itemDetailSeq AND TRANS_DATE=:transDate";
                Date timestamp = new Date();
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
                jdbcTemplate.update(bumpQuery, new MapSqlParameterSource()
                                .addValue("billNo", request.getBillNo())
                                .addValue("posCode", request.getPosCode())
                                .addValue("daySeq", request.getDaySeq())
                                .addValue("itemSeq", request.getItemSeq())
                                .addValue("itemDetailSeq", request.getItemDetailSeq())
                                .addValue("transDate", request.getTransDate())
                                .addValue("outletCode", outletCode)
                                .addValue("timeString", timeFormatter.format(timestamp))
                                .addValue("timestamp", timestamp));
                return request;
        }
}
