/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.kds.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author IT
 */
@RestController
@RequestMapping
public class KdsController {
    
    @GetMapping
    public Map<String, Object> version() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("VERSION", "ITD FFI 2024 1.0.1");
        return map;
    }
    
    @GetMapping(value = "/version")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("VERSION", "ITD FFI 2024 1.0.1");
        return map;
    }

    @GetMapping(value = "/test-exception")
    public @ResponseBody void testException() {
        throw new RuntimeException("TEST GLOBAL EXCEPTION HANDLER...");
    }
}
