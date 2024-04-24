package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.SupplyBaseDao;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;

@Service
public class SupplyBaseService {
    private final SupplyBaseDao suppliBaseDao;

    public SupplyBaseService(SupplyBaseDao supplyBaseDao) {
        this.suppliBaseDao = supplyBaseDao;
    }

    public List<Map<String, Object>> friedQueueOrder() {
        return suppliBaseDao.friedQueueOrder();
    }

    public List<Map<String, Object>> burgerQueueOrder() {
        return suppliBaseDao.burgerQueueOrder();
    }

    public List<Map<String, Object>> pastaQueueOrder() {
        return suppliBaseDao.pastaQueueOrder();
    }

    public PrepareItemSupplyBaseRequest doneItemSupplyBase(PrepareItemSupplyBaseRequest e) {
        return suppliBaseDao.doneItemSupplyBase(e);
    }

}
