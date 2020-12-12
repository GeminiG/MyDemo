package com.example.demo.aoptest.service.impl;

import com.example.demo.aoptest.service.HelloService;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        if (name == null || name.trim() == "") {
            throw new RuntimeException("parameter is null");
        }
        System.out.println("hello " + name);
    }
}
