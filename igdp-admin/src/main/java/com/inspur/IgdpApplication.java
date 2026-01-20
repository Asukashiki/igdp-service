package com.inspur;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author liyunlong
 */
@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.inspur.**.mapper")
public class IgdpApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(IgdpApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  焱宇服务启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "....................................................\n" +
                "....................................................\n" +
                "........................./\\.........................\n" +
                "..................______/__\\_______.................\n" +
                "..................||-------------||.................\n" +
                "..................||             ||.................\n" +
                "..................||    \\|||/    ||.................\n" +
                "..................||   [ @-@ ]   ||.................\n" +
                "..................||    ( ' )    ||.......       ...\n" +
                "..................||    _(O)_    ||.......|EXIT |...\n" +
                "..................||   / >=< \\   ||.......|==>> |...\n" +
                "..................||__/_|_:_|_\\__||.................\n" +
                "..................-----------------.................\n" +
                "....................................................\n" +
                "....................................................");
    }
}
