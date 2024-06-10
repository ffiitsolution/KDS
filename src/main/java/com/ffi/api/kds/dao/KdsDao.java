package com.ffi.api.kds.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface KdsDao {

    /**
     * get app date base on outlet_code define in properties
     * @return <pre>Date</pre>
     */
    public Date getAppDate();
    
    /**
     * provide api to get kds treshold from m_global where cond = 'KDS'
     * @return <pre>Date</pre>
     */
    public List<Map<String, Object>> kdsTreshholdSetting();
}
