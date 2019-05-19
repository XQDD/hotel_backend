package com.zheng.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//启动jpa审计
@EnableJpaAuditing
public class HotelApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HotelApplication.class);
    }

}
