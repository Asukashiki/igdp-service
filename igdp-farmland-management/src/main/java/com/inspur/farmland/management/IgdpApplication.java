package com.inspur.farmland.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 农田管理模块启动类
 * 
 * @author inspur
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.inspur")
@MapperScan("com.inspur.farmland.management.mapper")
public class IgdpApplication {

    public static void main(String[] args) {
        SpringApplication.run(IgdpApplication.class, args);
        System.out.println("农田管理模块启动成功");
    }
}