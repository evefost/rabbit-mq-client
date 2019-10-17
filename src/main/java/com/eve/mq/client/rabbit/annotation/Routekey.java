package com.eve.mq.client.rabbit.annotation;

import java.lang.annotation.*;

/**
 * 生产者
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Routekey {

    String value();
}
