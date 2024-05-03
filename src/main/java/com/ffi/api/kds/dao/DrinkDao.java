package com.ffi.api.kds.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ffi.api.kds.dto.DoneDrinkRequest;

@Repository
public interface DrinkDao {
    
    /**
     * Retrieves and displays the queue bib orders in drink.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> bibQueueOrder();

    /**
     * Retrieves and displays the queue ice cream orders in drink.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> iceCreamQueueOrder();

    /**
     * Retrieves and displays the queue other orders in drink.
     * 
     * @return A Map where the keys are strings representing the order details and the values are objects.
     *         <p>Note: The structure of the map should be defined as <pre>Map&lt;String, Object&gt;</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public List<Map<String, Object>> otherQueueOrder();

    /**
     * Mark item ready to serve drink.
     * 
     * @return A Object DoneDrinkRequest.
     *         <p>Note: DoneDrinkRequest define in package <pre>com.ffi.api.kds.dto.DoneDrinkRequest</pre>
     * @author <a href="https://www.linkedin.com/in/muhammad-dani-ramadhan-645356203">Dani Ramadhan</a> 
     */
    public DoneDrinkRequest doneDrink(DoneDrinkRequest e);
    
} 