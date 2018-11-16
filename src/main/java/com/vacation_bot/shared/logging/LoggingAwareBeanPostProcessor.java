package com.vacation_bot.shared.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * Spring bean post processor that injects a logger into beans that want it.
 */
public class LoggingAwareBeanPostProcessor implements BeanPostProcessor, Ordered {

    @Override
    public Object postProcessBeforeInitialization( final Object bean, final String beanName ) throws BeansException {
        if ( bean instanceof AbstractLoggingAware ) {
            Logger logger = LoggerFactory.getLogger( bean.getClass() );
            ( ( AbstractLoggingAware ) bean ).setLogger( logger );
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
