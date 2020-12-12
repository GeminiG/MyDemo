package com.example.demo.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/async")
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @PutMapping("/page")
    public void asyncPage() {
        System.out.println("请求线程名：【" + Thread.currentThread().getName() + "】");
        for (int i = 0; i < 15; i++) {
            asyncService.generateReport();
        }
    }
}
