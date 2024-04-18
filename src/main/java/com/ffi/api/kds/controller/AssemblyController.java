/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.kds.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.service.KdsService;

/**
 *
 * @author IT
 */
@RestController
@RequestMapping("/assembly")
public class AssemblyController {

    private final KdsService kdsService;
    public AssemblyController(final KdsService kdsService) {
        this.kdsService = kdsService;
    }

    @GetMapping(value = "/queue")
    public @ResponseBody List<Map<String, Object>> queueOrder() {
        return kdsService.queueOrder();
    }
}
