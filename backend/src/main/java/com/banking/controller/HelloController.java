package com.banking.controller;

import com.banking.service.HelloService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {

        this.helloService = helloService;

    }

    @GetMapping("/hello")
    public String hello() {

        return helloService.message();

    }

    @GetMapping("/health")
    public String health() {

        return "Application Running";

    }

}