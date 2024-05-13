/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.kds.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.dto.KdsHeaderRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.AssemblyService;

/**
 *
 * @author KDS Backend Teams
 */
@RestController
@RequestMapping("/assembly")
public class AssemblyController {

    private final AssemblyService assemblyService;

    public AssemblyController(final AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @GetMapping(value = "/queue")
    public @ResponseBody ResponseEntity<?> queueOrder() {
        return ResponseEntity.ok(assemblyService.queueOrder());
    }

    @PutMapping(value = "/done")
    public @ResponseBody ResponseEntity<?> doneAssembly(@RequestBody @Valid KdsHeaderRequest kds) {
        return ResponseEntity.ok(assemblyService.doneAssembly(kds));
    }

    @PutMapping(value = "/prepare-item-supply-base")
    public @ResponseBody ResponseEntity<?> prepareItemSupplyBase(@RequestBody @Valid PrepareItemSupplyBaseRequest itemKds) {
        return ResponseEntity.ok(assemblyService.prepareItemSupplyBase(itemKds));
    }

    @GetMapping(value = "/queue-pending")
    public @ResponseBody ResponseEntity<?> queuePending() {
        return ResponseEntity.ok(assemblyService.queuePending());
    }

    @GetMapping(value = "/history")
    public @ResponseBody ResponseEntity<?> historyAssembly() {
        return ResponseEntity.ok(assemblyService.historyAssembly());
    }

    @GetMapping(value = "/get-order")
    public @ResponseBody ResponseEntity<?> getOrder(@RequestBody @Valid KdsHeaderRequest request) {
        return ResponseEntity.ok(assemblyService.getOrder(request));
    }
}
