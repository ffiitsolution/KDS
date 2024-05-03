package com.ffi.api.kds.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.DrinkDao;
import com.ffi.api.kds.dto.DoneDrinkRequest;

@Service
public class DrinkService {

    private final DrinkDao drinkDao;

    public DrinkService(DrinkDao drinkDao) {
        this.drinkDao = drinkDao;
    }

    public List<Map<String, Object>> bibQueueOrder() {
        return drinkDao.bibQueueOrder();
    }

    public List<Map<String, Object>> iceCreamQueueOrder() {
        return drinkDao.iceCreamQueueOrder();
    }

    public List<Map<String, Object>> otherQueueOrder() {
        return drinkDao.otherQueueOrder();
    }

    public DoneDrinkRequest doneDrink(DoneDrinkRequest e) {
        return drinkDao.doneDrink(e);
    }
}
