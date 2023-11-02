package com.joan.security.aspect;

import com.joan.security.dto.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com.joan.security.service..*(..)) || execution(* com.joan.security.controller..*(..))")
    public Object logServices(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("Executing {} inside {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        return proceedingJoinPoint.proceed();
    }

    @Around("execution(* com.joan.security.controller..*(..))")
    public Object logUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            logger.info("Logged user {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        return proceedingJoinPoint.proceed();
    }
}
