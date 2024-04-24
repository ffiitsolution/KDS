package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface DrinkDao {
    
    public List<Map<String, Object>> bibQueueDrink();

    public List<Map<String, Object>> iceCreamQueueDrink();

    public List<Map<String, Object>> otherQueueDrink();
    
} 