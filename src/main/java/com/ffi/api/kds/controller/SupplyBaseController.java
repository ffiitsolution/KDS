package com.ffi.api.kds.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.service.SupplyBaseService;

@RestController
@RequestMapping("/supply-base")
public class SupplyBaseController {

    private final SupplyBaseService supplyBaseService;

    public SupplyBaseController(final SupplyBaseService supplyBaseService) {
        this.supplyBaseService = supplyBaseService;
    }

    @GetMapping("/fried-queue")
    public @ResponseBody List<Map<String, Object>> friedQueueOrder() {
        return supplyBaseService.friedQueueOrder();
    }

    @GetMapping("/burger-queue")
    public @ResponseBody List<Map<String, Object>> burgerQueueOrder() {
        return supplyBaseService.burgerQueueOrder();
    }

    @GetMapping("/pasta-queue")
    public @ResponseBody List<Map<String, Object>> pastaQueueOrder() {
        return supplyBaseService.pastaQueueOrder();
    }
}
