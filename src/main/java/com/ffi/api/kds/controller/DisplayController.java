package com.ffi.api.kds.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.service.DisplayService;

/**
 *
 * @author KDS Backend Teams
 */
@RestController
@RequestMapping("/display")
public class DisplayController {
    private final DisplayService displayService;

    public DisplayController(DisplayService displayService) {
        this.displayService = displayService;
    }

    @GetMapping("/ready-to-serve")
    public @ResponseBody ResponseEntity<?> displayReadyToServe() {
        return ResponseEntity.ok(displayService.displayReadyToServe());
    }

    @GetMapping("/waiting-to-serve")
    public @ResponseBody ResponseEntity<?> displayWaitingToServe() {
        return ResponseEntity.ok(displayService.displayWaitingToServe());
    }
}
