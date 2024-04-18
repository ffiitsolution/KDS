package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

public interface KdsDao {
    /**
     * Retrieves and displays the queue orders with the status "active" or those that are not yet completed.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> queueOrder();



}
