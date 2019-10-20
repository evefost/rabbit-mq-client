package com.eve.common;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.eve.common.GlobalConstant.TOKEN;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/9/12
 */
public class ServerContextHolder {

    private final static ThreadLocal<Map<String, Object>> contextHolder = ThreadLocal.withInitial(() -> new HashMap<>());

    public static void setData(String key, Object value) {
        if (value == null) {
            contextHolder.get().remove(key);
        } else {
            contextHolder.get().put(key, value);
        }
    }

    public static Object getData(String key) {
        return contextHolder.get().get(key);
    }


    public static String getToken() {
        return (String) contextHolder.get().get(TOKEN);
    }

    public static void setToken(String token) {
        if (StringUtils.isEmpty(token)) {
            contextHolder.get().remove(TOKEN);
        } else {
            contextHolder.get().put(TOKEN, token);
        }
    }

    public static String getTenantId() {
        return (String) contextHolder.get().get(GlobalConstant.TENANT_ID);
    }

    public static void setTenantId(String tenantId) {
        if (StringUtils.isEmpty(tenantId)) {
            contextHolder.get().remove(GlobalConstant.TENANT_ID);
        } else {
            contextHolder.get().put(GlobalConstant.TENANT_ID, tenantId);
        }
    }


}
