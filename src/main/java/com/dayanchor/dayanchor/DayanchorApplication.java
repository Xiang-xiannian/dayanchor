package com.dayanchor.dayanchor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Application entrypoint. 应用启动入口 */
@EnableScheduling
@SpringBootApplication
public class DayanchorApplication {

    /** Bootstrap Spring Boot. 启动 Spring Boot */
    public static void main(String[] args) {
        SpringApplication.run(DayanchorApplication.class, args);
    }
}

