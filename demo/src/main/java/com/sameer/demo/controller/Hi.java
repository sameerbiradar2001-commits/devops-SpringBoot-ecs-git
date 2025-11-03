package com.sameer.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hi {


    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${message.env:Default Environment}")
    private String envMessage;

    @GetMapping
    public String hi(){
        return "Hi from Sameer — running in " + activeProfile + " (" + envMessage + ")";
    }
}
