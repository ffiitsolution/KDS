package com.ffi.api.kds.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.SupplyBaseService;

@RestController
@RequestMapping("/supply-base")
public class SupplyBaseController {

    private final SupplyBaseService supplyBaseService;

    public SupplyBaseController(final SupplyBaseService supplyBaseService) {
        this.supplyBaseService = supplyBaseService;
    }

    @GetMapping("/fried-queue")
    public @ResponseBody ResponseEntity<?> friedQueueOrder() {
        return ResponseEntity.ok(supplyBaseService.friedQueueOrder());
    }

    @GetMapping("/burger-queue")
    public @ResponseBody ResponseEntity<?> burgerQueueOrder() {
        return ResponseEntity.ok(supplyBaseService.burgerQueueOrder());
    }

    @GetMapping("/pasta-queue")
    public @ResponseBody ResponseEntity<?> pastaQueueOrder() {
        return ResponseEntity.ok(supplyBaseService.pastaQueueOrder());
    }

    @PutMapping(value = "/done-supply-base")
    public @ResponseBody ResponseEntity<?> doneItemSupplyBase(@RequestBody PrepareItemSupplyBaseRequest itemKds) {
        return ResponseEntity.ok(supplyBaseService.doneItemSupplyBase(itemKds));
    }
}
