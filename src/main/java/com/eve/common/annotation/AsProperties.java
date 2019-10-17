package com.eve.common.annotation;


import java.lang.annotation.*;

/**
 * 标记为属性配置
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AsProperties {


}
