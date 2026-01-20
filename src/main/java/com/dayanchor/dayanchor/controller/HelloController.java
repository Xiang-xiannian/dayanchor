package com.dayanchor.dayanchor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from DayAnchor!";
    }

    @GetMapping("/bye")
    public String goodbye() {
        return "再见，欢迎下次光临！";
    }

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return "你好，" + name + "！";
    }
}
