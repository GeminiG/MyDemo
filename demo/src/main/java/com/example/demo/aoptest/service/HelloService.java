package com.example.demo.aoptest.service;

import org.springframework.stereotype.Service;

@Service
public interface HelloService {
    void sayHello(String name);
}
