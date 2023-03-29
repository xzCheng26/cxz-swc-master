package com.cxz.mushu;

import com.rsbi.ext.engine.control.ExtContextLoaderListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
@MapperScan("com.cxz.mushu.mapper")//使用MapperScan批量扫描所有的Mapper接口；
public class SwcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwcApplication.class, args);
    }


    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new ExtContextLoaderListener());
        return servletListenerRegistrationBean;
    }
}

