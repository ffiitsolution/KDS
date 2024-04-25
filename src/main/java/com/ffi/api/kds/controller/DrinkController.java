package com.ffi.api.kds.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.api.kds.dto.PrepareItemSupplyBaseRequest;
import com.ffi.api.kds.service.DrinkService;

/**
 *
 * @author KDS Backend Teams
 */
@RestController
@RequestMapping("/drink")
public class DrinkController {
    
    private final DrinkService drinkService;

    public DrinkController(final DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping("/bib-queue")
    public @ResponseBody ResponseEntity<?> bibQueueOrder() {
        return ResponseEntity.ok(drinkService.bibQueueOrder());
    }

    @GetMapping("/ice-cream-queue")
    public @ResponseBody ResponseEntity<?> iceCreamQueueOrder() {
        return ResponseEntity.ok(drinkService.iceCreamQueueOrder());
    }

    @GetMapping("/other-queue")
    public @ResponseBody ResponseEntity<?> otherQueueOrder() {
        return ResponseEntity.ok(drinkService.otherQueueOrder());
    }

    @PutMapping(value = "/done-drink")
    public @ResponseBody ResponseEntity<?> prepareItemSupplyBase(@RequestBody PrepareItemSupplyBaseRequest itemKds) {
        return ResponseEntity.ok(drinkService.doneDrink(itemKds));
    }
}
