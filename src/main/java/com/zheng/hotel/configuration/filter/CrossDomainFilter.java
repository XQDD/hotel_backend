package com.zheng.hotel.configuration.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//跨域处理过滤器，由于浏览器地同源策略（前后台分离测试时环境不同），所以后台做跨域处理，方便编码阶段测试
@WebFilter
@Component
@RequiredArgsConstructor
public class CrossDomainFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置请求头，让后台接口支持跨域
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,HEAD,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "range,Origin,Accept, X-Requested-With, Content-Type,Authorization,x-token,airbaggySessionId");
        //options请求直接返回（有些网络请求框架如axios跨域时会先发一个options请求判断后台是否支持跨域）
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.getOutputStream().write("OK".getBytes(StandardCharsets.UTF_8));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
