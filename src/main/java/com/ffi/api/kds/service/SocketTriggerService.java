package com.ffi.api.kds.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketTriggerService {

    private SimpMessagingTemplate simpMessagingTemplate;

    public SocketTriggerService(final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void refreshAssembly(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/assembly", message);
    }

    public void refreshAssemblyPending(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/pending", message);
    }

    public void refreshSupplyBaseFried(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/supply-base-fried", message);
    }

    public void refreshSupplyBaseBurger(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/supply-base-burger", message);
    }

    public void refreshSupplyBasePasta(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/supply-base-pasta", message);
    }

    public void refreshDrinkBib(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/drink-bib", message);
    }
    public void refreshDrinkIceCream(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/drink-icecream", message);
    }
    public void refreshDrinkOther(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/drink-other", message);
    }

    public void refreshPickup(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/pickup", message);
    }
}
