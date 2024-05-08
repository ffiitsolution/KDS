package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dto.KdsHeaderRequest;

@Repository
public interface PickupDao {
    /**
     * Retrieves and displays the queue pickup.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> queuePickup();

    /**
     * Mark the queue pickup to be serve. update field from T_KDS_HEADER 
     * <pre> 
     * PICKUP_STATUS = 'SRV'
     * USER_UPD= NULL
     * TIME_UPD= TIMESTAMP
     * DATE_UPD= TIMESTAMP
     * DISPATCH_END_TIME=TIMESTAMP
     * PICKUP_START_TIME=TIMESTAMP
     * DISPATCH_STATUS='DF'</pre>
     * 
     * @return A Object KdsHeaderRequest.
     *         <p>Note: KdsHeaderRequest define in package <pre>com.ffi.api.kds.dto.KdsHeaderRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public KdsHeaderRequest servePickup(KdsHeaderRequest request);

    /**
     * Mark the queue pickup to be claim. update field from T_KDS_HEADER 
     * <pre>
     * PICKUP_STATUS='CLM'
     * USER_UPD= NULL
     * TIME_UPD= TIMESTAMP
     * DATE_UPD= TIMESTAMP
     * PICKUP_END_TIME=TIMESTAMP
     * FINISH_TIME=TIMESTAMP</pre>
     * @return A Object KdsHeaderRequest.
     *         <p>Note: KdsHeaderRequest define in package <pre>com.ffi.api.kds.dto.KdsHeaderRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public KdsHeaderRequest claimPickup(KdsHeaderRequest request);

    /**
     * Mark the queue pickup to be unclaim. update field from T_KDS_HEADER 
     * PICKUP_STATUS='UCL'
     * USER_UPD= NULL
     * TIME_UPD= TIMESTAMP
     * DATE_UPD= TIMESTAMP
     * FINISH_TIME=TIMESTAMP
     * @return A Object KdsHeaderRequest.
     *         <p>Note: KdsHeaderRequest define in package <pre>com.ffi.api.kds.dto.KdsHeaderRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public KdsHeaderRequest unclaimPickup(KdsHeaderRequest request);
    
}