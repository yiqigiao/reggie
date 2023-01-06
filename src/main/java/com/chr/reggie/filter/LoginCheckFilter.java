package com.chr.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.chr.reggie.common.BaseContext;
import com.chr.reggie.common.R;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter(urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获得本次请求
        String requestURI = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login"
        };

        boolean check = check(urls, requestURI);
        // 是不放行的路径
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        Long employee = (Long) request.getSession().getAttribute("employee");
        if (employee != null) {
            BaseContext.setCurrentId(employee);
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    @Override
    public void destroy() { }

    /**
     * 匹配路径
     * @param urls 放行的路径
     * @param requestURI 请求路径
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
