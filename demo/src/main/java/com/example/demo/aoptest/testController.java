package com.example.demo.aoptest;

import com.example.demo.aoptest.aspect.MyAspect;
import com.example.demo.aoptest.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {

    @Autowired
    public HelloService helloService;

    @RequestMapping("/say")
    public void sayHello(String name) {
        helloService.sayHello(name);
        return;
    }

    @Bean
    public MyAspect initMyAspect() {
        return new MyAspect();
    }

}
