package com.example.demo;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication(scanBasePackages = {"com.example.demo.curator"})
public class DemoApplication implements ApplicationContextAware {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}
}
