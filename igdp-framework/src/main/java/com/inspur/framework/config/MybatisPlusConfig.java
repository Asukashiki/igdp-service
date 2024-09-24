package com.inspur.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liyunlong
 */
@Configuration
public class MybatisPlusConfig {
    @Value("${common.datasource.dialect:dm}")
    private String dialect;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //Dbtype.数据库 可改成自己对应的数据库，当前为MYSQL。
        DbType dbType = DbType.MYSQL;
        switch (dialect) {
            case "oracle":
                dbType = DbType.ORACLE;
                break;
            //瀚高数据库
            case "highgo":
                dbType = DbType.HIGH_GO;
                break;
            //达梦数据库
            case "dm":
                dbType = DbType.DM;
                break;
            //神通数据库
            case "oscar":
                dbType = DbType.OSCAR;
                break;
            //人大金仓
            case "kingbasees":
                dbType = DbType.KINGBASE_ES;
            default:
                break;
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        return interceptor;
    }
}
