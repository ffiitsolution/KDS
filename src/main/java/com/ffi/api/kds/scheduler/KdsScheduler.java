package com.ffi.api.kds.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ffi.api.kds.service.KdsService;
import com.ffi.api.kds.service.SocketTriggerService;

@Service
public class KdsScheduler {

    private String linePos;
    @Value("${app.outletCode}")
    private String outletCode;

    // assembly
    private Integer assemblyQueueOrder = null;

    // supplybase
    private Integer supplyBaseFriedQueueOrder = null;
    private Integer supplyBaseBurgerQueueOrder = null;
    private Integer supplyBasePastaQueueOrder = null;

    // drink
    private Integer drinkBibQueueOrder = null;
    private Integer drinkIceCreamQueueOrder = null;
    private Integer drinkOtherQueueOrder = null;

    // pickup
    private Integer pickupAfterAssemblyStatus = null;
    private Integer pickupServeStatus = null;
    private Integer pickupClaimUnclaimStatus = null;

    private final SocketTriggerService socketTriggerService;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KdsService kdsService;

    public KdsScheduler(final SocketTriggerService socketTriggerService,
            final NamedParameterJdbcTemplate jdbcTemplate,
            final @Value("${app.line.pos}") String linePos,
            final KdsService kdsService) {
        this.socketTriggerService = socketTriggerService;
        this.jdbcTemplate = jdbcTemplate;
        this.linePos = linePos;
        this.kdsService = kdsService;
    }

