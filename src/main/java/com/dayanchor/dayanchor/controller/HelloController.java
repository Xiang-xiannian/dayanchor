package com.dayanchor.dayanchor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Demo endpoints. 示例接口 */
@RestController
public class HelloController {

    /** Hello. 问候 */
    @GetMapping("/hello")
    public String hello() {
        return "Hello from DayAnchor!";
    }

    /** Goodbye. 告别 */
    @GetMapping("/bye")
    public String goodbye() {
        return "再见，欢迎下次光临！";
    }

    /** Greet by name. 按名字问候 */
    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return "你好，" + name + "！";
    }
}

