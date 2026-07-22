package com.banking.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String message() {

        return "Welcome to Banking Application";

    }

}