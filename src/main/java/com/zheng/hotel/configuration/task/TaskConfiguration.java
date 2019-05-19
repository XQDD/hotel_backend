package com.zheng.hotel.configuration.task;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

//异步操作相关配置
@Component
public class TaskConfiguration {
    //spring 线程池
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        return executor;
    }
}
