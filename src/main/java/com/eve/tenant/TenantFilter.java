package com.eve.tenant;

import com.eve.common.ServerContextHolder;
import com.eve.tenant.adapter.CompositeTenantMappingAdapter;
import com.eve.tenant.adapter.TenantMappingAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author xieyang
 * @date 19/7/27
 */

@Order(-1000)
@Component
public class TenantFilter implements Filter, ApplicationContextAware {


    private TenantMappingAdapter tenantMappingAdapter;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String tenantId = tenantMappingAdapter.getTenantId(servletRequest);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uuid = request.getHeader("log-uuid");
        ServerContextHolder.setData("log-uuid", uuid);
        ServerContextHolder.setTenantId(tenantId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ServerContextHolder.setTenantId(null);
            ServerContextHolder.setData("log-uuid", null);
        }
    }

    @Override
    public void destroy() {

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, TenantMappingAdapter> beansOfType = applicationContext.getBeansOfType(TenantMappingAdapter.class);
        Collection<TenantMappingAdapter> values = beansOfType.values();
        tenantMappingAdapter = new CompositeTenantMappingAdapter(values);
    }
}
