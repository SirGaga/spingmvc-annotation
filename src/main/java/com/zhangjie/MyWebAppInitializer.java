package com.zhangjie;

import com.zhangjie.config.AppConfig;
import com.zhangjie.config.RootConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    // 获取根容器的配置类：（类似于Spirng的配置文件），形成父容器
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }
    // 获取web容器的配置类（SpringMVC配置文件），形成子容器
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{AppConfig.class};
    }
    // 获取DispatcherServlet的映射信息
    // "/"的意思是拦截所有请求（包括静态资源：css，js，png等，但是不包括jsp，/*是真正的拦截所有请求【jsp页面是由tomcat的JSP引擎来进行解析的】）
    // 可以配置成/*.do来拦截 .do 的请求
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
