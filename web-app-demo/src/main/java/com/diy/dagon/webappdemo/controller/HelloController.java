package com.diy.dagon.webappdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dagon
 * @description: TODO
 * @date 2024/4/7-22:57
 */
@RestController
public class HelloController {


    @GetMapping("/hello")
    public String sayHello() {
        return "hello,nevermore~";
    }

}
