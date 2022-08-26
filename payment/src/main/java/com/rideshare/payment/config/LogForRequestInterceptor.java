package com.rideshare.payment.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LogForRequestInterceptor implements HandlerInterceptor {

    private Logger logger = LogManager.getLogger();
    private UUID requestID;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        requestID = UUID.randomUUID();

        request.setAttribute("id", requestID);

        logger.debug("Request Begin ::: id: "+ requestID + "\t uri:" + uri);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        String uri = request.getRequestURI();
        logger.debug("Request End ::: id: "+ requestID + "\t uri:" + uri);
    }
}
