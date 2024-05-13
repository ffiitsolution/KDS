package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.AssemblyDao;
import com.ffi.api.kds.dto.KdsHeaderRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;

@Service
public class AssemblyService {
    private final AssemblyDao assemblyDao;

    public AssemblyService(AssemblyDao assemblyDao) {
        this.assemblyDao = assemblyDao;
    }

    public List<Map<String, Object>> queueOrder() {
        return assemblyDao.queueOrder();
    }

    public KdsHeaderRequest doneAssembly(KdsHeaderRequest kds) {
        return assemblyDao.doneAssembly(kds);
    }

    public PrepareItemSupplyBaseRequest prepareItemSupplyBase(PrepareItemSupplyBaseRequest itemKds) {
        return assemblyDao.prepareItemSupplyBase(itemKds);
    }

    public List<Map<String, Object>> queuePending() {
        return assemblyDao.queuePending();
    }

    public List<Map<String, Object>> historyAssembly() {
        return assemblyDao.historyAssembly();
    }

    public Map<String, Object> getOrder( KdsHeaderRequest request) {
        return assemblyDao.getOrder(request);
    }
}
