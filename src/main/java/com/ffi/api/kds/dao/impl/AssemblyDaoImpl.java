package com.ffi.api.kds.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.AssemblyDao;
import com.ffi.api.kds.dto.KdsHeaderRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.KdsService;
import com.ffi.api.kds.service.SocketTriggerService;
import com.ffi.api.kds.util.DynamicRowMapper;
import com.ffi.api.kds.util.TransformGroupingData;

@Repository
public class AssemblyDaoImpl implements AssemblyDao {

        @Value("${app.outletCode}")
        private String outletCode;
        private String linePos;
        @Value("${app.charge.take.away.plu}")
        private String ctaPlu;

        private final NamedParameterJdbcTemplate jdbcTemplate;
        private KdsService kdsService;

        public AssemblyDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate,
                        final SocketTriggerService socketTriggerService,
                        final @Value("${app.line.pos}") String linePos,
                        final KdsService kdsService) {
                this.jdbcTemplate = jdbcTemplate;
                this.linePos = linePos;
                this.kdsService = kdsService;
        }

        @Override
        public List<Map<String, Object>> queueOrder() {
                String queueQuery = "SELECT TKH.OUTLET_CODE, TKH.POS_CODE, TKH.ASSEMBLY_START_TIME, TKH.START_TIME,"
                                + "        TKH.DAY_SEQ, TKH.TRANS_DATE, TKH.KDS_NO, TKH.BILL_NO, MG3.DESCRIPTION AS ORDER_TYPE_DESC, "
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
                                + " LEFT JOIN M_GLOBAL MG3 ON TKH.ORDER_TYPE = MG3.CODE AND MG3.COND = 'ORDER_TYPE' "
                                + " WHERE TKH.OUTLET_CODE = '" + outletCode + "' AND TKH.ASSEMBLY_STATUS = 'AQ' AND TKHI.MENU_ITEM_CODE <> '"+ctaPlu+"' "
                                + "        AND MG.VALUE NOT IN ('99') AND TKH.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                                + " ORDER BY TKH.START_TIME ASC, TKH.BILL_NO, TKHI.ITEM_SEQ, TKHID.ITEM_DETAIL_SEQ ";
                List<Map<String, Object>> queueResult = jdbcTemplate.query(queueQuery, new DynamicRowMapper());
                List<String> groupByHeader = new LinkedList<>();
                groupByHeader.add("kdsNo");
                groupByHeader.add("orderType");
                groupByHeader.add("daySeq");
                groupByHeader.add("billNo");
                groupByHeader.add("transDate");
                groupByHeader.add("posCode");
                groupByHeader.add("notes");
                groupByHeader.add("assemblyStartTime");
                groupByHeader.add("startTime");
                groupByHeader.add("orderTypeDesc");
                List<Map<String, Object>> itemsResult = TransformGroupingData.transformData(queueResult, groupByHeader,
                                "items");
                for (Map<String, Object> map : itemsResult) {
                        List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
                        List<String> groupByItem = new LinkedList<>();
                        groupByItem.add("itemSeq");
                        groupByItem.add("menuItemCode");
                        groupByItem.add("prepareMenuFlag");
                        groupByItem.add("description");
                        groupByItem.add("itemQty");
                        map.put("items", TransformGroupingData.transformData(items, groupByItem, "itemDetails"));
                        items = (List<Map<String, Object>>) map.get("items");
                        for (Map<String, Object> map2 : items) {
                                List<Map<String, Object>> itemDetails = (List<Map<String, Object>>) map2
                                                .get("itemDetails");
                                if (itemDetails.size() == 1 && Objects.equals(map2.get("menuItemCode"),
                                                itemDetails.get(0).get("menuItemDetailCode"))) {
                                        map2.put("itemDetails", new ArrayList<>());
                                        map2.putAll(itemDetails.get(0));
                                } else {
                                        OptionalInt indexBumpFlag = IntStream.range(0, itemDetails.size())
                                                        .filter(i -> itemDetails.get(i).get("prepareMenuDetailFlag")
                                                                        .equals(BigDecimal.ONE))
                                                        .findFirst();
                                        if (indexBumpFlag.isPresent()) {
                                                map2.put("prepareMenuFlag", 0);
                                        }
                                }
                        }
                }
                return itemsResult;
        }

        @Override
        public Map<String, Object> getOrder(KdsHeaderRequest request) {
                String headerQuery = "SELECT coalesce(mps.STAFF_NAME, ' ') CASHIER_STAFF_NAME, tkh.BILL_NO, tkh.NOTES, tkh.KDS_NO, tkh.TRANS_DATE, tkh.DATE_UPD, " +
                                " tkh.ASSEMBLY_START_TIME, tkh.START_TIME, tkh.ORDER_TYPE, MG3.DESCRIPTION ORDER_TYPE_DESC, tkh.DAY_SEQ, tkh.POS_CODE "
                                + " FROM T_KDS_HEADER tkh LEFT JOIN M_GLOBAL MG3 ON tkh.ORDER_TYPE = MG3.CODE " +
                                " AND MG3.COND = 'ORDER_TYPE' AND MG3.STATUS = 'A' " +
                                " LEFT JOIN T_POS_BILL tpb ON tpb.OUTLET_CODE = tkh.OUTLET_CODE AND tpb.BILL_NO = tkh.BILL_NO AND tpb.DAY_SEQ = tkh.DAY_SEQ AND tpb.POS_CODE = tkh.POS_CODE AND tpb.TRANS_DATE = tkh.TRANS_DATE " +
                                " LEFT JOIN M_POS_STAFF mps ON mps.STAFF_POS_CODE = tpb.CASHIER_CODE AND mps.STATUS='A' " +
                                " WHERE tkh.BILL_NO = :billNo AND tkh.KDS_NO =:kdsNo " +
                                " AND tkh.POS_CODE = :posCode AND tkh.DAY_SEQ = :daySeq ";
                Map<String, Object> headerResult = jdbcTemplate.queryForObject(headerQuery, new MapSqlParameterSource()
                                .addValue("billNo", request.getBillNo())
                                .addValue("kdsNo", request.getKdsNo())
                                .addValue("posCode", request.getPosCode())
                                .addValue("daySeq", request.getDaySeq()), new DynamicRowMapper());

                String billno = headerResult.get("billNo").toString();
                BigDecimal dayseq = (BigDecimal) headerResult.get("daySeq");
                String poscode = headerResult.get("posCode").toString();
                Date date = (Date) headerResult.get("transDate");

                String itemQuery = "SELECT A.ITEM_QTY, A.MENU_ITEM_CODE, A.ITEM_SEQ, B.DESCRIPTION, "
                                + " CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 "
                                + " END AS PREPARE_MENU_FLAG, A.TRANS_TYPE ITEM_TRANS_TYPE FROM T_KDS_ITEM A LEFT JOIN M_GLOBAL B ON "
                                + " A.MENU_ITEM_CODE = B.CODE AND B.COND = 'ITEM' AND B.STATUS = 'A'"
                                + " WHERE A.BILL_NO = :billNo AND A.DAY_SEQ = :daySeq AND A.POS_CODE=:posCode "
                                + " AND A.OUTLET_CODE = :outletCode AND A.TRANS_DATE = :transDate  AND A.MENU_ITEM_CODE <> '"+ctaPlu+"'"
                                + " ORDER BY A.ITEM_SEQ ASC ";
                List<Map<String, Object>> itemResults = jdbcTemplate.query(itemQuery,
                                new MapSqlParameterSource()
                                                .addValue("billNo", billno)
                                                .addValue("daySeq", dayseq)
                                                .addValue("posCode", poscode)
                                                .addValue("outletCode", outletCode)
                                                .addValue("transDate", date),
                                new DynamicRowMapper());
                for (Map<String, Object> item : itemResults) {
                        String itemDetailQuery = "SELECT A.ITEM_DETAIL_SEQ, ITEM_QTY, A.MENU_ITEM_CODE MENU_ITEM_DETAIL_CODE, B.DESCRIPTION DETAIL_DESCRIPTION, "
                                        + " CASE WHEN B.VALUE IN (11,12,13) THEN 1 ELSE 0 END AS PREPARE_MENU_DETAIL_FLAG "
                                        + " FROM T_KDS_ITEM_DETAIL A LEFT JOIN M_GLOBAL B ON A.MENU_ITEM_CODE =B.CODE "
                                        + " AND B.COND = 'ITEM' AND B.STATUS = 'A' WHERE "
                                        + " A.BILL_NO = :billNo "
                                        + " AND A.ITEM_SEQ = :itemSeq "
                                        + " AND A.DAY_SEQ = :daySeq "
                                        + " AND A.POS_CODE = :posCode "
                                        + " AND A.TRANS_DATE = :transDate "
                                        + " AND A.OUTLET_CODE = :outletCode "
                                        + " AND A.MENU_ITEM_CODE <> '"+ctaPlu+"' "
                                        + " ORDER BY A.ITEM_DETAIL_SEQ ";
                        List<Map<String, Object>> itemDetailResult = jdbcTemplate.query(itemDetailQuery,
                                        new MapSqlParameterSource()
                                                        .addValue("billNo", billno)
                                                        .addValue("daySeq", dayseq)
                                                        .addValue("posCode", poscode)
                                                        .addValue("transDate", date)
                                                        .addValue("outletCode", outletCode)
                                                        .addValue("itemSeq", item.get("itemSeq"))
                                                        .addValue("menuItemCode", item.get("menuItemCode")),
                                        new DynamicRowMapper());
                        item.put("itemDetails", itemDetailResult);
                }
                headerResult.put("items", itemResults);
                return headerResult;
        }

        @Override
        public KdsHeaderRequest doneAssembly(KdsHeaderRequest kds) {
                String doneAssemblyQuery = " UPDATE T_KDS_HEADER SET "
                                + " ASSEMBLY_STATUS = 'AF', ASSEMBLY_END_TIME = :timestamp, "
                                + " DISPATCH_STATUS = 'DP', DISPATCH_START_TIME = :timestamp, "
                                + " PICKUP_START_TIME = :timestamp, FINISH_TIME= :timestamp, "
                                + " DATE_UPD = :timestamp, TIME_UPD=:timeString, USER_UPD=NULL "
                                + "     WHERE BILL_NO = :billNo "
                                + "     AND KDS_NO = :kdsNo "
                                + "     AND POS_CODE = :posCode "
                                + "     AND DAY_SEQ = :daySeq"
                                + "     AND OUTLET_CODE = :outletCode"
                                + "     AND ASSEMBLY_STATUS = 'AQ'";

                Date timestamp = new Date();
                jdbcTemplate.update(doneAssemblyQuery, new MapSqlParameterSource()
                                .addValue("billNo", kds.getBillNo())
                                .addValue("kdsNo", kds.getKdsNo())
                                .addValue("posCode", kds.getPosCode())
                                .addValue("daySeq", kds.getDaySeq())
                                .addValue("outletCode", outletCode)
                                .addValue("timeString", KdsService.timeformatHHmmss.format(timestamp))
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

                // socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
                // socketTriggerService.refreshSupplyBaseFried(UUID.randomUUID().toString());
                // socketTriggerService.refreshSupplyBaseBurger(UUID.randomUUID().toString());
                // socketTriggerService.refreshSupplyBasePasta(UUID.randomUUID().toString());
                return request;
        }

        @Override
        public List<Map<String, Object>> queuePending() {
                // got query from pak asep, writed by Dani
                String pendingQuery = "SELECT a.MENU_ITEM_CODE, a.pos_code, b.kds_no,c.DESCRIPTION, a.TRANS_TYPE , a.ITEM_QTY "
                                + "  FROM T_KDS_ITEM_DETAIL a LEFT JOIN T_KDS_HEADER b ON a.BILL_NO=b.BILL_NO "
                                + " LEFT JOIN m_global c ON a.MENU_ITEM_CODE = c.CODE AND c.COND ='ITEM' WHERE a.item_status='P' "
                                + " AND b.ASSEMBLY_LINE_CODE ='" + linePos + "' "
                                + " ORDER BY to_number(kds_no) ";

                return jdbcTemplate.query(pendingQuery, new DynamicRowMapper());
        }

        @Override
        public List<Map<String, Object>> historyAssembly() {
                String transDate = KdsService.dateformatDDMMMYYYY.format(this.kdsService.getAppDate());
                String historyAssemblyQuery = "SELECT TKH.OUTLET_CODE, TKH.POS_CODE, TKH.KDS_NO, TKH.BILL_NO, "
                                + "       TKH.DAY_SEQ, TKH.TRANS_DATE, MG3.DESCRIPTION ORDER_TYPE_DESC, "
                                + "       TKH.ORDER_TYPE, TKH.TRANS_TYPE BILL_TRANS_TYPE, NVL(TKH.NOTES, '') NOTES "
                                + " FROM T_KDS_HEADER TKH "
                                + " LEFT JOIN M_GLOBAL MG3 ON TKH.ORDER_TYPE = MG3.CODE AND MG3.COND = 'ORDER_TYPE' "
                                + " WHERE TKH.OUTLET_CODE = '"+outletCode+"' AND TKH.ASSEMBLY_STATUS = 'AF' "
                                + "        AND TKH.ASSEMBLY_LINE_CODE = '"+linePos+"' AND TKH.TRANS_DATE='" + transDate + "' "
                                + " ORDER BY to_number(TKH.KDS_NO) DESC, TKH.START_TIME, TKH.BILL_NO ";

                return jdbcTemplate.query(historyAssemblyQuery, new DynamicRowMapper());
        }
}
