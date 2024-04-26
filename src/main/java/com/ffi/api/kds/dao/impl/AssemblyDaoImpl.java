package com.ffi.api.kds.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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
import com.ffi.api.kds.util.TransformGroupingData;

@Repository
public class AssemblyDaoImpl implements AssemblyDao {

        @Value("${app.outletCode}")
        private String outletCode;

        @Value("${app.line.pos}")
        private String linePos;

        @Value("${app.charge.take.away.plu}")
        private String ctaPlu;

        private final NamedParameterJdbcTemplate jdbcTemplate;

        public AssemblyDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public List<Map<String, Object>> queueOrder() {
                String queueQuery = "SELECT TKH.OUTLET_CODE, TKH.POS_CODE, "
                                + "        TKH.DAY_SEQ, TKH.TRANS_DATE, TKH.KDS_NO, TKH.BILL_NO, "
                                + "        TKH.ORDER_TYPE, TKH.TRANS_TYPE BILL_TRANS_TYPE, NVL(TKH.NOTES, '') NOTES , "
                                + "        TKHI.ITEM_SEQ, (CASE "
                                + "                WHEN TKH.ORDER_TYPE = 'DRT' THEN 'DT' "
                                + "                WHEN TKH.NOTES LIKE '%NO_URUT%' THEN 'POT' "
                                + "                WHEN TKH.ORDER_TYPE = 'HMD' THEN 'HD' "
                                + "                ELSE TKHI.TRANS_TYPE END) ITEM_TRANS_TYPE, TKHI.MENU_ITEM_CODE, "
                                + "        MG.DESCRIPTION, CASE WHEN MG.VALUE IN (11, 12, 13) THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG, "
                                + " TKHI.ITEM_QTY, TKHID.ITEM_DETAIL_SEQ, "
                                + "        TKHID.MENU_ITEM_CODE MENU_ITEM_DETAIL_CODE, "
                                + " MG2.DESCRIPTION DETAIL_DESCRIPTION, CASE WHEN MG.VALUE IN (11, 12, 13) THEN 1 ELSE 0 END AS PREPARE_MENU_DETAIL_FLAG, "
                                + "        TKHID.ITEM_QTY ITEM_DETAIL_QTY, TKHID.ITEM_TYPE, "
                                + "        TKHID.ITEM_FLOW, TKHID.ITEM_FLOW_ORI, TKHID.ITEM_STATUS, "
                                + "        ((SYSDATE - TKH.ASSEMBLY_START_TIME) * 24 * 60 * 60) AS ASSEMBLY_QUEUE_TIME"
                                + " FROM T_KDS_HEADER TKH "
                                + " JOIN T_KDS_ITEM TKHI ON "
                                + "        TKH.OUTLET_CODE = TKHI.OUTLET_CODE AND TKH.POS_CODE = TKHI.POS_CODE "
                                + "        AND TKH.DAY_SEQ = TKHI.DAY_SEQ AND TKH.BILL_NO = TKHI.BILL_NO "
                                + " JOIN T_KDS_ITEM_DETAIL TKHID ON TKH.OUTLET_CODE = TKHID.OUTLET_CODE "
                                + "        AND TKH.POS_CODE = TKHID.POS_CODE AND TKH.DAY_SEQ = TKHID.DAY_SEQ "
                                + "        AND TKH.BILL_NO = TKHID.BILL_NO AND TKHI.ITEM_SEQ = TKHID.ITEM_SEQ "
                                + " LEFT JOIN M_GLOBAL MG ON TKHI.MENU_ITEM_CODE = MG.CODE AND MG.COND = 'ITEM' "
                                + " LEFT JOIN M_GLOBAL MG2 ON TKHID.MENU_ITEM_CODE = MG2.CODE AND MG2.COND = 'ITEM' "
                                + " WHERE TKH.OUTLET_CODE = '" + outletCode + "' AND TKH.ASSEMBLY_STATUS = 'AQ' "
                                + "        AND MG.VALUE NOT IN ('99') AND TKH.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                                + " ORDER BY TKH.START_TIME ASC, TKH.BILL_NO, TKHI.ITEM_SEQ, TKHID.ITEM_DETAIL_SEQ ";

                List<Map<String, Object>> queueResult = jdbcTemplate.query(queueQuery, new DynamicRowMapper());
                List<String> groupBy = new LinkedList<>();
                groupBy.add("kdsNo");
                groupBy.add("orderType");
                groupBy.add("daySeq");
                groupBy.add("billNo");
                groupBy.add("transDate");
                groupBy.add("posCode");
                groupBy.add("notes");
                groupBy.add("description");
                groupBy.add("prepareMenuFlag");
                
                return TransformGroupingData.transformData(queueResult, groupBy, "item");

                // String headerQuery = "SELECT * FROM T_KDS_HEADER tkh WHERE TKH.ASSEMBLY_STATUS = 'AQ' AND OUTLET_CODE ="
                //                 + outletCode + " ORDER BY TO_NUMBER(KDS_NO)";
                // List<Map<String, Object>> headerResults = jdbcTemplate.query(headerQuery, new HashMap<>(),
                //                 new DynamicRowMapper());
                // for (Map<String, Object> header : headerResults) {
                //         String billno = header.get("billNo").toString();
                //         BigDecimal dayseq = (BigDecimal) header.get("daySeq");
                //         String poscode = header.get("posCode").toString();
                //         Date date = (Date) header.get("transDate");

                //         Map<String, Object> queryItemMap = new HashMap<>();
                //         queryItemMap.put("billNo", billno);
                //         queryItemMap.put("daySeq", dayseq);
                //         queryItemMap.put("posCode", poscode);
                //         queryItemMap.put("transDate", date);
                //         queryItemMap.put("outletCode", outletCode);

                //         String itemQuery = "SELECT A.*, B.DESCRIPTION, CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 "
                //                         + " END AS PREPARE_MENU_FLAG FROM T_KDS_ITEM A LEFT JOIN M_GLOBAL B ON A.MENU_ITEM_CODE = B.CODE AND B.COND = 'ITEM' AND B.STATUS = 'A'"
                //                         + " WHERE A.BILL_NO = :billNo AND A.DAY_SEQ = :daySeq AND A.POS_CODE = :posCode "
                //                         + " AND A.OUTLET_CODE = :outletCode AND A.TRANS_DATE = :transDate "
                //                         + " ORDER BY A.ITEM_SEQ ASC";
                //         List<Map<String, Object>> itemResults = jdbcTemplate.query(itemQuery, queryItemMap,
                //                         new DynamicRowMapper());
                //         for (Map<String, Object> item : itemResults) {

                //                 Map<String, Object> queryItemDetailMap = new HashMap<>();
                //                 queryItemDetailMap.put("billNo", billno);
                //                 queryItemDetailMap.put("daySeq", dayseq);
                //                 queryItemDetailMap.put("posCode", poscode);
                //                 queryItemDetailMap.put("transDate", date);
                //                 queryItemDetailMap.put("outletCode", outletCode);
                //                 queryItemDetailMap.put("itemSeq", item.get("itemSeq"));
                //                 queryItemDetailMap.put("menuItemCode", item.get("menuItemCode"));

                //                 String itemDetailQuery = "SELECT A.*, B.DESCRIPTION, CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 END AS PREPARE_MENU_FLAG "
                //                                 + " FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_GLOBAL B ON A.MENU_ITEM_CODE = B.CODE AND B.COND = 'ITEM' AND B.STATUS = 'A' WHERE "
                //                                 + " A.BILL_NO = :billNo "
                //                                 + " AND A.ITEM_SEQ = :itemSeq "
                //                                 + " AND A.DAY_SEQ = :daySeq "
                //                                 + " AND A.POS_CODE = :posCode "
                //                                 + " AND A.TRANS_DATE = :transDate "
                //                                 + " AND A.OUTLET_CODE = :outletCode "
                //                                 + " ORDER BY A.ITEM_DETAIL_SEQ";
                //                 List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                //                                 queryItemDetailMap,
                //                                 new DynamicRowMapper());

                //                 String menuItemCode = (String) item.get("menuItemCode");
                //                 OptionalInt indexOpt = IntStream.range(0, itemDetailResult.size())
                //                                 .filter(i -> menuItemCode
                //                                                 .equals(itemDetailResult.get(i).get("menuItemCode")))
                //                                 .findFirst();
                //                 if (indexOpt.isPresent() && itemDetailResult.size() == 1) {
                //                         item.put("itemDetails", new ArrayList<>());
                //                         item.putAll(itemDetailResult.get(0));
                //                 } else {
                //                         OptionalInt indexBumpFlag = IntStream.range(0, itemDetailResult.size())
                //                                         .filter(i -> itemDetailResult.get(i).get("prepareMenuFlag")
                //                                                         .equals(BigDecimal.ONE))
                //                                         .findFirst();
                //                         if (indexBumpFlag.isPresent()) {
                //                                 item.put("prepareMenuFlag", 0);
                //                         }
                //                         item.put("itemDetails", itemDetailResult);
                //                 }
                //         }
                //         header.put("items", itemResults);
                // }
                // return headerResults;
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
