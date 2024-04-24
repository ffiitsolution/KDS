package com.ffi.api.kds.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dao.DrinkDao;

@Repository
public class DrinkDaoImpl implements DrinkDao {

    @Override
    public List<Map<String, Object>> bibQueueDrink() {
        return null;
    }

    @Override
    public List<Map<String, Object>> iceCreamQueueDrink() {
        return null;
    }

    @Override
    public List<Map<String, Object>> otherQueueDrink() {
        return null;
    }

}
