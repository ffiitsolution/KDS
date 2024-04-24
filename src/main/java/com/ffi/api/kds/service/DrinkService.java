package com.ffi.api.kds.service;

import org.springframework.stereotype.Service;

import com.ffi.api.kds.dao.DrinkDao;

@Service
public class DrinkService {
    private final DrinkDao drinkDao;

    public DrinkService(DrinkDao drinkDao) {
        this.drinkDao = drinkDao;
    }
}
