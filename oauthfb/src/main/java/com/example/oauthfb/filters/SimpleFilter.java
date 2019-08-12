package com.example.oauthfb.filters;


import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.HttpResource;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class SimpleFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie cookie[] = req.getCookies();
        if (cookie != null)
            filterchain.doFilter(request, response);
        else {
            if(req.getRequestURI().equals("/facebook/userinfo"))
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            else
                filterchain.doFilter(request, response);


        }
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }
}
