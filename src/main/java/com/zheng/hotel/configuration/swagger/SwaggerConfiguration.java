package com.zheng.hotel.configuration.swagger;


import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicates;
import com.zheng.hotel.bean.rbac.SysUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.stream.Stream;


@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfiguration {
    private static final boolean enable = true;
    private final Class[] ignoreClasses = new Class[]{
            SysUser.class,
            HttpSession.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            MultipartFile.class,
    };

    @Bean
    public Docket admin() {
        return docker("管理端文档", "com.zheng.hotel.controller");
    }


    private Docket docker(String groupName, String controllerPackage, String... excludePackages) {
        var apis = new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage(controllerPackage));
        if (excludePackages.length > 0) {
            Stream.of(excludePackages).forEach(p -> apis.apis(Predicates.not(RequestHandlerSelectors.basePackage(p))));
        }
        return apis.build()
                .ignoredParameterTypes(ignoreClasses)
                .directModelSubstitute(PageRequest.class, Void.class)
                .apiInfo(new ApiInfoBuilder().build());
    }

}
