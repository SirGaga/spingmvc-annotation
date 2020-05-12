package com.zhangjie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
// Spring 的根容器不扫描 Controller 组件，是一个父容器
@ComponentScan(value="com.zhangjie",excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})
})
public class RootConfig {
}