package com.example.oauthfb.filters;


import com.example.oauthfb.controllers.FacebookController;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SimpleFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFilter.class);
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//        Cookie cookie[] = req.getCookies();
//        LOGGER.info("SUNT AICI LA INCEPUT");
//        if (cookie != null)
//            filterchain.doFilter(request, response);
//        else {
//            LOGGER.info("SUNT AICI");
//            if(req.getRequestURI().equals("/facebook/userinfo"))
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            else
//                filterchain.doFilter(request, response);
//        }
        filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }
}
