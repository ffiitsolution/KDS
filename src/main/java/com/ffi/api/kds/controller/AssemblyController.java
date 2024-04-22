/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.kds.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.dto.DoneAssemblyRequest;
import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.AssemblyService;

/**
 *
 * @author IT
 */
@RestController
@RequestMapping("/assembly")
public class AssemblyController {

    private final AssemblyService assemblyService;
    public AssemblyController(final AssemblyService assemblyService) {
        this.assemblyService = assemblyService;
    }

    @GetMapping(value = "/queue")
    public @ResponseBody List<Map<String, Object>> queueOrder() {
        return assemblyService.queueOrder();
    }

    @PutMapping(value = "/done")
    public @ResponseBody DoneAssemblyRequest doneAssembly(@RequestBody DoneAssemblyRequest kds) {
        return assemblyService.doneAssembly(kds);
    }

    @PutMapping(value = "/prepare-item-supply-base")
    public @ResponseBody PrepareItemSupplyBaseRequest prepareItemSupplyBase(@RequestBody PrepareItemSupplyBaseRequest itemKds) {
        return assemblyService.prepareItemSupplyBase(itemKds);
    }
}
