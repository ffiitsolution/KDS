package com.ffi.api.kds.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.dto.KdsHeaderRequest;
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

    @PutMapping(value = "/serve")
    public @ResponseBody ResponseEntity<?> servePickup(@RequestBody KdsHeaderRequest request) {
        return ResponseEntity.ok(pickupService.servePickup(request));
    }

    @PutMapping(value = "/claim")
    public @ResponseBody ResponseEntity<?> claimPickup(@RequestBody KdsHeaderRequest request) {
        return ResponseEntity.ok(pickupService.claimPickup(request));
    }

    @PutMapping(value = "/unclaim")
    public @ResponseBody ResponseEntity<?> unclaimPickup(@RequestBody KdsHeaderRequest request) {
        return ResponseEntity.ok(pickupService.unclaimPickup(request));
    }
}