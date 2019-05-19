package com.zheng.hotel.configuration.security;

import com.alibaba.fastjson.JSON;
import com.zheng.hotel.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

//shiro认证过滤器
@Slf4j
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //登录操作
        if (isLoginRequest(request, response)) {
            return true;
        }
        //其他操作
        else {
            //设置response字符编码
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            //返回错误信息
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().write(JSON.toJSONString(Result.error("请先登录")).getBytes());
            return false;
        }
    }


}
