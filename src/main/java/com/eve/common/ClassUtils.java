package com.eve.common;

import java.lang.reflect.Field;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/16
 */
public class ClassUtils {


    public static Field fieldExist(Class clzz, String fieldName) {
        Field field = null;
        try {
            field = clzz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {

        }
        if (field == null) {
            Class superclass = clzz.getSuperclass();
            if (superclass == null) {
                return null;
            }
            try {
                field = superclass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }
        }
        return field;
    }
}
