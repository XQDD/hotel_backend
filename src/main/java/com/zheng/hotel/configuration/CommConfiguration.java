package com.zheng.hotel.configuration;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//通用配置
@Configuration
public class CommConfiguration {
    //配置Spring boot支持在查询参数中加{}[]字符。
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        // 修改内置的 tomcat 容器配置
        TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
        tomcatServlet.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return tomcatServlet;
    }

}
