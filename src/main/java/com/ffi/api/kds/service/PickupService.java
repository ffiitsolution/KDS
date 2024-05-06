package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.PickupDao;

@Service
public class PickupService {
    private final PickupDao pickupDao;

    public PickupService(final PickupDao pickupDao) {
        this.pickupDao = pickupDao;
    }

    public List<Map<String, Object>> queuePickup() {
        return this.pickupDao.queuePickup();
    }
}