    @Scheduled(fixedDelay = 1000)
    public void countAssemblyQueueOrder() {

        String countQueueQuery = "SELECT COALESCE (count(*), 0) FROM T_KDS_HEADER TKH JOIN T_KDS_ITEM TKHI ON TKH.OUTLET_CODE = TKHI.OUTLET_CODE "
                + " AND TKH.POS_CODE = TKHI.POS_CODE AND TKH.DAY_SEQ = TKHI.DAY_SEQ AND TKH.BILL_NO = TKHI.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL TKHID ON TKH.OUTLET_CODE = TKHID.OUTLET_CODE AND TKH.POS_CODE = TKHID.POS_CODE "
                + " AND TKH.DAY_SEQ = TKHID.DAY_SEQ AND TKH.BILL_NO = TKHID.BILL_NO AND TKHI.ITEM_SEQ = TKHID.ITEM_SEQ "
                + " LEFT JOIN M_GLOBAL MG ON TKHI.MENU_ITEM_CODE = MG.CODE AND MG.COND = 'ITEM' "
                + " WHERE TKH.OUTLET_CODE = '" + outletCode
                + "' AND TKH.ASSEMBLY_STATUS = 'AQ' AND MG.VALUE NOT IN ('99') AND TKH.ASSEMBLY_LINE_CODE = '" + linePos+ "' ";
        Integer countQueueResult = jdbcTemplate.queryForObject(countQueueQuery, new HashMap<>(), Integer.class);
        if (assemblyQueueOrder == null) {
            assemblyQueueOrder = countQueueResult;
            System.out.println("LOG : initial message, refresh assembly..");
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(assemblyQueueOrder, countQueueResult)) {
            System.out.println("LOG : new order detected, refresh assembly..");
            assemblyQueueOrder = countQueueResult;
            this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countSupplyBaseFried() {
        if (linePos.equals("3")) return;
        String countSBFriedQuery = "SELECT COALESCE (COUNT(*), 0) FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 11 AND D.STATUS =  'A' "
                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode + "' AND C.ASSEMBLY_LINE_CODE='"
                + linePos + "'";

        Integer countQueueResult = jdbcTemplate.queryForObject(countSBFriedQuery, new HashMap<>(), Integer.class);
        if (supplyBaseFriedQueueOrder == null) {
            supplyBaseFriedQueueOrder = countQueueResult;
            System.out.println("LOG : initial message, refresh supplybase fried..");
            this.socketTriggerService.refreshSupplyBaseFried(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(supplyBaseFriedQueueOrder, countQueueResult)) {
            System.out.println("LOG : supplybase fried detected, refresh supplybase fried..");
            supplyBaseFriedQueueOrder = countQueueResult;
            this.socketTriggerService.refreshSupplyBaseFried(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countSupplyBaseBurger() {
        if (linePos.equals("3")) return;
        String countSBBurgerQuery = "SELECT COALESCE (COUNT(*), 0) FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 12 AND D.STATUS =  'A' "
                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode + "' AND C.ASSEMBLY_LINE_CODE='"
                + linePos + "'";
        Integer countQueueResult = jdbcTemplate.queryForObject(countSBBurgerQuery, new HashMap<>(), Integer.class);
        if (supplyBaseBurgerQueueOrder == null) {
            supplyBaseBurgerQueueOrder = countQueueResult;
            System.out.println("LOG : initial message, refresh supplybase burger..");
            this.socketTriggerService.refreshSupplyBaseBurger(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(supplyBaseBurgerQueueOrder, countQueueResult)) {
            System.out.println("LOG : supplybase burger detected, refresh supplybase burger..");
            supplyBaseBurgerQueueOrder = countQueueResult;
            this.socketTriggerService.refreshSupplyBaseBurger(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countSupplyBasePasta() {
        if (linePos.equals("3")) return;
        String countSBPastaQuery = "SELECT COALESCE (COUNT(*), 0) FROM T_KDS_ITEM_DETAIL A "
                + " LEFT JOIN T_KDS_HEADER C ON A.BILL_NO = C.BILL_NO "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ AND A.TRANS_DATE = C.TRANS_DATE "
                + " JOIN M_GLOBAL D ON A.MENU_ITEM_CODE = D.CODE AND D.COND = 'ITEM' AND D.VALUE = 13 AND D.STATUS =  'A' "
                + " WHERE A.ITEM_STATUS = 'P' AND A.OUTLET_CODE = '" + outletCode + "' AND C.ASSEMBLY_LINE_CODE='"+ linePos + "' ";
        Integer countQueueResult = jdbcTemplate.queryForObject(countSBPastaQuery, new HashMap<>(), Integer.class);
        if (supplyBasePastaQueueOrder == null) {
            supplyBasePastaQueueOrder = countQueueResult;
            System.out.println("LOG : initial message, refresh supplybase pasta..");
            this.socketTriggerService.refreshSupplyBasePasta(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(supplyBasePastaQueueOrder, countQueueResult)) {
            System.out.println("LOG : supplybase pasta detected, refresh supplybase pasta..");
            supplyBasePastaQueueOrder = countQueueResult;
            this.socketTriggerService.refreshSupplyBasePasta(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countDrinkBibQueueOrder() {
        if (linePos.equals("3")) return;
        String countDrinkBibQueueOrderQuery = "SELECT COALESCE(COUNT(*), 0) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'D' AND ITEM_STATUS <> 'F'";
        Integer countDrinkBibQueueOrderResult = jdbcTemplate.queryForObject(countDrinkBibQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkBibQueueOrder == null) {
            drinkBibQueueOrder = countDrinkBibQueueOrderResult;
            System.out.println("LOG : initial message, refresh drink bib..");
            this.socketTriggerService.refreshDrinkBib(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(this.drinkBibQueueOrder, countDrinkBibQueueOrderResult)) {
            System.out.println("LOG : bib drink detected, refresh drink bib..");
            this.drinkBibQueueOrder = null;
            this.drinkBibQueueOrder = countDrinkBibQueueOrderResult;
            this.socketTriggerService.refreshDrinkBib(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countDrinkIceCreamQueueOrder() {
        if (linePos.equals("3")) return;

        String countDrinkIceCreamQueueOrderQuery = "SELECT COALESCE(COUNT(*),0) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'O' AND ITEM_STATUS <> 'F'";
        Integer countDrinkIceCreamQueueOrderResult = jdbcTemplate.queryForObject(countDrinkIceCreamQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkIceCreamQueueOrder == null) {
            drinkIceCreamQueueOrder = countDrinkIceCreamQueueOrderResult;
            System.out.println("LOG : initial message, refresh drink ice cream..");
            this.socketTriggerService.refreshDrinkIceCream(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(drinkIceCreamQueueOrder, countDrinkIceCreamQueueOrderResult)) {
            System.out.println("LOG : ice cream drink detected, refresh drink ice cream..");
            drinkIceCreamQueueOrder = countDrinkIceCreamQueueOrderResult;
            this.socketTriggerService.refreshDrinkIceCream(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void otherQueueOrder() {
        if (linePos.equals("3")) return;
        String countDrinkOtherQueueOrderQuery = "SELECT COALESCE(COUNT(*), 0) "
                + " FROM T_KDS_HEADER A "
                + " JOIN T_KDS_ITEM B ON A.OUTLET_CODE = B.OUTLET_CODE AND A.POS_CODE = B.POS_CODE "
                + " AND A.DAY_SEQ = B.DAY_SEQ AND A.BILL_NO = B.BILL_NO "
                + " JOIN T_KDS_ITEM_DETAIL C ON A.OUTLET_CODE = C.OUTLET_CODE "
                + " AND A.POS_CODE = C.POS_CODE AND A.DAY_SEQ = C.DAY_SEQ "
                + " AND A.BILL_NO = C.BILL_NO AND B.ITEM_SEQ = C.ITEM_SEQ "
                + " WHERE A.ASSEMBLY_LINE_CODE = '" + linePos + "' "
                + " AND A.OUTLET_CODE = '" + outletCode + "' AND C.ITEM_FLOW = 'I' AND ITEM_STATUS <> 'F'";

        Integer countDrinkOtherOrderResult = jdbcTemplate.queryForObject(countDrinkOtherQueueOrderQuery,
                new HashMap<>(), Integer.class);
        if (drinkOtherQueueOrder == null) {
            drinkOtherQueueOrder = countDrinkOtherOrderResult;
            System.out.println("LOG : initial message, refresh drink other..");
            this.socketTriggerService.refreshDrinkOther(UUID.randomUUID().toString());
            return;
        }
        if (!Objects.equals(drinkOtherQueueOrder, countDrinkOtherOrderResult)) {
            System.out.println("LOG : other drink detected, refresh drink other..");
            drinkOtherQueueOrder = countDrinkOtherOrderResult;
            this.socketTriggerService.refreshDrinkOther(UUID.randomUUID().toString());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void countPickupQueue() {
        Date endDate = kdsService.getAppDate();
        String endDateString = KdsService.dateformatDDMMMYYYY.format(endDate);
        endDate = DateUtils.addDays(endDate, -7);
        String startDateString = KdsService.dateformatDDMMMYYYY.format(endDate);

        // count after assembly
        String countAfterAssemblyStatus = "SELECT COALESCE (COUNT(*),0) FROM T_KDS_HEADER tkh WHERE ASSEMBLY_STATUS <> 'AQ' "
                + " AND DISPATCH_STATUS = 'DP' AND PICKUP_STATUS=NULL AND ASSEMBLY_LINE_CODE = '" + linePos
                + "' AND OUTLET_CODE = '" + outletCode + "' ";
        Integer countPickupAfterAssemblyResult = jdbcTemplate.queryForObject(countAfterAssemblyStatus,
                new HashMap<>(), Integer.class);

        // count serve status
        String countServeStatus = "SELECT COALESCE (COUNT(*),0) FROM T_KDS_HEADER tkh WHERE ASSEMBLY_STATUS <> 'AQ' "
                + " AND DISPATCH_STATUS = 'DF' AND PICKUP_STATUS='SRV' AND ASSEMBLY_LINE_CODE = '" + linePos
                + "' AND OUTLET_CODE = '" + outletCode + "' ";
        Integer countPickupServeResult = jdbcTemplate.queryForObject(countServeStatus,
                new HashMap<>(), Integer.class);

        String countClaimUnclaimStatus = "SELECT COALESCE (COUNT(*),0) FROM T_KDS_HEADER tkh WHERE ASSEMBLY_STATUS <> 'AQ' "
                + " AND PICKUP_STATUS IN ('CLM', 'UCL') AND ASSEMBLY_LINE_CODE = '" + linePos + "' AND OUTLET_CODE = '"
                + outletCode + "' "
                + " AND TKH.TRANS_DATE BETWEEN '" + startDateString + "' AND '" + endDateString + "' ";
        Integer countPickupClaimUnclaimResult = jdbcTemplate.queryForObject(countClaimUnclaimStatus,
                new HashMap<>(), Integer.class);

        if (pickupAfterAssemblyStatus == null && pickupServeStatus == null && pickupClaimUnclaimStatus == null) {
            pickupAfterAssemblyStatus = countPickupAfterAssemblyResult;
            pickupServeStatus = countPickupServeResult;
            pickupClaimUnclaimStatus = countPickupClaimUnclaimResult;
            System.out.println("LOG : initial message, refresh pickup..");
            this.socketTriggerService.refreshPickup(UUID.randomUUID().toString());
            return;
        }

        if (!Objects.equals(pickupAfterAssemblyStatus, countPickupAfterAssemblyResult)
                || !Objects.equals(pickupServeStatus, countPickupServeResult)
                || !Objects.equals(pickupClaimUnclaimStatus, countPickupClaimUnclaimResult)) {
            System.out.println("LOG : pickup after assembly detected, refresh pickup..");
            pickupAfterAssemblyStatus = countPickupAfterAssemblyResult;
            pickupServeStatus = countPickupServeResult;
            pickupClaimUnclaimStatus = countPickupClaimUnclaimResult;
            this.socketTriggerService.refreshPickup(UUID.randomUUID().toString());
            return;
        }
    }
}
