/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.kds.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.service.SocketTriggerService;

/**
 *
 * @author IT
 */
@RestController
@RequestMapping
public class KdsController {

    @Autowired
    SocketTriggerService socketTriggerService;

    @GetMapping
    public Map<String, Object> version() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("", "ITD FFI 2024 1.0.1");
        return map;
    }

    @GetMapping(value = "/version")
    public @ResponseBody Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("VERSION", "ITD FFI 2024 1.0.1");
        return map;
    }

    @GetMapping(value = "/test-exception")
    public @ResponseBody void testException() {
        throw new RuntimeException("TEST GLOBAL EXCEPTION HANDLER...");
    }

    @GetMapping(value = "/refresh-notif")
    public @ResponseBody void testNotifAssemblyRefresh() {
        this.socketTriggerService.refreshAssembly(UUID.randomUUID().toString()+": testing assembly refresh..");
        this.socketTriggerService.refreshSupplyBaseFried(UUID.randomUUID().toString()+": testing supplybase fried refresh..");
        this.socketTriggerService.refreshSupplyBaseBurger(UUID.randomUUID().toString()+": testing supplybase burger refresh..");
        this.socketTriggerService.refreshSupplyBasePasta(UUID.randomUUID().toString()+": testing supplybase pasta refresh..");
        this.socketTriggerService.refreshDrinkBib(UUID.randomUUID().toString()+": testing drink bib refresh..");
        this.socketTriggerService.refreshDrinkIceCream(UUID.randomUUID().toString()+": testing drink icecream refresh..");
        this.socketTriggerService.refreshDrinkOther(UUID.randomUUID().toString()+": testing drink other refresh..");
    }
}
