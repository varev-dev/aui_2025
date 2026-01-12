package dev.varev.node.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Value("${INSTANCE_ID:unknown}")
    private String instanceId;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        
        log.info("[INSTANCE: {}] {} {}", instanceId, method, uri);
        
        response.addHeader("X-Instance-ID", instanceId);
        
        return true;
    }
}
