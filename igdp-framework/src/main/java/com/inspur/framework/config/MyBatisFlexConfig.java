//package com.inspur.framework.config;
//
//import com.mybatisflex.core.FlexGlobalConfig;
//import com.mybatisflex.core.audit.AuditManager;
//import com.mybatisflex.core.audit.ConsoleMessageCollector;
//import com.mybatisflex.core.datasource.DataSourceDecipher;
//import com.mybatisflex.core.mybatis.FlexConfiguration;
//import com.mybatisflex.spring.boot.ConfigurationCustomizer;
//import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
//import org.apache.ibatis.logging.stdout.StdOutImpl;
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
///**
// * mybatis-flex配置
// *
// * @author dataprince数据小王子
// */
//@Configuration
//@MapperScan("com.inspur.*.mapper")
//public class MyBatisFlexConfig implements ConfigurationCustomizer, MyBatisFlexCustomizer {
//
//    private static final Logger logger = LoggerFactory.getLogger("mybatis-flex-sql");
//
//    /**
//     * 数据源解密
//     */
//    @Bean
//    public DataSourceDecipher decipher() {
//        DataSourceDecipher decipher = new Decipher();
//        return decipher;
//    }
//
//    @Override
//    public void customize(FlexConfiguration configuration) {
//        //mybatis实现的打印sql到控制台，便于调试
//        configuration.setLogImpl(StdOutImpl.class);
//    }
//
//    /**
//     * Mybatis-Flex自定义初始化配置
//     *
//     * @param globalConfig 全局配置
//     */
//    @Override
//    public void customize(FlexGlobalConfig globalConfig) {
//        // 注册全局数据填充监听器
//        globalConfig.registerInsertListener(new EntityInsertListener(), BaseEntity.class);
//        globalConfig.registerUpdateListener(new EntityUpdateListener(), BaseEntity.class);
//
//        // 开启审计功能
//        AuditManager.setAuditEnable(true);
////        AuditManager.setMessageFactory(new AuditMessageFactory());
//        // 设置 SQL 审计收集器
//        AuditManager.setMessageCollector(new ConsoleMessageCollector());
//    }
//
//}
