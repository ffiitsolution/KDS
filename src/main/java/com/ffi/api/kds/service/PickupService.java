package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.PickupDao;
import com.ffi.api.kds.dto.KdsHeaderRequest;

@Service
public class PickupService {
    private final PickupDao pickupDao;

    public PickupService(final PickupDao pickupDao) {
        this.pickupDao = pickupDao;
    }

    public List<Map<String, Object>> queuePickup() {
        return this.pickupDao.queuePickup();
    }

    public KdsHeaderRequest servePickup(KdsHeaderRequest request) {
        return this.pickupDao.servePickup(request);
    }

    public KdsHeaderRequest claimPickup(KdsHeaderRequest request) {
        return this.pickupDao.claimPickup(request);
    }

    public KdsHeaderRequest unclaimPickup(KdsHeaderRequest request) {
        return this.pickupDao.unclaimPickup(request);
    }
}
