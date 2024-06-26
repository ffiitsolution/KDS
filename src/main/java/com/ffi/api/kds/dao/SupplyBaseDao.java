package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;

@Repository
public interface SupplyBaseDao {

    /**
     * Retrieves and displays the queue fried orders in supply base.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> friedQueueOrder();

    /**
     * Retrieves and displays the queue burger orders in supply base.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> burgerQueueOrder();

    /**
     * Retrieves and displays the queue pasta orders in supply base.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> pastaQueueOrder();

    /**
     * Mark item listed in supply base has already done and ready to serve.
     * 
     * @return A Object PrepareItemAssembyRequest.
     *         <p>Note: PrepareItemAssembyRequest define in package <pre>com.ffi.api.kds.dto.PrepareItemAssembyRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public PrepareItemSupplyBaseRequest doneItemSupplyBase(PrepareItemSupplyBaseRequest e);
}