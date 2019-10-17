package com.eve.tenant.adapter;

import javax.servlet.ServletRequest;

/**
 * Created by xieyang on 19/7/28.
 */
public interface TenantMappingAdapter {


    String getTenantId(ServletRequest servletRequest);

}
