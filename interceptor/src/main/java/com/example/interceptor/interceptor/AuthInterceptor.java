package com.example.interceptor.interceptor;

import com.example.interceptor.annotation.Auth;
import com.example.interceptor.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uriStr = request.getRequestURI();
        URI uri = UriComponentsBuilder.fromUriString(uriStr)
                .query(request.getQueryString())
                .build()
                .toUri();
        log.info("request uri : {}", uri);

        boolean hasAnnotation = checkAnnotation(handler, Auth.class);
        log.info("has Auth annotation : {}", hasAnnotation);

        if (hasAnnotation) {
            String query = uri.getQuery();
            log.info("query : {}", query);
            if (query.equals("auth=admin")) {
                return true;
            }
            throw new AuthException("관리자 권한이 아닙니다.");
        }
        return true;
    }

    private boolean checkAnnotation(Object handler, Class clazz) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethodAnnotation(clazz) != null || handlerMethod.getBeanType().getAnnotation(clazz) != null) {
            return true;
        }

        return false;
    }
}
