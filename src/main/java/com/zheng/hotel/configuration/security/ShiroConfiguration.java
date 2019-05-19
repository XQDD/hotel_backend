package com.zheng.hotel.configuration.security;

import com.zheng.hotel.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;


//shiro配置
@Configuration
@RequiredArgsConstructor
public class ShiroConfiguration {


    private final SysUserService sysUserService;

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm(sysUserService);
    }

    /**
     * SecurityManager : 安全管理器，负责所有与安全相关的操作，是Shiro的核心，负责与Shiro的其他组件进行交互
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        return new DefaultWebSecurityManager(shiroRealm());
    }


    /**
     * 设置shiro 拦截器规则
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        var shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义shiro拦截器设置
        var filters = new LinkedHashMap<String, Filter>();
        //认证拦截器
        filters.put("authc", new ShiroAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        //url匹配规则配置
        var filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //登录放行
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        filterChainDefinitionMap.put("/user/getCaptcha", "anon");
        //swagger文档放行
        filterChainDefinitionMap.put("/*.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        //其他全部需要认证
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /*
     *集成shiro到spring，启用注解式权限控制
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        var advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


}