package com.bankapp.bankapp.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j //SELF LOGGING FACADE FOR JAVA
public class Logging {

    @Pointcut("execution(public * com.bankapp.bankapp.app.controller.*.*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(public * com.bankapp.bankapp.app.service.*.*(..))")
    public void serviceLog() {
    }

    /**
     * регистрирует IP-адрес, URL-адрес, HTTP-метод и имя метода контроллера для каждого входящего запроса
     **/
    @Before("controllerLog()")
    public void doBeforeController(JoinPoint jp) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("""
                        
                        
                        
                        
                        NEW REQUEST:
                        IP : {}
                        URL : {}
                        HTTP_METHOD : {}
                        CONTROLLER_METHOD : {}.{}""",
                request.getRemoteAddr(),
                request.getRequestURL().toString(),
                request.getMethod(),
                jp.getSignature().getDeclaringTypeName(),
                jp.getSignature().getName());

    }

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint jp) {
        log.info("""
                        
                        
                        
                        
                        
                        RUN SERVICE:
                        SERVICE_METHOD : {}.{}
                        """,
                jp.getSignature().getDeclaringTypeName(), jp.getSignature().getName());
    }

    /**
     * регистрирует возвращаемое значение метода контроллера
     **/
    @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
    public void doAfterReturning(Object returnObject) {
        log.info("""
                        
                        
                        
                        
                        
                        
                        
                        Return value: {}
                        END OF REQUEST
                        """,
                returnObject);
    }

    /**
     *  будет выполнен после того, как перехваченные методы выбросят исключение
     */
    @AfterThrowing(throwing = "ex", pointcut = "controllerLog()")
    public void throwsException(JoinPoint jp, Exception ex) {
        log.error("Request throw an exception. Cause - {}. {}",
                Arrays.toString(jp.getArgs()), ex.getMessage());
    }
}