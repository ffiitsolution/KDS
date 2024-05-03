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

    public void refreshSupplyBase(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/supply-base", message);
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
}
