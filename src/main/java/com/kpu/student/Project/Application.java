package com.kpu.student.Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.kpu.student")
public class Application {
    public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
    }
}

