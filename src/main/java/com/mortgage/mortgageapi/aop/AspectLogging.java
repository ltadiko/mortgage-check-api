package com.mortgage.mortgageapi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class AspectLogging {


    @Pointcut("within(com.mortgage.mortgageapi.controller..*)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("START Execution: {}.{}() with arguments: {}", className, methodName, Arrays.toString(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();

            stopWatch.stop();
            log.info("END Execution: {}.{}() executed in {} ms", className, methodName, stopWatch.getTotalTimeMillis());

            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("EXCEPTION in {}.{}(): {} after {} ms", className, methodName, e.getMessage(), stopWatch.getTotalTimeMillis());
            throw e;
        }
    }
}