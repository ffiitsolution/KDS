package com.ffi.api.kds.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface KdsDao {

    public Date getAppDate();
    
    public List<Map<String, Object>> kdsTreshholdSetting();
}
