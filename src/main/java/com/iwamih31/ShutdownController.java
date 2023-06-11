package com.iwamih31;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ShutdownController {
    @PostMapping("/shutdown")
    void shutdownContext() {
        SpringApplication.exit(applicationContext,(() -> 0));
    }

    @Autowired
    private ApplicationContext applicationContext;
}
