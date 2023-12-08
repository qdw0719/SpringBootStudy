package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/api/temp/*")
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(httpServletRequest, httpServletResponse);

        String uri = httpServletRequest.getRequestURI();

        BufferedReader br = httpServletRequest.getReader();
        br.lines().forEach(line -> {
            log.info("uri: {}, line: {}", uri, line);
        });

        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request uri : {}, request body : {}", uri, reqContent);

        String resContent = new String(httpServletResponse.getContentAsByteArray());
        int httpStatuscode = httpServletResponse.getStatus();

        httpServletResponse.copyBodyToResponse();
        log.info("response Status : {}, response body : {}", httpStatuscode, resContent);
    }
}
