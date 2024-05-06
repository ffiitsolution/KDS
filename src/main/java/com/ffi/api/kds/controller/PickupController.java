package com.ffi.api.kds.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.service.PickupService;

/**
 *
 * @author KDS Backend Teams
 */
@RestController
@RequestMapping("/pickup")
public class PickupController {
    private final PickupService pickupService;

    public PickupController(final PickupService pickupService) {
        this.pickupService = pickupService;
    }

    @GetMapping(value = "/queue")
    public @ResponseBody ResponseEntity<?> queuePickup() {
        return ResponseEntity.ok(pickupService.queuePickup());
    }
}
