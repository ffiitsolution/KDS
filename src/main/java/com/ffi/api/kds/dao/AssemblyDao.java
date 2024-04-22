package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dto.DoneAssemblyRequest;
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
     * Mark the queue orders to done assembly.
     * 
     * @return A Object DoneAssemblyRequest.
     *         <p>Note: DoneAssemblyRequest define in package <pre>com.ffi.api.kds.dto.DoneAssemblyRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public DoneAssemblyRequest doneAssembly(DoneAssemblyRequest e);

    /**
     * Mark the queue orders to done assembly.
     * 
     * @return A Object PrepareItemAssembyRequest.
     *         <p>Note: PrepareItemAssembyRequest define in package <pre>com.ffi.api.kds.dto.PrepareItemAssembyRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public PrepareItemSupplyBaseRequest prepareItemSupplyBase(PrepareItemSupplyBaseRequest e);
}
