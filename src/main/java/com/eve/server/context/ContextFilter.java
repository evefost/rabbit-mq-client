package com.eve.server.context;

import com.eve.common.GlobalConstant;
import com.eve.common.ServerContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xieyang
 * @date 19/7/27
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class ContextFilter implements Filter {



    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String tenantId = request.getHeader(GlobalConstant.TENANT_ID);
            String uuid = request.getHeader(GlobalConstant.LOG_UUID);
            String token = request.getHeader(GlobalConstant.TOKEN);
            ServerContextHolder.setTenantId(tenantId);
            ServerContextHolder.setData(GlobalConstant.LOG_UUID, uuid);
            ServerContextHolder.setToken(token);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ServerContextHolder.setTenantId(null);
            ServerContextHolder.setData(GlobalConstant.LOG_UUID, null);
            ServerContextHolder.setToken(null);
        }
    }

    @Override
    public void destroy() {

    }


}
