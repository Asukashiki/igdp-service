package com.inspur.assets.monitor.service;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liyunlong
 * @version 1.0
 * @date 2024/8/29 11:42
 */
@Service
public class AssetsAlertEventFactory implements InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;


    private static final String BASE_NAME = "AssetsAlertEventService";

    public static Map<String, IAssetsAlertEventService> strategyMaps = new HashMap<>();

    /**
     * 根据依赖平台名称获取对应的实现类进行处理
     **/
    public IAssetsAlertEventService getClassBySysName(String grantType) throws Exception {
        IAssetsAlertEventService api = strategyMaps.get(grantType + BASE_NAME);
        if (null == api) {
            throw new Exception("未匹配到有效的方法处理本请求");
        }
        return api;
    }


    /**
     * 启动时将所有处理类加载好由spring进行管理
     * <p>
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, IAssetsAlertEventService> beansOfFuncType = applicationContext.getBeansOfType(IAssetsAlertEventService.class);
        strategyMaps.putAll(beansOfFuncType);

    }
}
