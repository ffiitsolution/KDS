package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dto.KdsHeaderRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;

@Repository
public interface AssemblyDao {
    /**
     * Retrieves and displays the queue orders with the status "active" or those that are not yet completed.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> queueOrder();

    /**
     * Mark the queue orders to done assembly. update field from T_KDS_HEADER
     * <pre>
     * ASSEMBLY_STATUS= 'AF'
     * ASSEMBLY_END_TIME= TIMESTAMP
     * DISPATCH_STATUS= 'DP'
     * DISPATCH_START_TIME= TIMESTAMP
     * PICKUP_START_TIME= TIMESTAMP
     * FINISH_TIME= TIMESTAMP
     * DATE_UPD= TIMESTAMP
     * TIME_UPD= TIMESTRING
     * USER_UPD= NULL
     * </pre>
     * @return A Object KdsHeaderRequest.
     *         <p>Note: KdsHeaderRequest define in package <pre>com.ffi.api.kds.dto.KdsHeaderRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public KdsHeaderRequest doneAssembly(KdsHeaderRequest e);

    /**
     * Mark item ready to create in supply base. update field from T_KDS_ITEM_DETAIL
     * <pre>
     * ITEM_FLOW= 'B'
     * ITEM_STATUS= 'P'
     * DATE_UPD= TIMESTAMP
     * TIME_UPD= TIMESTRING
     * USER_UPD= NULL
     * </pre>
     * @return A Object PrepareItemAssembyRequest.
     *         <p>Note: PrepareItemAssembyRequest define in package <pre>com.ffi.api.kds.dto.PrepareItemAssembyRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public PrepareItemSupplyBaseRequest prepareItemSupplyBase(PrepareItemSupplyBaseRequest e);

    /**
     * Retrieves and displays the queue pending orders where status kds_item_detail is 'P'.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> queuePending();


    /**
     * Retrieves and displays the history orders in assembly.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> historyAssembly();


    /**
     * Get one Order full data
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public Map<String, Object> getOrder(KdsHeaderRequest request);

}
