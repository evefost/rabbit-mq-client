package com.eve.common;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */
public class WrapperData<T> {

    private String tenantId;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
