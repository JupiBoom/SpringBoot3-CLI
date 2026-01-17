package com.rosy.volunteer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.rosy.volunteer.mapper")
@ComponentScan(basePackages = {"com.rosy.volunteer", "com.rosy.common"})
public class VolunteerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VolunteerApplication.class, args);
    }
}
