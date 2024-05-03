package com.ffi.api.kds.scheduler;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ffi.api.kds.service.SocketTriggerService;

@Service
public class KdsScheduler {

    private String linePos;
    private String orderType;
    @Value("${app.outletCode}")
    private String outletCode;

    private Integer assemblyQueueOrder = null;
    private Integer drinkBibQueueOrder = null;
    private Integer drinkIceCreamQueueOrder = null;
    private Integer drinkOtherQueueOrder = null;

    private final SocketTriggerService socketTriggerService;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public KdsScheduler(final SocketTriggerService socketTriggerService,
            final NamedParameterJdbcTemplate jdbcTemplate,
            final @Value("${app.line.pos}") String linePos) {
        this.socketTriggerService = socketTriggerService;
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
        if (Objects.equals("0", linePos)) {
            orderType = "ETA";
        } else if (Objects.equals("3", linePos)) {
            orderType = "DRT";
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countAssemblyQueueOrder() {
        String countQueueQuery = "SELECT count(*) FROM T_KDS_HEADER TKH JOIN T_KDS_ITEM TKHI ON TKH.OUTLET_CODE = TKHI.OUTLET_CODE "
                + " AND TKH.POS_CODE = TKHI.POS_CODE AND TKH.DAY_SEQ = TKHI.DAY_SEQ AND TKH.BILL_NO = TKHI.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL TKHID ON TKH.OUTLET_CODE = TKHID.OUTLET_CODE AND TKH.POS_CODE = TKHID.POS_CODE "
                + " AND TKH.DAY_SEQ = TKHID.DAY_SEQ AND TKH.BILL_NO = TKHID.BILL_NO AND TKHI.ITEM_SEQ = TKHID.ITEM_SEQ "
                + " LEFT JOIN M_GLOBAL MG ON TKHI.MENU_ITEM_CODE = MG.CODE AND MG.COND = 'ITEM' "
                + " WHERE TKH.OUTLET_CODE = '" + outletCode
                + "' AND TKH.ASSEMBLY_STATUS = 'AQ' AND MG.VALUE NOT IN ('99') AND TKH.ASSEMBLY_LINE_CODE = '" + linePos
                + "'";

        Integer countQueueResult = jdbcTemplate.queryForObject(countQueueQuery, new HashMap<>(), Integer.class);
        if (assemblyQueueOrder == null) {
            assemblyQueueOrder = countQueueResult;
            return;
        }
        if (!Objects.equals(assemblyQueueOrder, countQueueResult)) {
            System.out.println("LOG : new order detected, refresh assembly..");
            assemblyQueueOrder = countQueueResult;
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countDrinkBibQueueOrder() {
        String countDrinkBibQueueOrderQuery = "SELECT COUNT(*) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' AND A.ORDER_TYPE = '" + orderType + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'D' AND ITEM_STATUS <> 'F'";

        Integer countDrinkBibQueueOrderResult = jdbcTemplate.queryForObject(countDrinkBibQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkBibQueueOrder == null) {
            drinkBibQueueOrder = countDrinkBibQueueOrderResult;
            return;
        }
        if (!Objects.equals(drinkBibQueueOrder, countDrinkBibQueueOrderResult)) {
            System.out.println("LOG : bib drink detected, refresh drink bib..");
            drinkBibQueueOrder = countDrinkBibQueueOrderResult;
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
        }
    }

    public void countDrinkIceCreamQueueOrder() {
        String countDrinkIceCreamQueueOrderQuery = "SELECT COUNT(*) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' AND A.ORDER_TYPE = '" + orderType + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'O' AND ITEM_STATUS <> 'F'";
        Integer countDrinkIceCreamQueueOrderResult = jdbcTemplate.queryForObject(countDrinkIceCreamQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkIceCreamQueueOrder == null) {
            drinkIceCreamQueueOrder = countDrinkIceCreamQueueOrderResult;
            return;
        }
        if (!Objects.equals(drinkBibQueueOrder, countDrinkIceCreamQueueOrderResult)) {
            System.out.println("LOG : ice cream drink detected, refresh drink ice cream..");
            drinkBibQueueOrder = countDrinkIceCreamQueueOrderResult;
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
        }
    }

    public void otherQueueOrder() {
        String countDrinkOtherQueueOrderQuery = "SELECT COUNT(*) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' AND A.ORDER_TYPE = '" + orderType + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'I' AND ITEM_STATUS <> 'F'";

        Integer countDrinkOtherOrderResult = jdbcTemplate.queryForObject(countDrinkOtherQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkOtherQueueOrder == null) {
            drinkOtherQueueOrder = countDrinkOtherOrderResult;
            return;
        }
        if (!Objects.equals(drinkOtherQueueOrder, countDrinkOtherOrderResult)) {
            System.out.println("LOG : other drink detected, refresh drink other..");
            drinkOtherQueueOrder = countDrinkOtherOrderResult;
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
        }
    }
}
