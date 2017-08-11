package org.zhenchao.oauth.service.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.zhenchao.oauth.service.AppInfoService;
import org.zhenchao.oauth.service.impl.AppInfoServiceImpl;

/**
 * @author zhenchao.wang 2017-08-08 22:44
 * @version 1.0.0
 */
@Component
@Lazy(false)
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanFactory.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static AppInfoService getAppInfoService() {
        return getBean(AppInfoServiceImpl.class);
    }

}
