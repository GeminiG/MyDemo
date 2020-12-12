package com.example.demo.aoptest.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MyAspect {

    @Pointcut("execution(* com.example.demo.aoptest.service.impl.HelloServiceImpl.sayHello(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before() {
        System.out.println("before ...");
    }

    @After("pointCut()")
    public void after() {
        System.out.println("after ...");
    }

    @AfterReturning("pointCut()")
    public void afterReturning() {
        System.out.println("after returning ...");
    }

    @AfterThrowing("pointCut()")
    public void afterThrowing() {
        System.out.println(("after throwing..."));
    }

    @Around("pointCut()")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("around before...");
        jp.proceed();
        System.out.println("around after...");
    }

}
